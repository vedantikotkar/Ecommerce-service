package com.example.auth.controller;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class PropertyChecker {

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    @PostConstruct
    public void checkKeys() {
        System.out.println("Razorpay Key ID: " + keyId);
        System.out.println("Razorpay Key Secret: " + (keySecret == null ? "null" : "Loaded"));
    }
}
