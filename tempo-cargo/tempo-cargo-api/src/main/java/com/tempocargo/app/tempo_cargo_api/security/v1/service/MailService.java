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

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MailService {

    @Value("${sendgrid.api-key}")
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
}
