package com.bilyoner.livebetting.service.impl;

import com.bilyoner.livebetting.entity.Match;
import com.bilyoner.livebetting.entity.OddsHistory;
import com.bilyoner.livebetting.repository.MatchRepository;
import com.bilyoner.livebetting.repository.OddsHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@EnableScheduling
@RequiredArgsConstructor
public class MatchOddsUpdater {

    private final MatchRepository matchRepository;
    private final OddsHistoryRepository oddsHistoryRepository;

    private static final BigDecimal PROFIT_RATE = BigDecimal.valueOf(1.2);


    @Scheduled(fixedRate = 1000)
    @Transactional
    public void updateMatchOdds() {
        List<Match> matches = matchRepository.findAll();
        matches.forEach(this::updateOddsForMatch);
    }

    private void updateOddsForMatch(Match match) {
        BigDecimal homeWinOdds = generateRandomOdds();
        BigDecimal drawOdds = generateRandomOdds();
        BigDecimal awayWinOdds = generateRandomOdds();

        BigDecimal totalInverseOdds = calculateMarginalTotalInverseOdds(homeWinOdds, drawOdds, awayWinOdds);

        log.info("totalInverseOdds: {}", totalInverseOdds);

        homeWinOdds = adjustOdds(homeWinOdds, totalInverseOdds);
        drawOdds = adjustOdds(drawOdds, totalInverseOdds);
        awayWinOdds = adjustOdds(awayWinOdds, totalInverseOdds);

        log.info("homeWinOdds: {} awayWinOdds: {} drawOdds {}",homeWinOdds,awayWinOdds,drawOdds);

        saveOddsHistory(match, match.getCurrentHomeWinOdds(), match.getCurrentDrawOdds(), match.getCurrentAwayWinOdds());

        match.setCurrentHomeWinOdds(homeWinOdds);
        match.setCurrentDrawOdds(drawOdds);
        match.setCurrentAwayWinOdds(awayWinOdds);

        matchRepository.save(match);
    }

    private BigDecimal calculateMarginalTotalInverseOdds(BigDecimal homeWinOdds, BigDecimal drawOdds, BigDecimal awayWinOdds) {
        BigDecimal totalInverseOdds = BigDecimal.ONE.divide(homeWinOdds, 10, RoundingMode.HALF_UP)
                .add(BigDecimal.ONE.divide(drawOdds, 10, RoundingMode.HALF_UP))
                .add(BigDecimal.ONE.divide(awayWinOdds, 10, RoundingMode.HALF_UP));
        return totalInverseOdds.multiply(PROFIT_RATE);
    }

    private BigDecimal adjustOdds(BigDecimal odds, BigDecimal marjliTotalInverseOdds) {
        return BigDecimal.ONE.divide(
                BigDecimal.ONE.divide(odds, 10, RoundingMode.HALF_UP)
                        .multiply(PROFIT_RATE)
                        .divide(marjliTotalInverseOdds, 10, RoundingMode.HALF_UP),
                2, RoundingMode.HALF_UP
        );
    }

    private void saveOddsHistory(Match match, BigDecimal homeWinOdds, BigDecimal drawOdds, BigDecimal awayWinOdds) {
        OddsHistory history = new OddsHistory();
        history.setMatch(match);
        history.setHomeWinOdds(homeWinOdds);
        history.setDrawOdds(drawOdds);
        history.setAwayWinOdds(awayWinOdds);
        history.setTimestamp(LocalDateTime.now());
        oddsHistoryRepository.save(history);
    }

    private BigDecimal generateRandomOdds() {
        BigDecimal odds =  BigDecimal.valueOf(1.01 + Math.random() * 38.99)
                .setScale(2, RoundingMode.HALF_UP);
        log.info("Generated Odds: {}",odds);
        return odds;

    }
}