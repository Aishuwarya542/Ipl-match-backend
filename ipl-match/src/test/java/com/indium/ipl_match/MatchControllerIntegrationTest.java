package com.indium.ipl_match;

import com.indium.ipl_match.entity.MatchInfo;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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
        Mockito.when(playerRepository.findByPlayerName(Mockito.anyString())).thenReturn(Optional.empty());
    }

    @Test
    @WithMockUser(username = "aishu", roles = "USER")  // Mock an authenticated user with the "USER" role
    public void testUploadJsonDataValidContent() throws Exception {
        Mockito.doNothing().when(matchInsertService).insertMatchData(Mockito.anyString());
        String validJsonContent = "{ \"meta\": { \"data_version\": \"1.0\", \"created\": \"2023-09-15\", \"revision\": 1 }, \"data\": [] }";
        mockMvc.perform(post("/api/matches")
                        .with(csrf())  // Adding CSRF token
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJsonContent))
                .andExpect(status().isOk())
                .andExpect(content().string("Match data uploaded successfully!"));
    }

    @Test
    @WithMockUser(username = "aishu", roles = "USER")
    public void testUploadJsonDataInvalidContent() throws Exception {
        String invalidJsonContent = "{ invalid_json: [ ]";

        mockMvc.perform(post("/api/matches")
                        .with(csrf())  // Adding CSRF token
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJsonContent))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "aishu", roles = "USER")
    public void testUploadEmptyJsonData() throws Exception {
        String emptyJsonContent = "";

        mockMvc.perform(post("/api/matches")
                        .with(csrf())  // Adding CSRF token
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emptyJsonContent))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(""));
    }

}
