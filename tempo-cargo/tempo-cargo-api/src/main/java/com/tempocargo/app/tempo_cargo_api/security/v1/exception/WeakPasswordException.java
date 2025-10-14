package com.tempocargo.app.tempo_cargo_api.security.v1.exception;

public class WeakPasswordException extends RuntimeException {

    public WeakPasswordException(String message) {
        super(message);
    }
}
