package com.indium.ipl_match;

import com.indium.ipl_match.controller.MatchController;
import com.indium.ipl_match.service.MatchInsertService;
import com.indium.ipl_match.service.MatchQueryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.Collections;
import java.time.LocalDate;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(MatchController.class)
public class MatchControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MatchInsertService matchInsertService;

    @MockBean
    private MatchQueryService matchQueryService;

    @MockBean
    private KafkaTemplate<String, String> kafkaTemplate;

    // Test Case 1: Test for uploading match data successfully
    @Test
    public void testUploadMatchDataSuccess() throws Exception {
        String jsonContent = "{\"matchNumber\": 1}";

        doNothing().when(matchInsertService).insertMatchData(anyString());

        mockMvc.perform(post("/api/matches")
                        .contentType("application/json")
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(content().string("Match data uploaded successfully!"));

        verify(matchInsertService, times(1)).insertMatchData(anyString());
    }

    // Test Case 2: Test for error during match data upload
    @Test
    public void testUploadMatchDataFailure() throws Exception {
        String jsonContent = "{\"matchNumber\": 1}";

        doThrow(new IOException("Error in processing")).when(matchInsertService).insertMatchData(anyString());

        mockMvc.perform(post("/api/matches")
                        .contentType("application/json")
                        .content(jsonContent))
                .andExpect(status().is5xxServerError())
                .andExpect(content().string("Error occurred while processing the match data: Error in processing"));

        verify(matchInsertService, times(1)).insertMatchData(anyString());
    }

    // Test Case 3: Test for finding matches by player name
    @Test
    public void testFindMatchesByPlayerName() throws Exception {
        String playerName = "Virat Kohli";
        when(matchQueryService.getMatchesByPlayerName(playerName)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/matches/player/match/{playerName}", playerName))
                .andExpect(status().isOk())  // Expect HTTP 200
                .andExpect(content().json("[]"));  // Expect empty JSON array

        verify(matchQueryService, times(1)).getMatchesByPlayerName(playerName);
    }

    // Test Case 4: Test for retrieving cumulative score by player name
    @Test
    public void testGetCumulativeScore() throws Exception {
        String playerName = "Virat Kohli";
        when(matchQueryService.getCumulativeScoreByPlayerName(playerName)).thenReturn(120);

        mockMvc.perform(get("/api/matches/score").param("playerName", playerName))
                .andExpect(status().isOk())  // Expect HTTP 200
                .andExpect(content().string("120"));  // Expect score

        verify(matchQueryService, times(1)).getCumulativeScoreByPlayerName(playerName);
    }

    // Test Case 5: Test for getting match scores by date
    @Test
    public void testGetMatchScoresByDate() throws Exception {
        String date = "2024-09-13";

        mockMvc.perform(get("/api/matches/match/date").param("date", date))
                .andExpect(status().isNoContent())  // Expect HTTP 204 No Content
                .andExpect(content().string("No matches found on " + date));
    }

    // Test Case 6: Test for invalid date format
    @Test
    public void testGetMatchScoresByDateInvalidDate() throws Exception {
        String invalidDate = "invalid-date";

        mockMvc.perform(get("/api/matches/match/date").param("date", invalidDate))
                .andExpect(status().isBadRequest())  // Expect HTTP 400 for invalid date format
                .andExpect(content().string("Invalid date format for date: " + invalidDate));

        verify(matchQueryService, never()).getMatchScoresByDate(any(LocalDate.class));
    }

    // Test Case 7: Test for retrieving top batsmen
    @Test
    public void testGetTopBatsmen() throws Exception {
        int numberOfPlayers = 5;
        when(matchQueryService.getTopBatsmenWithNameAndId(numberOfPlayers)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/matches/top-batsmen").param("numberOfPlayers", String.valueOf(numberOfPlayers)))
                .andExpect(status().isOk())  // Expect HTTP 200
                .andExpect(content().json("[]"));  // Expect empty JSON array

        verify(matchQueryService, times(1)).getTopBatsmenWithNameAndId(numberOfPlayers);
    }
}
