package com.bilyoner.livebetting.mapper;

import com.bilyoner.livebetting.entity.OddsHistory;
import com.bilyoner.livebetting.model.dto.OddsHistoryDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface OddHistoryMapper {

    @Named("to")
    OddsHistoryDto to(OddsHistory oddsHistory);

}
