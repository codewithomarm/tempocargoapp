package com.tempocargo.app.tempo_cargo_api.security.v1.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestController {

    @GetMapping("/protected-hello")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Hello from Protected");
    }
}
