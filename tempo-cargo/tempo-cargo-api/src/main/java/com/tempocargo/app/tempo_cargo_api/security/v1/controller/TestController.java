package com.tempocargo.app.tempo_cargo_api.security.v1.controller;

import com.tempocargo.app.tempo_cargo_api.security.v1.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestController {

    private final MailService mailService;

    @GetMapping("/protected-hello")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Hello from Protected");
    }

    @GetMapping("/send-mail")
    public ResponseEntity<String> sendTestMail() {
        try {
            mailService.sendTestMail("codewithomarm@gmail.com");
            return ResponseEntity.ok("Test email sent successfully to codewithomarm@gmail.com!");
        } catch (Exception e) {
            e.printStackTrace(); // log en consola
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error sending email: " + e.getMessage());
        }
    }
}
