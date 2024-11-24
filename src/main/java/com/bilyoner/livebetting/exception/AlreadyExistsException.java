package com.bilyoner.livebetting.exception;

import org.springframework.http.HttpStatus;

public class AlreadyExistsException extends BaseException {
    public AlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}