package com.tempocargo.app.tempo_cargo_api.security.v1.exception;

public class ForgotPasswordOtpExpiredException extends RuntimeException {

    public ForgotPasswordOtpExpiredException(String message){
        super(message);
    }
}
