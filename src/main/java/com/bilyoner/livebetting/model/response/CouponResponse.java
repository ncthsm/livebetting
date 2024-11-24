package com.bilyoner.livebetting.model.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponResponse  {
    private String couponId;
    private Set<SelectedBetsResponse> selectedBet;
    private BigDecimal price;
}
