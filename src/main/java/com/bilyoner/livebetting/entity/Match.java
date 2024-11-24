package com.bilyoner.livebetting.entity;

import com.bilyoner.livebetting.entity.base.BaseEntity;
import com.bilyoner.livebetting.model.enums.League;
import com.bilyoner.livebetting.model.enums.SelectedBet;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Match  extends BaseEntity {

    @Enumerated(EnumType.STRING)
    private League league;

    private String homeTeam;
    private String awayTeam;

    private BigDecimal currentHomeWinOdds;
    private BigDecimal currentDrawOdds;
    private BigDecimal currentAwayWinOdds;
    private LocalDateTime startTime;

    private int couponCount = 0;
    private String description;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<OddsHistory> oddsHistory;

    public BigDecimal getSelectedOdds(SelectedBet selectedBet) {
        return switch (selectedBet) {
            case AWAY -> getCurrentAwayWinOdds();
            case DRAW -> getCurrentDrawOdds();
            case HOME -> getCurrentHomeWinOdds();
        };
    }

}
