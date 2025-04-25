package com.example.auth.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class RecaptchaController {

    @Value("${recaptcha.secret}") // Load from application.properties
    private String recaptchaSecret;

    private static final String RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    @PostMapping("/verify-recaptcha")
    public ResponseEntity<Map<String, Object>> verifyRecaptcha(@RequestBody Map<String, String> requestBody) {
        String token = requestBody.get("token");

        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Token is missing"
            ));
        }

        // Construct verification URL
        String url = RECAPTCHA_VERIFY_URL + "?secret=" + recaptchaSecret + "&response=" + token;

        // Send verification request to Google
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = restTemplate.postForObject(url, null, Map.class);

        System.out.println("Google reCAPTCHA Response: " + response);

        if (response != null && Boolean.TRUE.equals(response.get("success"))) {
            Double score = (Double) response.getOrDefault("score", 0.0);
            if (score > 0.5) { // Ensure bot detection threshold
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "message", "reCAPTCHA verified!"
                ));
            }
        }

        return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "reCAPTCHA verification failed!",
                "details", response
        ));
    }
}
