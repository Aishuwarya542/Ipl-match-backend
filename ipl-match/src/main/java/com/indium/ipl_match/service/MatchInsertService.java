package com.indium.ipl_match.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.indium.ipl_match.entity.*;
import com.indium.ipl_match.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MatchInsertService {

    @Autowired
    private MetaRepository metaRepository;

    @Autowired
    private MatchInfoRepository matchInfoRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private OfficialRepository officialRepository;

    @Autowired
    private InningRepository inningRepository;

    @Autowired
    private OverRepository overRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private WicketRepository wicketRepository;

    @Autowired
    private PowerplayRepository powerplayRepository;

    @Autowired
    private TossRepository tossRepository;

    @Autowired
    private TargetRepository targetRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @CacheEvict(value = {"matchesByPlayer", "cumulativeScore", "matchScoresByDate", "TopBatsmen"}, allEntries = true)
    @Transactional
    public void insertMatchData(String jsonContent) throws IOException {
        // Parse the JSON content
        JsonNode rootNode = objectMapper.readTree(jsonContent);

        // Insert Meta data
        JsonNode metaNode = rootNode.path("meta");
        Meta meta = new Meta();
        meta.setDataVersion(metaNode.path("data_version").asText());

// Parse the date string safely
        String createdStr = metaNode.path("created").asText(null); // Use null as default if "created" is missing
        if (createdStr != null && !createdStr.isEmpty()) {
            try {
                LocalDate createdDate = LocalDate.parse(createdStr);
                meta.setCreated(createdDate);
            } catch (DateTimeParseException e) {
                System.err.println("Error parsing the date: " + createdStr);
                throw new RuntimeException("Date parsing failed for date: " + createdStr, e);
            }
        } else {
            System.err.println("Created date is missing or empty");
            throw new RuntimeException("Date parsing failed: Created date is missing or empty");
        }

        meta.setRevision(metaNode.path("revision").asInt());

// Save meta entity
        Meta savedMeta = metaRepository.save(meta);


        // Insert Match Info
        JsonNode matchInfoNode = rootNode.path("info");
        MatchInfo matchInfo = new MatchInfo();
        matchInfo.setMatchNumber(matchInfoNode.path("event").path("match_number").asInt());
        matchInfo.setCity(matchInfoNode.path("city").asText());
        matchInfo.setDate(LocalDate.parse(matchInfoNode.path("dates").get(0).asText()));
        matchInfo.setVenue(matchInfoNode.path("venue").asText());
        matchInfo.setSeason(matchInfoNode.path("season").asText());
        matchInfo.setMatchType(matchInfoNode.path("match_type").asText());
        matchInfo.setGender(matchInfoNode.path("gender").asText());
        matchInfo.setOvers(matchInfoNode.path("overs").asInt());
        matchInfo.setEventName(matchInfoNode.path("event").path("name").asText());
        matchInfo.setPlayerOfMatch(matchInfoNode.path("player_of_match").get(0).asText());
        matchInfo.setOutcomeByRuns(matchInfoNode.path("outcome").path("by").path("runs").asInt());
        matchInfo.setWinner(matchInfoNode.path("outcome").path("winner").asText());
        matchInfo.setBallsPerOver(matchInfoNode.path("balls_per_over").asInt());
        matchInfo.setMeta(savedMeta); // Link with meta
        MatchInfo savedMatchInfo = matchInfoRepository.save(matchInfo);

        // Insert Teams
        JsonNode teamsNode = matchInfoNode.path("teams");
        List<Team> teams = new ArrayList<>();
        for (JsonNode teamNameNode : teamsNode) {
            Team team = new Team();
            team.setMatchInfo(savedMatchInfo);
            team.setTeamName(teamNameNode.asText());
            teams.add(teamRepository.save(team));
        }

        // Insert Players
        JsonNode playersNode = matchInfoNode.path("players");
        JsonNode registryNode = matchInfoNode.path("registry").path("people");
        playersNode.fields().forEachRemaining(entry -> {
            String teamName = entry.getKey();
            Team team = teams.stream()
                    .filter(t -> t.getTeamName().equals(teamName))
                    .findFirst()
                    .orElse(null);
            if (team != null) {
                entry.getValue().forEach(playerNode -> {
                    String playerName = playerNode.asText();
                    // Check if the player already exists in the database
                    Player existingPlayer = playerRepository.findByPlayerName(playerName).orElse(null);
                    if (existingPlayer == null) {
                        Player player = new Player();
                        player.setPlayerName(playerName);
                        player.setTeam(team);
                        player.setMatchInfo(savedMatchInfo);
                        String registerId = registryNode.path(playerName).asText();
                        player.setRegisterId(registerId);
                        playerRepository.save(player);
                    } else {
                        System.out.println("Player already exists: " + playerName);
                    }
                });
            }
        });


        // Insert Officials
        JsonNode officialsNode = matchInfoNode.path("officials");
        officialsNode.fields().forEachRemaining(entry -> {
            String role = entry.getKey();
            entry.getValue().forEach(officialNameNode -> {
                Official official = new Official();
                official.setRole(role);
                official.setOfficialName(officialNameNode.asText());
                official.setMatchInfo(savedMatchInfo);
                officialRepository.save(official);
            });
        });

        // Insert Innings
        JsonNode inningsNode = rootNode.path("innings");
        for (JsonNode inningNode : inningsNode) {
            Inning inning = new Inning();
            inning.setMatchInfo(savedMatchInfo);
            Team team = teams.stream()
                    .filter(t -> t.getTeamName().equals(inningNode.path("team").asText()))
                    .findFirst()
                    .orElse(null);
            inning.setTeam(team);
            Inning savedInning = inningRepository.save(inning);

            // Insert Target
            if (inningNode.has("target")) {
                JsonNode targetNode = inningNode.path("target");
                Target target = new Target();
                target.setInning(savedInning);
                target.setTargetRuns(targetNode.path("runs").asInt(0));  // Default to 0 if "runs" is missing
                target.setTargetOvers(targetNode.path("overs").asInt(0));  // Default to 0 if "overs" is missing
                targetRepository.save(target);
            }

            // Insert Overs
            JsonNode oversNode = inningNode.path("overs");
            for (JsonNode overNode : oversNode) {
                Overs over = new Overs();
                over.setInning(savedInning);
                over.setOverNumber(overNode.path("over").asInt());
                Overs savedOver = overRepository.save(over);  // Save the over to get the ID


                // Insert Deliveries
                JsonNode deliveriesNode = overNode.path("deliveries");
                int ballNumber = 1;  // Manually generate ball numbers since the JSON doesn't have it
                for (JsonNode deliveryNode : deliveriesNode) {
                    Delivery delivery = new Delivery();
                    delivery.setInning(savedInning);
                    delivery.setOverNumber(savedOver.getOverNumber());  // Use savedOver reference for the over number
                    delivery.setBallNumber(ballNumber++);  // Increment ball number manually
                    delivery.setRunsBatter(deliveryNode.path("runs").path("batter").asInt());
                    delivery.setRunsTotal(deliveryNode.path("runs").path("total").asInt());
                    delivery.setBatter(playerRepository.findByPlayerName(deliveryNode.path("batter").asText()).orElse(null));
                    delivery.setBowler(playerRepository.findByPlayerName(deliveryNode.path("bowler").asText()).orElse(null));
                    delivery.setNonStriker(playerRepository.findByPlayerName(deliveryNode.path("non_striker").asText()).orElse(null));
                    deliveryRepository.save(delivery);

                    // Insert Wickets
                    if (deliveryNode.has("wickets")) {
                        for (JsonNode wicketNode : deliveryNode.path("wickets")) {
                            Wicket wicket = new Wicket();
                            wicket.setDelivery(delivery);
                            wicket.setPlayerOut(wicketNode.path("player_out").asText());
                            wicket.setKindOfDismissal(wicketNode.path("kind").asText());
                            wicketRepository.save(wicket);
                        }
                    }
                }
            }

            // Insert Powerplay Info
            JsonNode powerplayNode = inningsNode.get(0).path("powerplays");
            for (JsonNode powerplayData : powerplayNode) {
                Powerplay powerplay = new Powerplay();
                powerplay.setInning(savedInning);  
                powerplay.setFromOver(powerplayData.path("from").floatValue());
                powerplay.setToOver(powerplayData.path("to").floatValue());
                powerplay.setType(powerplayData.path("type").asText());

                powerplayRepository.save(powerplay);
            }
        }
        // Insert Toss Info
        JsonNode tossNode = matchInfoNode.path("toss");
        Toss toss = new Toss();
        toss.setMatchInfo(savedMatchInfo);
        toss.setTossWinner(tossNode.path("winner").asText());
        toss.setDecision(tossNode.path("decision").asText());
        tossRepository.save(toss);

    }
}
