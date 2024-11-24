package com.bilyoner.livebetting.model.response;

import com.bilyoner.livebetting.model.dto.OddsHistoryDto;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;

@Data
public class MatchOddsHistoryResponse {

    private Long matchId;
    private String homeTeam;
    private String awayTeam;

    private BigDecimal currentHomeWinOdds;
    private BigDecimal currentDrawOdds;
    private BigDecimal currentAwayWinOdds;

    private Page<OddsHistoryDto> oddsHistory = Page.empty();
}