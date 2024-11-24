package com.bilyoner.livebetting.exception;

import org.springframework.http.HttpStatus;

public class CouponCountException extends BaseException {
    public CouponCountException(String message) {
        super(message, HttpStatus.NOT_ACCEPTABLE);
    }

}
