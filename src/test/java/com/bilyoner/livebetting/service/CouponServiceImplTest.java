package com.bilyoner.livebetting.service;

import com.bilyoner.livebetting.config.CouponConfig;
import com.bilyoner.livebetting.entity.Coupon;
import com.bilyoner.livebetting.entity.CouponMatch;
import com.bilyoner.livebetting.entity.Match;
import com.bilyoner.livebetting.exception.CouponCountException;
import com.bilyoner.livebetting.exception.NotFoundException;
import com.bilyoner.livebetting.model.dto.CouponDto;
import com.bilyoner.livebetting.model.dto.SelectedBetsDto;
import com.bilyoner.livebetting.model.enums.SelectedBet;
import com.bilyoner.livebetting.model.response.CouponResponse;
import com.bilyoner.livebetting.repository.CouponRepository;
import com.bilyoner.livebetting.repository.MatchRepository;
import com.bilyoner.livebetting.service.impl.CouponServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CouponServiceImplTest {

    private CouponServiceImpl couponService;

    @Mock
    private CouponRepository couponRepository;
    
    @Mock
    private MatchRepository matchRepository;

    @Mock
    private CouponConfig couponConfig;

    private CouponDto couponDto;
    private SelectedBetsDto selectedBetsDto;
    private Match match;
    private Coupon coupon;

    @BeforeEach
    void setUp() {
        couponService = new CouponServiceImpl(couponRepository, matchRepository, couponConfig);
        getCouponDto();
        getMatch();
        getCoupon();
    }

    @Test
    void shouldReturnCouponResponsePage() {
        Page<Coupon> couponPage = new PageImpl<>(List.of(coupon));
        when(couponRepository.findAll(any(Pageable.class))).thenReturn(couponPage);

        Page<CouponResponse> result = couponService.getAllCoupon(Pageable.unpaged());

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getCouponId()).isEqualTo("1");
    }

    @Test
    void shouldDeleteCouponById() {
        couponService.deleteCoupon(1L);
        verify(couponRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldReturnCouponResponse() throws Exception {
        couponDto.setPrice(BigDecimal.valueOf(100));
        when(matchRepository.findByIdWithLock(anyLong())).thenReturn(Optional.of(match));
        when(couponConfig.getCouponCount()).thenReturn(500);
        when(couponRepository.save(any(Coupon.class))).thenReturn(coupon);

        CompletableFuture<CouponResponse> result = couponService.addCoupon(couponDto);

        assertThat(result).isNotNull();
        assertThat(result.get()).isNotNull();
        assertThat(result.get().getCouponId()).isEqualTo("1");
    }

    @Test
    void shouldThrowCouponCountException_WhenCouponCountExceeded() {
        match.setCouponCount(500);
        when(matchRepository.findByIdWithLock(anyLong())).thenReturn(Optional.of(match));

        couponDto.setPrice(BigDecimal.valueOf(100));
        when(couponConfig.getCouponCount()).thenReturn(500);

        assertThatThrownBy(() -> couponService.addCoupon(couponDto))
                .isInstanceOf(CouponCountException.class)
                .hasMessageContaining("Coupon count cannot exceed 500 for this match.");
    }

    @Test
    void shouldThrowNotFoundException_WhenMatchNotFound() {
        when(matchRepository.findByIdWithLock(anyLong())).thenReturn(Optional.empty());

        couponDto.setPrice(BigDecimal.valueOf(100));

        assertThatThrownBy(() -> couponService.addCoupon(couponDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Match not found with Id");
    }


    @Test
    void shouldCreateCouponMatch_WhenMatchIsValid() {
        when(matchRepository.findByIdWithLock(anyLong())).thenReturn(Optional.of(match));
        when(couponConfig.getCouponCount()).thenReturn(500);

        CouponMatch couponMatch = couponService.createCouponMatch(selectedBetsDto);

        assertThat(couponMatch).isNotNull();
        assertThat(couponMatch.getMatch()).isEqualTo(match);
        assertThat(couponMatch.getSelectedOdds()).isEqualTo(match.getCurrentHomeWinOdds());
    }

    private CouponDto getCouponDto(){
        couponDto = new CouponDto();
        selectedBetsDto = new SelectedBetsDto();
        selectedBetsDto.setMatchId(1L);
        selectedBetsDto.setBetType(SelectedBet.HOME);
        couponDto.setSelectedBets(Set.of(selectedBetsDto));
        return couponDto;
    }

    private Match getMatch(){
        match = new Match();
        match.setCouponCount(0);
        match.setCurrentHomeWinOdds(BigDecimal.valueOf(1.5));
        return match;
    }

    private Coupon getCoupon(){
        coupon = new Coupon();
        coupon.setId(1L);
        coupon.setPrice(BigDecimal.valueOf(100));
        return coupon;
    }


}