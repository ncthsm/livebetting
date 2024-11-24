package com.bilyoner.livebetting.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private String message;
    private int status;
    private List<String> errors;

    public ErrorResponse(String message, int status) {
        this.message = message;
        this.status = status;
    }

}