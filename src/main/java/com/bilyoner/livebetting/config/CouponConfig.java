package com.bilyoner.livebetting.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "coupon")
public class CouponConfig {

    private int timeout;
    private int couponCount;

}
