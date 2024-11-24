package com.bilyoner.livebetting.controller;

import com.bilyoner.livebetting.config.BetApiV1;
import com.bilyoner.livebetting.model.dto.MatchDto;
import com.bilyoner.livebetting.model.response.MatchOddsHistoryResponse;
import com.bilyoner.livebetting.model.response.MatchResponse;
import com.bilyoner.livebetting.service.MatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(BetApiV1.BASE_PATH_MATCH)
public class MatchController {

    private final MatchService matchService;

    @PostMapping
    public ResponseEntity<MatchDto> addMatch(@Valid @RequestBody MatchDto matchDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(matchService.addMatch(matchDto));
    }

    @GetMapping
    public ResponseEntity<Page<MatchResponse>> getAllMatches(@RequestParam(name = "page", defaultValue = "0") int page,
                                                             @RequestParam(value = "pageSize", defaultValue = "0") int pageSize) {
        return ResponseEntity.status(HttpStatus.OK).body(matchService.getMatches(PageRequest.of(page, pageSize)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatch(@PathVariable("id") Long id) {
        matchService.deleteMatch(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/{matchId}/odds-history")
    public ResponseEntity<MatchOddsHistoryResponse> getMatchOddsHistory(@PathVariable("matchId") Long matchId,
                                                                        @RequestParam(name = "page", defaultValue = "0") int page,
                                                                        @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
        return ResponseEntity.status(HttpStatus.OK).body(matchService.getMatchOddsHistory(matchId, PageRequest.of(page, pageSize)));
    }

}
