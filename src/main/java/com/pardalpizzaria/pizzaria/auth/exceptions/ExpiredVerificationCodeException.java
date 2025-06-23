package com.pardalpizzaria.pizzaria.auth.exceptions;

public class ExpiredVerificationCodeException extends RuntimeException {
    public ExpiredVerificationCodeException(String message) {
        super(message);
    }
    public ExpiredVerificationCodeException(String message, Throwable cause) {
        super(message, cause);
    }

}
