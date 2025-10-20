package com.tempocargo.app.tempo_cargo_api.security.v1.exception;

public class ForgotPasswordNotRequestedException extends RuntimeException {

    public ForgotPasswordNotRequestedException(String message){
        super(message);
    }
}
