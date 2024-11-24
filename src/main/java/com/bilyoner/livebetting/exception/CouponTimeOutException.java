package com.bilyoner.livebetting.exception;

import org.springframework.http.HttpStatus;

public class CouponTimeOutException extends BaseException{
    public CouponTimeOutException(String message) {
        super(message, HttpStatus.REQUEST_TIMEOUT);
    }

}
