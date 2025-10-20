package com.tempocargo.app.tempo_cargo_api.security.v1.service;

import com.tempocargo.app.tempo_cargo_api.auth.v1.user.model.TempoUser;
import com.tempocargo.app.tempo_cargo_api.auth.v1.user.repository.TempoUserRepository;
import com.tempocargo.app.tempo_cargo_api.security.v1.dto.request.ResetPasswordRequest;
import com.tempocargo.app.tempo_cargo_api.security.v1.dto.response.ForgotPasswordOtpVerificationResponse;
import com.tempocargo.app.tempo_cargo_api.security.v1.dto.response.ResetPasswordResponse;
import com.tempocargo.app.tempo_cargo_api.security.v1.exception.*;
import com.tempocargo.app.tempo_cargo_api.security.v1.dto.request.ForgotPasswordEmailVerificationRequest;
import com.tempocargo.app.tempo_cargo_api.security.v1.dto.request.ForgotPasswordOtpVerificationRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

@Service
@AllArgsConstructor
public class ForgotPasswordService {

    private final TempoUserRepository tempoUserRepository;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public String emailVerification(ForgotPasswordEmailVerificationRequest request) throws IOException {
        TempoUser entityUser = validateEmailFromRequest(request.getEmail());

        String forgetPasswordOtp = String.format("%06d", new SecureRandom().nextInt(999999));
        entityUser.setRecuperationToken(forgetPasswordOtp);

        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
        LocalDateTime expiresAt = now.plusMinutes(6);
        entityUser.setRecuperationTokenExpAt(expiresAt);

        tempoUserRepository.save(entityUser);

        mailService.sendForgetPasswordOtp(request.getEmail(), forgetPasswordOtp);

        return "Verification Code sent to email provided";
    }

    @Transactional
    public ForgotPasswordOtpVerificationResponse otpVerification(ForgotPasswordOtpVerificationRequest request) {
        TempoUser entityUser = validateEmailFromRequest(request.getEmail());

        if (entityUser.getRecuperationToken() == null || entityUser.getRecuperationTokenExpAt() == null) {
            throw new ForgotPasswordOtpNotRequestedException("Forgot Password Recuperation Token not requested for this account");
        }

        if (!entityUser.getRecuperationToken().equals(request.getOtp())) {
            throw new IllegalArgumentException("Incorrect Recuperation Token: " + request.getOtp());
        }

        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
        if (entityUser.getRecuperationTokenExpAt().isBefore(now)) {
            throw new ForgotPasswordOtpExpiredException("Forgot Password Recuperation Token already expired");
        }

        entityUser.setIsRecuperationTokenVerified(true);
        LocalDateTime requestExpiresAt = LocalDateTime.now().plusMinutes(5).truncatedTo(ChronoUnit.MICROS);
        entityUser.setResetPasswordRequestExpAt(requestExpiresAt);
        tempoUserRepository.save(entityUser);

        return ForgotPasswordOtpVerificationResponse.builder()
                .message("Forgot Password OTP verified successfully")
                .requestExpiresAt(requestExpiresAt.atZone(ZoneOffset.UTC).toInstant())
                .build();
    }

    @Transactional
    public ResetPasswordResponse resetPassword(ResetPasswordRequest request) {
        TempoUser entityUser = validateEmailFromRequest(request.getEmail());

        if (hasNullRecoveryFields(entityUser)) {
            throw new ForgotPasswordNotRequestedException("Forgot Password process not requested for this account");
        }

        if (!entityUser.getIsRecuperationTokenVerified()) {
            throw new ForgotPasswordOtpUnverifiedException("Forgot Password Verification Token has not been verified");
        }

        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MICROS);
        if (entityUser.getResetPasswordRequestExpAt().isBefore(now)) {
            invalidateRecoveryFields(entityUser);
            throw new ForgotPasswordRequestExpired("Forgot Password Request already expired. Request a new Forgot Password Verification Token");
        }

        String newPasswordHash = passwordEncoder.encode(request.getNewPassword());
        entityUser.setPasswordHash(newPasswordHash);

        invalidateRecoveryFields(entityUser);

        return new ResetPasswordResponse("Reset Password Completed Successfully");
    }

    private TempoUser validateEmailFromRequest(String email) {
        return tempoUserRepository.findByEmail(email).orElseThrow(
                () -> new UnregisteredEmailException("Email is not registered: " + email));
    }

    private boolean hasNullRecoveryFields(TempoUser entityUser) {
        return entityUser.getRecuperationToken() == null ||
                entityUser.getRecuperationTokenExpAt() == null ||
                entityUser.getIsRecuperationTokenVerified() == null ||
                entityUser.getResetPasswordRequestExpAt() == null;
    }

    private void invalidateRecoveryFields(TempoUser entityUser) {
        entityUser.setRecuperationToken(null);
        entityUser.setRecuperationTokenExpAt(null);
        entityUser.setIsRecuperationTokenVerified(false);
        entityUser.setResetPasswordRequestExpAt(null);
        tempoUserRepository.save(entityUser);
    }
}
