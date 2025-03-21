package com.example.auth.controller;


import com.stripe.model.Event;
import com.stripe.net.Webhook;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stripe")
public class StripeWebhookController {

    private static final String ENDPOINT_SECRET = "whsec_your_webhook_secret";

    @PostMapping("/webhook")
    public String handleStripeWebhook(@RequestBody String payload,
                                      @RequestHeader("Stripe-Signature") String sigHeader) {
        try {
            Event event = Webhook.constructEvent(payload, sigHeader, ENDPOINT_SECRET);
            if ("payment_intent.succeeded".equals(event.getType())) {
                System.out.println("💳 Payment Successful!");
            }
        } catch (Exception e) {
            return "Webhook error: " + e.getMessage();
        }
        return "Webhook received!";
    }
}
