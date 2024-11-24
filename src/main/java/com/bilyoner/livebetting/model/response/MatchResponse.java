package com.bilyoner.livebetting.model.response;

import com.bilyoner.livebetting.model.dto.MatchDto;
import lombok.Data;

@Data
public class MatchResponse extends MatchDto {

    private Long id;
    private int couponCount;
}
