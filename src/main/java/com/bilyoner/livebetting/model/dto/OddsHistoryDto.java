package com.bilyoner.livebetting.model.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OddsHistoryDto {
    private BigDecimal homeWinOdds;
    private BigDecimal drawOdds;
    private BigDecimal awayWinOdds;
    private LocalDateTime timestamp;
}