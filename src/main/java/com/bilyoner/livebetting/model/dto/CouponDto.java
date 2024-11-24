package com.bilyoner.livebetting.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class CouponDto {
    @NotNull
    @Size(max = 20, message = "Selected bets cannot contain more than 20 items")
    @Schema(description = "List of match bets")
    private Set<SelectedBetsDto> selectedBets;

    @NotNull
    @DecimalMin(value = "20.00", message = "Price must be greater than 20")
    private BigDecimal price;
}
