package com.bilyoner.livebetting.model.response;

import com.bilyoner.livebetting.model.enums.SelectedBet;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class SelectedBetsResponse {
    @Schema(description = "Match ID", example = "123")
    private Long matchId;

    @Schema(description = "Selected bet type", example = "HOME")
    private SelectedBet betType;

    private BigDecimal odds;
}
