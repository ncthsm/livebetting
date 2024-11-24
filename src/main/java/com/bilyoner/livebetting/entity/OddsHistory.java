package com.bilyoner.livebetting.entity;

import com.bilyoner.livebetting.entity.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class OddsHistory extends BaseEntity {

    private BigDecimal homeWinOdds;
    private BigDecimal drawOdds;
    private BigDecimal awayWinOdds;
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "match_id")
    private Match match;

}
