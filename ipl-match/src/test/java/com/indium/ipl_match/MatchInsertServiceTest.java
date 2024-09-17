//package com.indium.ipl_match;
//
//import com.indium.ipl_match.entity.*;
//import com.indium.ipl_match.repository.*;
//import com.indium.ipl_match.service.MatchInsertService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//public class MatchInsertServiceTest {
//
//    @Autowired
//    private MatchInsertService matchInsertService;
//
//    @MockBean
//    private MetaRepository metaRepository;
//
//    @MockBean
//    private MatchInfoRepository matchInfoRepository;
//
//    @MockBean
//    private TeamRepository teamRepository;
//
//    @MockBean
//    private PlayerRepository playerRepository;
//
//    @MockBean
//    private OfficialRepository officialRepository;
//
//    @MockBean
//    private InningRepository inningRepository;
//
//    @MockBean
//    private OverRepository overRepository;
//
//    @MockBean
//    private DeliveryRepository deliveryRepository;
//
//    @MockBean
//    private WicketRepository wicketRepository;
//
//    @MockBean
//    private PowerplayRepository powerplayRepository;
//
//    @MockBean
//    private TossRepository tossRepository;
//
//    private String jsonContent;
//
//    @BeforeEach
//    public void setup() throws IOException {
//        // Load the JSON content from a file
//        jsonContent = new String(Files.readAllBytes(Paths.get("src/test/resources/test-match-data.json")));
//
//        // Mocking the repository save methods
//        when(metaRepository.save(any(Meta.class))).thenReturn(new Meta());
//        when(matchInfoRepository.save(any(MatchInfo.class))).thenReturn(new MatchInfo());
//        when(teamRepository.save(any(Team.class))).thenReturn(new Team());
////        when(playerRepository.save(any(Player.class))).thenReturn(new Player());
//        when(officialRepository.save(any(Official.class))).thenReturn(new Official());
//        when(inningRepository.save(any(Inning.class))).thenReturn(new Inning());
//        when(overRepository.save(any(Overs.class))).thenReturn(new Overs());
//        when(deliveryRepository.save(any(Delivery.class))).thenReturn(new Delivery());
//        when(wicketRepository.save(any(Wicket.class))).thenReturn(new Wicket());
//        when(powerplayRepository.save(any(Powerplay.class))).thenReturn(new Powerplay());
//        when(tossRepository.save(any(Toss.class))).thenReturn(new Toss());
//
//        Mockito.doNothing().when(playerRepository).save(any(Player.class));  // For void save(Player player)
//
//    }
//
//    @Test
//    public void testInsertMatchData() throws Exception {
//        // Call the insertMatchData method
//        matchInsertService.insertMatchData(jsonContent);
//
//        // Verify if save() methods of repositories were called
//        verify(metaRepository, times(1)).save(any(Meta.class));
//        verify(matchInfoRepository, times(1)).save(any(MatchInfo.class));
//        verify(teamRepository, atLeastOnce()).save(any(Team.class));
////        verify(playerRepository, atLeastOnce()).save(any(Player.class));
//        verify(officialRepository, atLeastOnce()).save(any(Official.class));
//        verify(inningRepository, atLeastOnce()).save(any(Inning.class));
//        verify(overRepository, atLeastOnce()).save(any(Overs.class));
//        verify(deliveryRepository, atLeastOnce()).save(any(Delivery.class));
//        verify(wicketRepository, atLeastOnce()).save(any(Wicket.class));
//        verify(powerplayRepository, atLeastOnce()).save(any(Powerplay.class));
//        verify(tossRepository, times(1)).save(any(Toss.class));
//    }
//}

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
        // Mocking the repository save methods
        Mockito.when(metaRepository.save(any(Meta.class))).thenReturn(new Meta());
        Mockito.when(matchInfoRepository.save(any(MatchInfo.class))).thenReturn(new MatchInfo());
        Mockito.when(teamRepository.save(any(Team.class))).thenReturn(new Team());
