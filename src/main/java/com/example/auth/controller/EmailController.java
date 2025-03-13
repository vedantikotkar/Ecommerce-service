package com.example.auth.controller;

import com.example.auth.dto.EmailRequest;
import com.example.auth.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest emailRequest) {
        String to = emailRequest.getEmail();
        String subject = "Hello " + emailRequest.getName();
        String body = "Congratulations " + emailRequest.getName() + " (" + emailRequest.getPhone() + "):\n\n" + emailRequest.getMessage();

        emailService.sendEmail(to, subject, body);

        return ResponseEntity.ok("Email sent successfully!");
    }
}