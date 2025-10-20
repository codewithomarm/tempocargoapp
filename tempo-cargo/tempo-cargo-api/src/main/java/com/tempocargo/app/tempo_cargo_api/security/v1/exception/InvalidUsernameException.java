package com.tempocargo.app.tempo_cargo_api.security.v1.exception;

public class InvalidUsernameException extends RuntimeException {

    public InvalidUsernameException(String message) {
        super(message);
    }
}
