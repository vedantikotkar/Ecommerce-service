package com.example.auth.controller;

import com.example.auth.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/payment")
@CrossOrigin(origins = "*")  // Allow frontend requests
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * Endpoint to create a new Razorpay order.
     */
    @PostMapping("/create-order")
    public Map<String, String> createOrder(@RequestParam double amount, @RequestParam(defaultValue = "INR") String currency) {
        return paymentService.createOrder(amount, currency);
    }

    /**
     * Endpoint to verify payment after completion.
     */
    @PostMapping("/verify-payment")
    public Map<String, String> verifyPayment(@RequestParam String orderId, @RequestParam String paymentId, @RequestParam String signature) {
        boolean isValid = paymentService.verifyPayment(orderId, paymentId, signature);
        return Map.of("status", isValid ? "Payment Successful" : "Payment Verification Failed");
    }
}
