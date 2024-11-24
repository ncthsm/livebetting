package com.bilyoner.livebetting.controller;

import com.bilyoner.livebetting.config.BetApiV1;
import com.bilyoner.livebetting.config.CouponConfig;
import com.bilyoner.livebetting.exception.AlreadyExistsException;
import com.bilyoner.livebetting.exception.CouponInterruptedException;
import com.bilyoner.livebetting.exception.CouponTimeOutException;
import com.bilyoner.livebetting.model.dto.CouponDto;
import com.bilyoner.livebetting.model.response.CouponResponse;
import com.bilyoner.livebetting.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


@RestController
@RequiredArgsConstructor
@RequestMapping(BetApiV1.BASE_PATH_COUPON)
public class CouponController {

    private final CouponService couponService;
    private final CouponConfig couponConfig;

    @PostMapping
    public ResponseEntity<CouponResponse> addCoupon(@Valid @RequestBody CouponDto couponDto) {
        CompletableFuture<CouponResponse> future =couponService.addCoupon(couponDto);
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(future.get(couponConfig.getTimeout(), TimeUnit.SECONDS));
        } catch (TimeoutException e) {
            throw new CouponTimeOutException("Coupon creation timed out.");
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new CouponInterruptedException("Error occurred while adding coupon. "+ e.getCause());
        }
    }

    @GetMapping
    public ResponseEntity<Page<CouponResponse>> getAllCoupons(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize) {
        return ResponseEntity.status(HttpStatus.OK).body(couponService.getAllCoupon(PageRequest.of(page, pageSize)));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteCoupon(@RequestParam("id") Long id) {
        couponService.deleteCoupon(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
