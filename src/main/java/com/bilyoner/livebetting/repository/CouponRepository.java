package com.bilyoner.livebetting.repository;

import com.bilyoner.livebetting.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
