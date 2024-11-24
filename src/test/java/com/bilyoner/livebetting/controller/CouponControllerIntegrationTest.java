package com.bilyoner.livebetting.controller;

import com.bilyoner.livebetting.config.BetApiV1;
import com.bilyoner.livebetting.config.CouponConfig;
import com.bilyoner.livebetting.entity.Coupon;
import com.bilyoner.livebetting.entity.Match;
import com.bilyoner.livebetting.model.dto.CouponDto;
import com.bilyoner.livebetting.model.dto.SelectedBetsDto;
import com.bilyoner.livebetting.model.enums.SelectedBet;
import com.bilyoner.livebetting.repository.CouponRepository;
import com.bilyoner.livebetting.repository.MatchRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class CouponControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CouponConfig couponConfig;

    private static final BigDecimal PRICE = BigDecimal.valueOf(50);

    @Test
    void shouldCreateCoupon() throws Exception {
        matchRepository.deleteAll();
        matchRepository.save(getMatch());

        String jsonRequest = objectMapper.writeValueAsString(getCouponDto());

        mockMvc.perform(post(BetApiV1.BASE_PATH_COUPON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest));


    }

    @Test
    void shouldThrowCouponCountException_WhenCouponCountExceedsLimit() throws Exception {
        Match match = getMatch();
        match.setCouponCount(500);

        matchRepository.save(match);

        String jsonRequest = objectMapper.writeValueAsString(getCouponDto());

        mockMvc.perform(post(BetApiV1.BASE_PATH_COUPON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$.message").value(containsString("Coupon count cannot exceed 500 for this match.")));
    }

    @Test
    void shouldDeleteCouponById() throws Exception {
        Coupon coupon = new Coupon();
        coupon.setPrice(BigDecimal.valueOf(50));
        Coupon savedCoupon = couponRepository.save(coupon);

        mockMvc.perform(delete(BetApiV1.BASE_PATH_COUPON)
                        .param("id", savedCoupon.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Optional<Coupon> deletedCoupon = couponRepository.findById(savedCoupon.getId());
        assertThat(deletedCoupon).isEmpty();
    }

    @Test
    void getAllCoupons_ShouldReturnCouponsPage() throws Exception {
        Coupon coupon1 = new Coupon();
        coupon1.setPrice(BigDecimal.valueOf(50));
        couponRepository.save(coupon1);

        Coupon coupon2 = new Coupon();
        coupon2.setPrice(BigDecimal.valueOf(100));
        couponRepository.save(coupon2);

        mockMvc.perform(get(BetApiV1.BASE_PATH_COUPON)
                        .param("page", "0")
                        .param("pageSize", "2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].price").value(50));
    }




    private CouponDto getCouponDto() {
        CouponDto couponDto = new CouponDto();
        couponDto.setPrice(PRICE);

        SelectedBetsDto selectedBetsDto = new SelectedBetsDto();
        selectedBetsDto.setBetType(SelectedBet.HOME);
        selectedBetsDto.setMatchId(1L);
        couponDto.setSelectedBets(Set.of(selectedBetsDto));
        return couponDto;
    }

    private Match getMatch(){
        Match match = new Match();
        match.setId(1L);
        match.setCurrentHomeWinOdds(BigDecimal.valueOf(1.5));
        match.setCurrentDrawOdds(BigDecimal.valueOf(3.0));
        match.setCurrentAwayWinOdds(BigDecimal.valueOf(4.5));
        return match;
    }
}