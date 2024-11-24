package com.bilyoner.livebetting.service;

import com.bilyoner.livebetting.model.dto.MatchDto;
import com.bilyoner.livebetting.model.response.MatchOddsHistoryResponse;
import com.bilyoner.livebetting.model.response.MatchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MatchService {

    MatchDto addMatch(MatchDto matchDto);
    Page<MatchResponse> getMatches(Pageable pageable);
    void deleteMatch(Long matchId);
    MatchOddsHistoryResponse getMatchOddsHistory(Long matchId,Pageable pageable);
}
