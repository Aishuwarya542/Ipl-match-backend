package com.indium.ipl_match;

import com.indium.ipl_match.entity.MatchInfo;
import com.indium.ipl_match.repository.MatchInfoRepository;
import com.indium.ipl_match.repository.PlayerRepository;
import com.indium.ipl_match.service.MatchQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MatchQueryServiceTest {

    @InjectMocks
    private MatchQueryService matchQueryService;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private MatchInfoRepository matchInfoRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test case 1: Test getMatchesByPlayerName with valid player
    @Test
    public void testGetMatchesByPlayerNameValid() {
        String playerName = "Player A";
        List<MatchInfo> mockMatches = new ArrayList<>();
        mockMatches.add(new MatchInfo());
        when(playerRepository.findMatchesByPlayerName(playerName)).thenReturn(mockMatches);

        List<MatchInfo> result = matchQueryService.getMatchesByPlayerName(playerName);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    // Test case 2: Test getMatchesByPlayerName with no matches
    @Test
    public void testGetMatchesByPlayerNameNoMatches() {
        String playerName = "Player B";
        when(playerRepository.findMatchesByPlayerName(playerName)).thenReturn(Collections.emptyList());

        List<MatchInfo> result = matchQueryService.getMatchesByPlayerName(playerName);
        assertTrue(result.isEmpty());
    }

    // Test case 3: Test getCumulativeScoreByPlayerName with valid player
    @Test
    public void testGetCumulativeScoreByPlayerNameValid() {
        String playerName = "Player A";
        when(playerRepository.findCumulativeScoreByPlayerName(playerName)).thenReturn(150);

        Integer result = matchQueryService.getCumulativeScoreByPlayerName(playerName);
        assertNotNull(result);
        assertEquals(150, result);
    }

    // Test case 4: Test getCumulativeScoreByPlayerName with no score
    @Test
    public void testGetCumulativeScoreByPlayerNameNoScore() {
        String playerName = "Player B";
        when(playerRepository.findCumulativeScoreByPlayerName(playerName)).thenReturn(null);

        Integer result = matchQueryService.getCumulativeScoreByPlayerName(playerName);
        assertNotNull(result);
        assertEquals(0, result);
    }

    // Test case 5: Test getMatchScoresByDate
    @Test
    public void testGetMatchScoresByDate() {
        LocalDate matchDate = LocalDate.of(2023, 9, 15);
        Object[] matchData = new Object[]{1, 250};  // Ensure explicit Object array
        List<Object[]> mockResults = new ArrayList<>();  // Create a new ArrayList
        mockResults.add(matchData);  // Add the matchData to the list

        when(matchInfoRepository.findMatchScoresByDate(matchDate)).thenReturn(mockResults);

        List<Map<String, Object>> result = matchQueryService.getMatchScoresByDate(matchDate);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).get("matchNumber"));
        assertEquals(250, result.get(0).get("totalScore"));
    }

    // Test case 6: Test getTopBatsmenWithNameAndId with valid number of players
    @Test
    public void testGetTopBatsmenWithNameAndIdValid() {
        int numberOfPlayers = 3;
        Object[] playerData1 = {1, "Player A"};
        Object[] playerData2 = {2, "Player B"};
        List<Object[]> mockTopBatsmen = Arrays.asList(playerData1, playerData2); // Arrays.asList instead of List.of

        PageRequest pageRequest = PageRequest.of(0, numberOfPlayers);
        PageImpl<Object[]> mockPage = new PageImpl<>(mockTopBatsmen, pageRequest, mockTopBatsmen.size());

        when(playerRepository.findTopBatsmenWithNameAndId(pageRequest)).thenReturn(mockPage);

        List<Object[]> result = matchQueryService.getTopBatsmenWithNameAndId(numberOfPlayers);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0)[0]); // Check player ID
        assertEquals("Player A", result.get(0)[1]); // Check player name
    }


    // Test case 7: Test getTopBatsmenWithNameAndId when no players are found
    @Test
    public void testGetTopBatsmenWithNameAndIdNoPlayers() {
        int numberOfPlayers = 3;
        PageRequest pageRequest = PageRequest.of(0, numberOfPlayers);
        when(playerRepository.findTopBatsmenWithNameAndId(pageRequest)).thenReturn(Page.empty());

        List<Object[]> result = matchQueryService.getTopBatsmenWithNameAndId(numberOfPlayers);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // Test case 8: Test getMatchScoresByDate when no matches are found
    @Test
    public void testGetMatchScoresByDateNoMatches() {
        LocalDate matchDate = LocalDate.of(2023, 9, 15);
        when(matchInfoRepository.findMatchScoresByDate(matchDate)).thenReturn(Collections.emptyList());

        List<Map<String, Object>> result = matchQueryService.getMatchScoresByDate(matchDate);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
