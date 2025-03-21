package com.example.auth.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class StripePaymentService {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    public Map<String, Object> createPaymentIntent(double amount, String currency) {
        Stripe.apiKey = stripeApiKey;
        Map<String, Object> response = new HashMap<>();

        try {
            // Convert amount to cents
            Map<String, Object> params = new HashMap<>();
            params.put("amount", (int) (amount * 100)); // Stripe requires amount in smallest currency unit
            params.put("currency", currency);
            params.put("payment_method_types", java.util.List.of("card"));

            // Create payment intent
            PaymentIntent paymentIntent = PaymentIntent.create(params);
            response.put("paymentIntentId", paymentIntent.getId());
            response.put("clientSecret", paymentIntent.getClientSecret());
        } catch (StripeException e) {
            e.printStackTrace();
            response.put("error", "Failed to create Stripe payment");
        }

        return response;
    }
}