//        Mockito.when(playerRepository.save(any(Player.class))).thenReturn(new Player());
        // Mock other repository save methods similarly if needed...
    }

    @Test
    public void testInsertMatchData() throws IOException {
        // Sample valid JSON data for testing
        String jsonContent = "{ \"meta\": { \"data_version\": \"1.0\", \"created\": \"2023-09-15\", \"revision\": 1 }, \"info\": { \"event\": { \"match_number\": 100 }, \"city\": \"City A\", \"dates\": [\"2023-09-15\"], \"venue\": \"Venue A\", \"season\": \"Season 2023\", \"match_type\": \"Test\", \"gender\": \"male\", \"overs\": 50, \"event\": { \"name\": \"Event A\" }, \"player_of_match\": [\"Player 1\"], \"outcome\": { \"winner\": \"Team A\", \"by\": { \"runs\": 100 } }, \"teams\": [\"Team A\", \"Team B\"], \"toss\": { \"winner\": \"Team A\", \"decision\": \"bat\" } } }";

        // Call the method to test
        matchInsertService.insertMatchData(jsonContent);

        // Assertions or verification can be done here
        Mockito.verify(metaRepository, Mockito.times(1)).save(any(Meta.class));
        Mockito.verify(matchInfoRepository, Mockito.times(1)).save(any(MatchInfo.class));
        Mockito.verify(teamRepository, Mockito.times(2)).save(any(Team.class));  // Expecting 2 teams
    }

    @Test
    public void testInsertMetaAndMatchInfo() throws IOException {
        // Sample JSON data for meta and match info
        String jsonContent = "{ \"meta\": { \"data_version\": \"1.0\", \"created\": \"2023-09-15\", \"revision\": 1 }, \"info\": { \"event\": { \"match_number\": 100 }, \"city\": \"City A\", \"dates\": [\"2023-09-15\"], \"venue\": \"Venue A\", \"season\": \"Season 2023\", \"match_type\": \"Test\", \"gender\": \"male\", \"overs\": 50, \"event\": { \"name\": \"Event A\" }, \"player_of_match\": [\"Player 1\"], \"outcome\": { \"winner\": \"Team A\", \"by\": { \"runs\": 100 } }, \"teams\": [\"Team A\", \"Team B\"] } }";

        // Call the method to test
        matchInsertService.insertMatchData(jsonContent);

        // Capture the Meta object passed to the save method
        ArgumentCaptor<Meta> metaCaptor = ArgumentCaptor.forClass(Meta.class);
        Mockito.verify(metaRepository).save(metaCaptor.capture());
        Meta savedMeta = metaCaptor.getValue();

        // Print the captured Meta object for debugging
        System.out.println("Saved Meta: " + savedMeta);

        // Assertions to verify the saved values
        assertEquals("1.0", savedMeta.getDataVersion(), "Expected data version 1.0");
        assertEquals(LocalDate.of(2023, 9, 15), savedMeta.getCreated(), "Expected created date to be 2023-09-15");
        assertEquals(1, savedMeta.getRevision(), "Expected revision to be 1");
    }



    @Test
    public void testInsertMatchOfficials() throws IOException {
        // Sample JSON for testing officials insertion
        String jsonContent = "{ \"meta\": { \"data_version\": \"1.0\", \"created\": \"2023-09-15\", \"revision\": 1 }, \"info\": { \"event\": { \"match_number\": 100 }, \"city\": \"City A\", \"dates\": [\"2023-09-15\"], \"venue\": \"Venue A\", \"season\": \"Season 2023\", \"match_type\": \"Test\", \"gender\": \"male\", \"overs\": 50, \"event\": { \"name\": \"Event A\" }, \"player_of_match\": [\"Player 1\"], \"outcome\": { \"winner\": \"Team A\", \"by\": { \"runs\": 100 } }, \"teams\": [\"Team A\", \"Team B\"], \"officials\": { \"umpire\": [\"Official A\", \"Official B\"], \"referee\": [\"Official C\"] } } }";

        // Call the method to test
        matchInsertService.insertMatchData(jsonContent);

        // Verify that the officialRepository's save method was called for each official
        Mockito.verify(officialRepository, Mockito.times(3)).save(any(Official.class));  // Expecting 3 officials (2 umpires, 1 referee)
    }

    @Test
    public void testInsertMatchInfo() throws IOException {
        // Sample JSON data for testing match info insertion
        String jsonContent = "{ \"meta\": { \"data_version\": \"1.0\", \"created\": \"2023-09-15\", \"revision\": 1 }, \"info\": { \"event\": { \"match_number\": 0 }, \"city\": \"City B\", \"dates\": [\"2023-09-16\"], \"venue\": \"Venue B\", \"season\": \"Season 2023\", \"match_type\": \"ODI\", \"gender\": \"female\", \"overs\": 50, \"event\": { \"name\": \"Event B\" }, \"player_of_match\": [\"Player 2\"], \"outcome\": { \"winner\": \"Team B\", \"by\": { \"runs\": 150 } }, \"teams\": [\"Team A\", \"Team B\"] } }";

        // Call the method to test
        matchInsertService.insertMatchData(jsonContent);

        // Capture the MatchInfo object passed to the save method
        ArgumentCaptor<MatchInfo> matchInfoCaptor = ArgumentCaptor.forClass(MatchInfo.class);
        Mockito.verify(matchInfoRepository).save(matchInfoCaptor.capture());
        MatchInfo savedMatchInfo = matchInfoCaptor.getValue();

        // Print the captured MatchInfo object for debugging
        System.out.println("Saved MatchInfo: " + savedMatchInfo);

        // Assertions to verify the saved values
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

//    @Test
//    public void testInsertInningsAndOvers() throws IOException {
//        // Sample JSON for testing innings and overs insertion
//        String jsonContent = "{ \"meta\": { \"data_version\": \"1.0\", \"created\": \"2023-09-15\", \"revision\": 1 }, \"info\": { \"event\": { \"match_number\": 100 }, \"city\": \"City A\", \"dates\": [\"2023-09-15\"], \"venue\": \"Venue A\", \"season\": \"Season 2023\", \"match_type\": \"Test\", \"gender\": \"male\", \"overs\": 50, \"event\": { \"name\": \"Event A\" }, \"player_of_match\": [\"Player 1\"], \"outcome\": { \"winner\": \"Team A\", \"by\": { \"runs\": 100 } }, \"teams\": [\"Team A\", \"Team B\"], \"innings\": [{ \"team\": \"Team A\", \"overs\": [{ \"over\": 1, \"deliveries\": [{ \"batter\": \"Player 1\", \"bowler\": \"Player 2\", \"runs\": { \"batter\": 4, \"total\": 4 } }] }] }] } }";
//
//        // Call the method to test
//        matchInsertService.insertMatchData(jsonContent);
//
//        // Verify that inningRepository's save method was called once
//        Mockito.verify(inningRepository, Mockito.times(1)).save(any(Inning.class));  // Expecting 1 inning
//
//        // Verify that overRepository's save method was called for each over
//        Mockito.verify(overRepository, Mockito.times(1)).save(any(Overs.class));  // Expecting 1 over
//    }

}

