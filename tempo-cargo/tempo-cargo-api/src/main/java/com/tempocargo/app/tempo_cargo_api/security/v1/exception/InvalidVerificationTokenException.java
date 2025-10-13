package com.tempocargo.app.tempo_cargo_api.common.v1.exception;

public class InvalidVerificationTokenException extends RuntimeException {

    public InvalidVerificationTokenException(String message) {
        super(message);
    }
}
