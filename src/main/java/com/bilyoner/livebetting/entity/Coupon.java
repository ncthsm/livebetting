package com.bilyoner.livebetting.entity;

import com.bilyoner.livebetting.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class Coupon extends BaseEntity {

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<CouponMatch> couponMatches = new ArrayList<>();

    private BigDecimal price;

}
