package com.tempocargo.app.tempo_cargo_api.security.v1.exception;

public class UnregisteredEmailException extends RuntimeException {

    public UnregisteredEmailException(String message) {
        super(message);
    }
}
