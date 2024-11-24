package com.bilyoner.livebetting.exception;

import org.springframework.http.HttpStatus;

public class CouponInterruptedException extends BaseException{
    public CouponInterruptedException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
