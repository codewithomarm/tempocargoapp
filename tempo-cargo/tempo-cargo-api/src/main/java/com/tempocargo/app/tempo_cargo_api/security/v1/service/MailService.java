package com.tempocargo.app.tempo_cargo_api.security.v1.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MailService {

    private final TemplateEngine templateEngine;

    @Value("${SENDGRID_API_KEY}")
    private String sendgridApiKey;

    public void sendTestMail(String toEmail) throws IOException {
        Email from = new Email("no-reply@tempocargo.lat");
        String subject = "Bienvenido a TempoCargo!";
        Email to = new Email(toEmail);
        Content content = new Content("text/plain", "Gracias por registrarte en TempoCargo.");
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendgridApiKey);
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        Response response = sg.api(request);
        System.out.println("SendGrid Status: " + response.getStatusCode());
    }

    public void sendEmailVerificationOtp(String toEmail, String otp) throws IOException {
        Context context = new Context();
        context.setVariable("otp", otp);

        String htmlContent = templateEngine.process("otp-email-verification", context);

        Email from = new Email("no-reply@tempocargo.lat");
        String subject = "Tempo Cargo Registration OTP";
        Email to = new Email(toEmail);
        Content content = new Content("text/html", htmlContent);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendgridApiKey);
        Request request = new Request();
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        Response response = sg.api(request);
        System.out.println("SendGrid Status: " + response.getStatusCode());
    }
}
