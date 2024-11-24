package com.bilyoner.livebetting.service;

import com.bilyoner.livebetting.model.dto.CouponDto;
import com.bilyoner.livebetting.model.response.CouponResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.concurrent.CompletableFuture;

public interface CouponService {
    CompletableFuture<CouponResponse> addCoupon(CouponDto couponDto);
    Page<CouponResponse> getAllCoupon(Pageable pageable);
    void deleteCoupon(Long id);
}
