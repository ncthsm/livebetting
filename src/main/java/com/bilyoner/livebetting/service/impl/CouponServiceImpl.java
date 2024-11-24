package com.bilyoner.livebetting.service.impl;

import com.bilyoner.livebetting.config.CouponConfig;
import com.bilyoner.livebetting.entity.Coupon;
import com.bilyoner.livebetting.entity.CouponMatch;
import com.bilyoner.livebetting.entity.Match;
import com.bilyoner.livebetting.exception.CouponCountException;
import com.bilyoner.livebetting.exception.NotFoundException;
import com.bilyoner.livebetting.model.dto.CouponDto;
import com.bilyoner.livebetting.model.dto.SelectedBetsDto;
import com.bilyoner.livebetting.model.response.CouponResponse;
import com.bilyoner.livebetting.model.response.SelectedBetsResponse;
import com.bilyoner.livebetting.repository.CouponRepository;
import com.bilyoner.livebetting.repository.MatchRepository;
import com.bilyoner.livebetting.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;


@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final MatchRepository matchRepository;
    private final CouponConfig couponConfig;


    @Override
    public Page<CouponResponse> getAllCoupon(Pageable pageable) {
        Page<Coupon> couponPage = couponRepository.findAll(pageable);
        return couponPage.map(this::mapToCouponResponse);
    }

    @Override
    public void deleteCoupon(Long id) {
        couponRepository.deleteById(id);
    }

    @Async
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public CompletableFuture<CouponResponse>  addCoupon(CouponDto couponDto) {
        Coupon coupon = createCouponFromDto(couponDto);
        Coupon savedCoupon = couponRepository.save(coupon);
        return CompletableFuture.completedFuture(mapToCouponResponse(savedCoupon));
    }

    private Coupon createCouponFromDto(CouponDto couponDto) {
        Coupon coupon = new Coupon();
        coupon.setPrice(couponDto.getPrice());

        List<CouponMatch> couponMatches = couponDto.getSelectedBets().stream()
                .map(this::createCouponMatch)
                .toList();

        coupon.setCouponMatches(couponMatches);
        return coupon;
    }

    public CouponMatch createCouponMatch(SelectedBetsDto selectedBetsDto) {
        Match match = matchRepository.findByIdWithLock(selectedBetsDto.getMatchId())
                .orElseThrow(() -> new NotFoundException("Match not found with Id: " + selectedBetsDto.getMatchId()));

        if (match.getCouponCount() >= couponConfig.getCouponCount()) {
            throw new CouponCountException("Coupon count cannot exceed 500 for this match.");
        }

        BigDecimal odds = match.getSelectedOdds(selectedBetsDto.getBetType());

        match.setCouponCount(match.getCouponCount() + 1);
        matchRepository.save(match);
        return new CouponMatch(match, selectedBetsDto.getBetType(), odds);
    }


    private CouponResponse mapToCouponResponse(Coupon coupon){
        CouponResponse response = new CouponResponse();
        response.setPrice(coupon.getPrice());
        Set<SelectedBetsResponse> selectedBetsResponseSet = new HashSet<>();

        coupon.getCouponMatches().forEach(couponMatch->
            selectedBetsResponseSet.add(new SelectedBetsResponse(couponMatch.getMatch().getId(),couponMatch.getSelectedBet(),couponMatch.getSelectedOdds()))
        );

        response.setCouponId(coupon.getId().toString());
        response.setSelectedBet(selectedBetsResponseSet);
        return response;
    }
}
