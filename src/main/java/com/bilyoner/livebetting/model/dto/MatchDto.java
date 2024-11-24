package com.bilyoner.livebetting.model.dto;

import com.bilyoner.livebetting.model.enums.League;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MatchDto {

    @NotNull
    @Enumerated(EnumType.STRING)
    private League league;

    @NotEmpty
    private String homeTeam;
    @NotEmpty
    private String awayTeam;

    @NotNull
    @DecimalMin(value = "1.01", message = "odds must be greater than 1.00")
    private BigDecimal homeWinOdds;

    @NotNull
    @DecimalMin(value = "1.01", message = "odds must be greater than 1.00")
    private BigDecimal drawOdds;

    @NotNull
    @DecimalMin(value = "1.01", message = "odds must be greater than 01")
    private BigDecimal awayWinOdds;
    @NotNull
    private LocalDateTime startTime;

    private String description;

    private Long id;

}
