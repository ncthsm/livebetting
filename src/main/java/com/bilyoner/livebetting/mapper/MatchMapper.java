package com.bilyoner.livebetting.mapper;


import com.bilyoner.livebetting.entity.Match;
import com.bilyoner.livebetting.model.dto.MatchDto;
import com.bilyoner.livebetting.model.response.MatchOddsHistoryResponse;
import com.bilyoner.livebetting.model.response.MatchResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MatchMapper {

    @Named("from")
    @Mapping(target = "currentHomeWinOdds",source = "homeWinOdds")
    @Mapping(target = "currentAwayWinOdds",source = "awayWinOdds")
    @Mapping(target = "currentDrawOdds",source = "drawOdds")
    Match from(MatchDto matchDto);

    @Named("to")
    @Mapping(source = "currentHomeWinOdds",target = "homeWinOdds")
    @Mapping(source = "currentAwayWinOdds",target = "awayWinOdds")
    @Mapping(source = "currentDrawOdds",target = "drawOdds")
    MatchResponse to(Match match);

    @Named("toHistory")
    @Mapping(target = "matchId",source = "id")
    @Mapping(target = "oddsHistory", ignore = true)
    MatchOddsHistoryResponse toHistory(Match match);

}
