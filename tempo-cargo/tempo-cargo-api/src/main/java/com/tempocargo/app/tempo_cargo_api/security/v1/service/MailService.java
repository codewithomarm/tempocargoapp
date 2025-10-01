package com.tempocargo.app.tempo_cargo_api.security.v1.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    public void sendTestMail(String to) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@tempocargo.lat");
        message.setTo(to);
        message.setSubject("Test Email from TempoCargo API");
        message.setText("Hello! This is a test email sent from your API and Docker Mailserver setup.");
        mailSender.send(message);
    }
}
