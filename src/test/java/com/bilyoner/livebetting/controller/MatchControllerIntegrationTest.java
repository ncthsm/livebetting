package com.bilyoner.livebetting.controller;

import com.bilyoner.livebetting.model.dto.MatchDto;
import com.bilyoner.livebetting.model.enums.League;
import com.bilyoner.livebetting.model.response.MatchOddsHistoryResponse;
import com.bilyoner.livebetting.model.response.MatchResponse;
import com.bilyoner.livebetting.repository.MatchRepository;
import com.bilyoner.livebetting.service.MatchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import org.springframework.transaction.annotation.Transactional;

import static com.bilyoner.livebetting.config.BetApiV1.BASE_PATH_MATCH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MatchControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MatchService matchService;

    private MatchDto matchDto;
    @Autowired
    private MatchRepository matchRepository;

    @BeforeEach
    void setUp() {
        matchDto = new MatchDto();
        matchDto.setHomeTeam("Team A");
        matchDto.setAwayTeam("Team B");
        matchDto.setStartTime(LocalDateTime.now());
        matchDto.setLeague(League.BUNDESLIGA);
        matchDto.setDrawOdds(BigDecimal.TEN);
        matchDto.setHomeWinOdds(BigDecimal.TEN);
        matchDto.setAwayWinOdds(BigDecimal.TEN);
    }

    @Test
    void addMatch_ShouldReturnCreatedMatch() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(matchDto);

        MvcResult result = mockMvc.perform(post(BASE_PATH_MATCH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        MatchDto returnedMatch = objectMapper.readValue(jsonResponse, MatchDto.class);

        assertThat(returnedMatch.getHomeTeam()).isEqualTo(matchDto.getHomeTeam());
        assertThat(returnedMatch.getAwayTeam()).isEqualTo(matchDto.getAwayTeam());
    }

    @Test
    void getAllMatches_ShouldReturnPagedMatches() throws Exception {
        matchService.addMatch(matchDto);

        MvcResult result = mockMvc.perform(get(BASE_PATH_MATCH)
                        .param("page", "0")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        MatchResponse matchResponse = objectMapper.readValue(jsonResponse, MatchResponse.class);

        assertThat(matchResponse).isNotNull();
    }

    @Test
    void deleteMatch_ShouldDeleteTheMatch() throws Exception {
        MatchDto savedMatch = matchService.addMatch(matchDto);

        mockMvc.perform(delete(BASE_PATH_MATCH+ "/{id}", savedMatch.getId()))
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(get(BASE_PATH_MATCH)
                        .param("page", "0")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        MatchResponse matchResponse = objectMapper.readValue(jsonResponse, MatchResponse.class);
        assertThat(matchResponse.getId()).isNull();
    }

    @Test
    void getMatchOddsHistory_ShouldReturnPagedOddsHistory() throws Exception {
        MatchDto savedMatch = matchService.addMatch(matchDto);

        MvcResult result = mockMvc.perform(get(BASE_PATH_MATCH+"/{matchId}/odds-history", savedMatch.getId())
                        .param("page", "0")
                        .param("pageSize", "10"))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();

        assertThat(jsonResponse).isNotNull();
    }
}