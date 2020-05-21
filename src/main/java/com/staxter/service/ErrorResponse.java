package com.staxter.service;

public class ErrorResponse {
    private final String code;
    private final String description;

    public ErrorResponse() {
        code = "USER_ALREADY_EXISTS";
        description = "A user with the given username already exists";
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
