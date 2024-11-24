package com.bilyoner.livebetting.service;

import com.bilyoner.livebetting.entity.Match;
import com.bilyoner.livebetting.entity.OddsHistory;
import com.bilyoner.livebetting.repository.MatchRepository;
import com.bilyoner.livebetting.repository.OddsHistoryRepository;
import com.bilyoner.livebetting.service.impl.MatchOddsUpdater;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MatchOddsUpdaterTest {

    private MatchOddsUpdater matchOddsUpdater;

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private OddsHistoryRepository oddsHistoryRepository;

    private Match match;

    @BeforeEach
    void setUp() {
        matchOddsUpdater = new MatchOddsUpdater(matchRepository, oddsHistoryRepository);

        match = getMatch();
        when(matchRepository.findAll()).thenReturn(List.of(match));
        lenient().when(oddsHistoryRepository.save(any(OddsHistory.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ScheduledAnnotationBeanPostProcessor scheduledProcessor = new ScheduledAnnotationBeanPostProcessor();
        scheduledProcessor.postProcessBeforeInitialization(matchOddsUpdater, "matchOddsUpdater");
    }

    @Test
    void shouldUpdateMatchOdds() {
        for (int i = 0; i < 10000; i++) {
            matchOddsUpdater.updateMatchOdds();

            assertThat(match.getCurrentDrawOdds()).isGreaterThanOrEqualTo(BigDecimal.valueOf(1.01));
            assertThat(match.getCurrentAwayWinOdds()).isGreaterThanOrEqualTo(BigDecimal.valueOf(1.01));
            assertThat(match.getCurrentHomeWinOdds()).isGreaterThanOrEqualTo(BigDecimal.valueOf(1.01));
        }
    }


    @Test
    void shouldSaveOddsHistory() {
        matchOddsUpdater.updateMatchOdds();

        verify(oddsHistoryRepository, times(1)).save(any(OddsHistory.class));
    }

    @Test
    void shouldHandleEmptyMatchList() {

        when(matchRepository.findAll()).thenReturn(Collections.emptyList());

        matchOddsUpdater.updateMatchOdds();

        verify(oddsHistoryRepository, times(0)).save(any(OddsHistory.class));
    }

    @Test
    void shouldNotThrowException() {
        assertThatCode(() -> matchOddsUpdater.updateMatchOdds())
                .doesNotThrowAnyException();
    }

    private Match getMatch(){
        match = new Match();
        match.setCurrentHomeWinOdds(BigDecimal.valueOf(1.5));
        match.setCurrentDrawOdds(BigDecimal.valueOf(3.0));
        match.setCurrentAwayWinOdds(BigDecimal.valueOf(4.5));
        return match;
    }
}