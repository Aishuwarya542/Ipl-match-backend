package com.indium.ipl_match;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.indium.ipl_match.entity.*;
import com.indium.ipl_match.repository.*;
import com.indium.ipl_match.service.MatchInsertService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.ArgumentCaptor;
import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class MatchInsertServiceTest {

    @Autowired
    private MatchInsertService matchInsertService;

    @MockBean
    private MetaRepository metaRepository;

    @MockBean
    private MatchInfoRepository matchInfoRepository;

    @MockBean
    private TeamRepository teamRepository;

    @MockBean
    private PlayerRepository playerRepository;

    @MockBean
    private OfficialRepository officialRepository;

    @MockBean
    private InningRepository inningRepository;

    @MockBean
    private OverRepository overRepository;

    @MockBean
    private DeliveryRepository deliveryRepository;

    @MockBean
    private WicketRepository wicketRepository;

    @MockBean
    private PowerplayRepository powerplayRepository;

    @MockBean
    private TossRepository tossRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        Mockito.when(metaRepository.save(any(Meta.class))).thenReturn(new Meta());
        Mockito.when(matchInfoRepository.save(any(MatchInfo.class))).thenReturn(new MatchInfo());
        Mockito.when(teamRepository.save(any(Team.class))).thenReturn(new Team());
    }

    @Test
    public void testInsertMatchData() throws IOException {
        String jsonContent = "{ \"meta\": { \"data_version\": \"1.0\", \"created\": \"2023-09-15\", \"revision\": 1 }, \"info\": { \"event\": { \"match_number\": 100 }, \"city\": \"City A\", \"dates\": [\"2023-09-15\"], \"venue\": \"Venue A\", \"season\": \"Season 2023\", \"match_type\": \"Test\", \"gender\": \"male\", \"overs\": 50, \"event\": { \"name\": \"Event A\" }, \"player_of_match\": [\"Player 1\"], \"outcome\": { \"winner\": \"Team A\", \"by\": { \"runs\": 100 } }, \"teams\": [\"Team A\", \"Team B\"], \"toss\": { \"winner\": \"Team A\", \"decision\": \"bat\" } } }";

        matchInsertService.insertMatchData(jsonContent);

        Mockito.verify(metaRepository, Mockito.times(1)).save(any(Meta.class));
        Mockito.verify(matchInfoRepository, Mockito.times(1)).save(any(MatchInfo.class));
        Mockito.verify(teamRepository, Mockito.times(2)).save(any(Team.class));  // Expecting 2 teams
    }

    @Test
    public void testInsertMetaAndMatchInfo() throws IOException {
        String jsonContent = "{ \"meta\": { \"data_version\": \"1.0\", \"created\": \"2023-09-15\", \"revision\": 1 }, \"info\": { \"event\": { \"match_number\": 100 }, \"city\": \"City A\", \"dates\": [\"2023-09-15\"], \"venue\": \"Venue A\", \"season\": \"Season 2023\", \"match_type\": \"Test\", \"gender\": \"male\", \"overs\": 50, \"event\": { \"name\": \"Event A\" }, \"player_of_match\": [\"Player 1\"], \"outcome\": { \"winner\": \"Team A\", \"by\": { \"runs\": 100 } }, \"teams\": [\"Team A\", \"Team B\"] } }";

        matchInsertService.insertMatchData(jsonContent);

        ArgumentCaptor<Meta> metaCaptor = ArgumentCaptor.forClass(Meta.class);
        Mockito.verify(metaRepository).save(metaCaptor.capture());
        Meta savedMeta = metaCaptor.getValue();

        System.out.println("Saved Meta: " + savedMeta);

        assertEquals("1.0", savedMeta.getDataVersion(), "Expected data version 1.0");
        assertEquals(LocalDate.of(2023, 9, 15), savedMeta.getCreated(), "Expected created date to be 2023-09-15");
        assertEquals(1, savedMeta.getRevision(), "Expected revision to be 1");
    }



    @Test
    public void testInsertMatchOfficials() throws IOException {
        String jsonContent = "{ \"meta\": { \"data_version\": \"1.0\", \"created\": \"2023-09-15\", \"revision\": 1 }, \"info\": { \"event\": { \"match_number\": 100 }, \"city\": \"City A\", \"dates\": [\"2023-09-15\"], \"venue\": \"Venue A\", \"season\": \"Season 2023\", \"match_type\": \"Test\", \"gender\": \"male\", \"overs\": 50, \"event\": { \"name\": \"Event A\" }, \"player_of_match\": [\"Player 1\"], \"outcome\": { \"winner\": \"Team A\", \"by\": { \"runs\": 100 } }, \"teams\": [\"Team A\", \"Team B\"], \"officials\": { \"umpire\": [\"Official A\", \"Official B\"], \"referee\": [\"Official C\"] } } }";

        matchInsertService.insertMatchData(jsonContent);

        Mockito.verify(officialRepository, Mockito.times(3)).save(any(Official.class));  // Expecting 3 officials (2 umpires, 1 referee)
    }

    @Test
    public void testInsertMatchInfo() throws IOException {
        String jsonContent = "{ \"meta\": { \"data_version\": \"1.0\", \"created\": \"2023-09-15\", \"revision\": 1 }, \"info\": { \"event\": { \"match_number\": 0 }, \"city\": \"City B\", \"dates\": [\"2023-09-16\"], \"venue\": \"Venue B\", \"season\": \"Season 2023\", \"match_type\": \"ODI\", \"gender\": \"female\", \"overs\": 50, \"event\": { \"name\": \"Event B\" }, \"player_of_match\": [\"Player 2\"], \"outcome\": { \"winner\": \"Team B\", \"by\": { \"runs\": 150 } }, \"teams\": [\"Team A\", \"Team B\"] } }";

        matchInsertService.insertMatchData(jsonContent);

        ArgumentCaptor<MatchInfo> matchInfoCaptor = ArgumentCaptor.forClass(MatchInfo.class);
        Mockito.verify(matchInfoRepository).save(matchInfoCaptor.capture());
        MatchInfo savedMatchInfo = matchInfoCaptor.getValue();

        System.out.println("Saved MatchInfo: " + savedMatchInfo);

        assertEquals(0, savedMatchInfo.getMatchNumber(), "Expected match number to be 0");
        assertEquals("City B", savedMatchInfo.getCity(), "Expected city to be City B");
        assertEquals(LocalDate.of(2023, 9, 16), savedMatchInfo.getDate(), "Expected date to be 2023-09-16");
        assertEquals("Venue B", savedMatchInfo.getVenue(), "Expected venue to be Venue B");
        assertEquals("ODI", savedMatchInfo.getMatchType(), "Expected match type to be ODI");
        assertEquals("female", savedMatchInfo.getGender(), "Expected gender to be female");
        assertEquals(50, savedMatchInfo.getOvers(), "Expected overs to be 50");
        assertEquals("Event B", savedMatchInfo.getEventName(), "Expected event name to be Event B");
        assertEquals("Player 2", savedMatchInfo.getPlayerOfMatch(), "Expected player of match to be Player 2");
        assertEquals("Team B", savedMatchInfo.getWinner(), "Expected winner to be Team B");
        assertEquals(150, savedMatchInfo.getOutcomeByRuns(), "Expected outcome by runs to be 150");
    }
}

