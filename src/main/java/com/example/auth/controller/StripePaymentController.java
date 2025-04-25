package com.example.auth.controller;
import com.example.auth.service.StripePaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/stripe")
public class StripePaymentController {

    private final StripePaymentService stripePaymentService;

    public StripePaymentController(StripePaymentService stripePaymentService) {
        this.stripePaymentService = stripePaymentService;
    }

    @PostMapping("/create-payment-intent")
    public ResponseEntity<Map<String, Object>> createPaymentIntent(
            @RequestParam double amount,
            @RequestParam String currency) {

        Map<String, Object> response = stripePaymentService.createPaymentIntent(amount, currency);
        return ResponseEntity.ok(response);
    }
}
