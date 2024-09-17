package com.indium.ipl_match.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.indium.ipl_match.entity.MatchInfo;
import com.indium.ipl_match.service.*;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.slf4j.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

@RestController
@Tag(name = "Match API", description = "API to manage and query match data")
@RequestMapping("/api/matches")
public class MatchController {

    private static Logger logger = LoggerFactory.getLogger(MatchController.class);

    @Autowired
    private MatchInsertService matchInsertService;

    @Autowired
    private MatchQueryService matchQueryService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    // Uploading the JSON file
    @Operation(
            summary = "Upload match data",
            description = "Upload a new JSON file containing match data"
    )
    @ApiResponse(description = "Upload successful", responseCode = "200")
    @PostMapping
    public ResponseEntity<String> uploadMatchData(@RequestBody String jsonContent) throws JsonProcessingException {
        try {
            objectMapper.readTree(jsonContent);
            matchInsertService.insertMatchData(jsonContent);
            String logMessage = "New match data file inserted";
            logger.info(logMessage);
            kafkaTemplate.send("match-logs-topic", "Upload", logMessage);
            return ResponseEntity.ok("Match data uploaded successfully!");
        } catch (JsonProcessingException e) {
            String errorMessage = "Invalid JSON format: " + e.getMessage();
            logger.error(errorMessage);
            kafkaTemplate.send("match-logs-topic", "UploadError", errorMessage);
            return ResponseEntity.status(400).body(errorMessage);
        } catch (IOException e) {
            String errorMessage = "Error occurred while processing the match data: " + e.getMessage();
            logger.error(errorMessage);
            kafkaTemplate.send("match-logs-topic", "UploadError", errorMessage);
            return ResponseEntity.status(500).body(errorMessage);  // Return 500 Internal Server Error
        }
    }

    // to get the number of matches played by a player -- Query 1
    @Operation(
            summary = "Find matches by player name",
            description = "Retrieve all matches played by a given player"
    )
    @ApiResponses({
            @ApiResponse(description = "Matches found", responseCode = "200"),
            @ApiResponse(description = "No matches found", responseCode = "204")
    })
    @Parameter(name = "playerName", description = "The name of the player to query")
    @GetMapping("/player/match/{playerName}")
    public List<MatchInfo> findMatchesByPlayerName(@PathVariable String playerName) throws JsonProcessingException {
        List<MatchInfo> matches = matchQueryService.getMatchesByPlayerName(playerName);
        String logMessage = "The matches played by " + playerName + " were queried";
        logger.info(logMessage);
        kafkaTemplate.send("match-logs-topic", "Query 1", logMessage);
        return matches;
    }

    // to get the total runs scored by a player -- Query 2
    @Operation(
            summary = "Get match scores by date",
            description = "Retrieve the scores of all matches played on a given date"
    )
    @ApiResponses({
            @ApiResponse(description = "Match scores retrieved successfully", responseCode = "200"),
            @ApiResponse(description = "Invalid date format", responseCode = "400"),
            @ApiResponse(description = "No matches found", responseCode = "204")
    })
    @Parameter(name = "date", description = "The date for which match scores are requested", example = "2024-09-13")
    @GetMapping("/score")
    public Integer getCumulativeScore(@RequestParam String playerName) throws JsonProcessingException {
        Integer cumulativeScore = matchQueryService.getCumulativeScoreByPlayerName(playerName);
        String logMessage = "The cumulative score of " + playerName + " was queried";
        logger.info(logMessage);
        kafkaTemplate.send("match-logs-topic", "Query 2", logMessage);
        return cumulativeScore;
    }

    // to get the total score of the match on the given date -- Query 3
    @Operation(
            summary = "Get match scores by date",
            description = "Retrieve the scores of all matches played on a given date"
    )
    @ApiResponses({
            @ApiResponse(description = "Match scores retrieved successfully", responseCode = "200"),
            @ApiResponse(description = "Invalid date format", responseCode = "400"),
            @ApiResponse(description = "No matches found", responseCode = "204")
    })
    @Parameter(name = "date", description = "The date for which match scores are requested", example = "2024-09-13")
    @GetMapping("/match/date")
    public ResponseEntity<String> getMatchScoresByDate(@RequestParam String date) throws JsonProcessingException {
        try {
            LocalDate matchDate = LocalDate.parse(date);
            List<Map<String, Object>> matchScores = matchQueryService.getMatchScoresByDate(matchDate);
            if (matchScores.isEmpty()) {
                String logMessage = "No matches found on " + date;
                logger.info(logMessage);
                kafkaTemplate.send("match-logs-topic", "Query 3", logMessage);
                return ResponseEntity.status(204).body(logMessage);
            } else {
                String logMessage = "Matches on " + date + " were queried";
                logger.info(logMessage);
                kafkaTemplate.send("match-logs-topic", "Query 3", logMessage);
                return ResponseEntity.ok(objectMapper.writeValueAsString(matchScores));
            }
        } catch (DateTimeParseException e) {
            String errorMessage = "Invalid date format for date: " + date;
            logger.error(errorMessage);
            kafkaTemplate.send("match-logs-topic", "QueryError", errorMessage);
            return ResponseEntity.status(400).body(errorMessage);
        }
    }

    // to display the list of the required number of top batsmen -- Query 4
    @Operation(
            summary = "Get top batsmen",
            description = "Retrieve the top N batsmen"
    )
    @ApiResponses({
            @ApiResponse(description = "Top batsmen retrieved successfully", responseCode = "200"),
            @ApiResponse(description = "Invalid number of players", responseCode = "400")
    })
    @Parameter(name = "numberOfPlayers", description = "The number of top batsmen to retrieve")
    @GetMapping("/top-batsmen")
    public List<Object[]> getTopBatsmen(@RequestParam int numberOfPlayers) throws JsonProcessingException {
        List<Object[]> topBatsmen = matchQueryService.getTopBatsmenWithNameAndId(numberOfPlayers);
        String logMessage = "Top " + numberOfPlayers + " batsmen were queried";
        logger.info(logMessage);
        kafkaTemplate.send("match-logs-topic", "Query 4", logMessage);
        return topBatsmen;
    }
}