package com.tempocargo.app.tempo_cargo_api.security.v1.exception;

public class ForgotPasswordRequestExpired extends RuntimeException {

    public ForgotPasswordRequestExpired(String message) {
        super(message);
    }
}
