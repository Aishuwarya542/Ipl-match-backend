package com.indium.ipl_match;

import com.indium.ipl_match.repository.PlayerRepository;
import com.indium.ipl_match.service.MatchInsertService;
import com.indium.ipl_match.service.MatchQueryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class MatchControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MatchInsertService matchInsertService;

    @MockBean
    private PlayerRepository playerRepository;

    @MockBean
    private MatchQueryService matchQueryService;


    @BeforeEach
    public void setUp() {
        // If needed, mock other repository methods or service methods here
        // For example, if you want to mock findByName in playerRepository:
        Mockito.when(playerRepository.findByPlayerName(Mockito.anyString())).thenReturn(Optional.empty());

    }

    @Test
    public void testUploadJsonDataValidContent() throws Exception {
        // Mock the service call
        Mockito.doNothing().when(matchInsertService).insertMatchData(Mockito.anyString());

        String validJsonContent = "{ \"meta\": { \"data_version\": \"1.0\", \"created\": \"2023-09-15\", \"revision\": 1 }, \"data\": [] }";

        // Send a valid JSON payload
        mockMvc.perform(post("/api/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJsonContent)) // Sending valid JSON content
                .andExpect(status().isOk()) // Expecting 200 OK
                .andExpect(content().string("Match data uploaded successfully!")); // Validating the response message
    }

    @Test
    public void testUploadJsonDataInvalidContent() throws Exception {
        // Sending invalid JSON content
        String invalidJsonContent = "{ invalid_json: [ ]";  // Missing proper JSON structure

        mockMvc.perform(post("/api/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJsonContent)) // Sending invalid JSON content
                .andExpect(status().isBadRequest()); // Expecting 400 Bad Request
    }

    @Test
    public void testUploadEmptyJsonData() throws Exception {
        // Sending an empty JSON content
        String emptyJsonContent = "";  // Empty JSON

        mockMvc.perform(post("/api/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emptyJsonContent)) // Sending empty content
                .andExpect(status().isBadRequest()) // Expecting 400 Bad Request
                .andExpect(content().string("")); // Expecting an empty body
    }
}

