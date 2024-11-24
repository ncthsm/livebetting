package com.bilyoner.livebetting.entity;

import com.bilyoner.livebetting.entity.base.BaseEntity;
import com.bilyoner.livebetting.model.enums.SelectedBet;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class CouponMatch extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "match_id")
    private Match match;

    @Enumerated(EnumType.STRING)
    private SelectedBet selectedBet;

    private BigDecimal selectedOdds;

}
