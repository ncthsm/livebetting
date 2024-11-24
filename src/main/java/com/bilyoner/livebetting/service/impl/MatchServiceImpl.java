package com.bilyoner.livebetting.service.impl;

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
import com.bilyoner.livebetting.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final MatchMapper matchMapper;
    private final OddsHistoryRepository oddsHistoryRepository;
    private final OddHistoryMapper oddHistoryMapper;

    @Override
    public MatchDto addMatch(MatchDto matchDto) {
        Match match = matchRepository.save(matchMapper.from(matchDto));
        return matchMapper.to(match);
    }

    @Override
    public void deleteMatch(Long id) {
        matchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MatchResponse> getMatches(Pageable pageable) {
        Page<Match> matchPage = matchRepository.findAllByOrderByStartTimeAsc(pageable);
        return matchPage.map(matchMapper::to);
    }

    @Override
    @Transactional(readOnly = true)
    public MatchOddsHistoryResponse getMatchOddsHistory(Long matchId, Pageable pageable) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new NotFoundException("Match not found with id: " + matchId));

        MatchOddsHistoryResponse response = matchMapper.toHistory(match);

        Page<OddsHistory> oddsHistories = oddsHistoryRepository.findByMatchOrderByTimestampAsc(match,pageable);
        Page<OddsHistoryDto> oddsHistoryDtoPage = oddsHistories.map(oddHistoryMapper::to);

        response.setOddsHistory(oddsHistoryDtoPage);
        return response;
    }

}
