package com.bilyoner.livebetting.service;

import com.bilyoner.livebetting.entity.Match;
import com.bilyoner.livebetting.entity.OddsHistory;
import com.bilyoner.livebetting.exception.NotFoundException;
import com.bilyoner.livebetting.mapper.MatchMapper;
import com.bilyoner.livebetting.mapper.OddHistoryMapper;
import com.bilyoner.livebetting.model.dto.MatchDto;
import com.bilyoner.livebetting.model.dto.OddsHistoryDto;
import com.bilyoner.livebetting.model.response.MatchOddsHistoryResponse;
import com.bilyoner.livebetting.model.response.MatchResponse;
import com.bilyoner.livebetting.repository.MatchRepository;
import com.bilyoner.livebetting.repository.OddsHistoryRepository;
import com.bilyoner.livebetting.service.impl.MatchServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchServiceImplTest {

    private MatchServiceImpl matchService;

    @Mock
    private MatchRepository matchRepository;
    @Mock
    private MatchMapper matchMapper;
    @Mock
    private OddsHistoryRepository oddsHistoryRepository;
    @Mock
    private OddHistoryMapper oddHistoryMapper;

    private static final String AWAY_TEAM = "TEST_AWAY_TEAM";
    private static final BigDecimal DRAW_ODDS = BigDecimal.valueOf(1.4);

    @BeforeEach
    void setUp() {
        matchService = new MatchServiceImpl(matchRepository, matchMapper, oddsHistoryRepository, oddHistoryMapper);
    }

    @Test
    void addMatch_ShouldSaveMatchAndReturnMatchDto() {
        var match = getMatch();
        var matchDto = getMatchDto();

        when(matchMapper.from(matchDto)).thenReturn(match);
        when(matchRepository.save(match)).thenReturn(match);
        when(matchMapper.to(match)).thenReturn(getMatchResponse());

        MatchDto result = matchService.addMatch(matchDto);

        verify(matchRepository, times(1)).save(match);
        assertThat(result).isNotNull();
        assertEquals(AWAY_TEAM, result.getAwayTeam());
    }

    @Test
    void deleteMatch_ShouldDeleteMatchById() {
        matchService.deleteMatch(1L);
        verify(matchRepository, times(1)).deleteById(1L);
    }

    @Test
    void getMatches_ShouldReturnPageOfMatchResponse() {
        var match = getMatch();
        Pageable pageable = Pageable.unpaged();
        Page<Match> matchPage = new PageImpl<>(Collections.singletonList(match));
        when(matchRepository.findAllByOrderByStartTimeAsc(pageable)).thenReturn(matchPage);
        when(matchMapper.to(match)).thenReturn(getMatchResponse());

        Page<MatchResponse> result = matchService.getMatches(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent().size()).isEqualTo(1);
        assertThat(result.getContent().get(0).getId()).isEqualTo(1L);
    }

    @Test
    void getMatchOddsHistory_ShouldThrowNotFoundException_WhenMatchNotFound() {
        Long matchId = 1L;
        Pageable pageable = Pageable.unpaged();

        when(matchRepository.findById(matchId)).thenReturn(Optional.empty());

        try {
            matchService.getMatchOddsHistory(matchId, pageable);
        } catch (NotFoundException e) {
            assertThat(e.getMessage()).isEqualTo("Match not found with id: " + matchId);
        }

        verify(matchRepository, times(1)).findById(matchId);
    }

    @Test
    void getMatchOddsHistory_ShouldReturnEmptyList_WhenNoOddsHistoryFound() {
        var match = getMatch();
        Pageable pageable = Pageable.unpaged();
        Page<OddsHistory> emptyOddsHistoryPage = new PageImpl<>(Collections.emptyList());

        when(matchMapper.toHistory(match)).thenReturn(getMatchOddsHistoryResponse());
        when(matchRepository.findById(1L)).thenReturn(Optional.of(match));
        when(oddsHistoryRepository.findByMatchOrderByTimestampAsc(match, pageable)).thenReturn(emptyOddsHistoryPage);

        var result = matchService.getMatchOddsHistory(1L, pageable);

        assertThat(result).isNotNull();
        assertEquals(0, result.getOddsHistory().getSize());
    }

    @Test
    void addMatch_ShouldThrowException_WhenInvalidMatchDto() {
        MatchDto invalidMatchDto = new MatchDto();

        try {
            matchService.addMatch(invalidMatchDto);
        } catch (Exception e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    private MatchDto getMatchDto() {
        MatchDto matchDto = new MatchDto();
        matchDto.setAwayTeam(AWAY_TEAM);
        return matchDto;
    }

    private MatchResponse getMatchResponse() {
        MatchResponse matchResponse = new MatchResponse();
        matchResponse.setId(1L);
        matchResponse.setAwayTeam(AWAY_TEAM);
        return matchResponse;
    }

    private Match getMatch() {
        Match match = new Match();
        match.setAwayTeam(AWAY_TEAM);
        match.setStartTime(LocalDateTime.now());
        return match;
    }




    private MatchOddsHistoryResponse getMatchOddsHistoryResponse() {
        MatchOddsHistoryResponse matchOddsHistoryResponse = new MatchOddsHistoryResponse();
        matchOddsHistoryResponse.setAwayTeam(AWAY_TEAM);
        matchOddsHistoryResponse.setCurrentDrawOdds(DRAW_ODDS);
        return matchOddsHistoryResponse;
    }

}