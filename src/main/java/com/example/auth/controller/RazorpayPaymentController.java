package com.example.auth.controller;

import com.example.auth.service.RazorpayPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/payment")
@CrossOrigin(origins = "*")  // Allow frontend requests
public class RazorpayPaymentController {

    private final RazorpayPaymentService paymentService;

    @Autowired
    public RazorpayPaymentController(RazorpayPaymentService paymentService) {
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
     * Endpoint for verifying Razorpay payment.
     */
    @PostMapping("/verify-payment")
    public ResponseEntity<Map<String, Object>> verifyPayment(
            @RequestParam String orderId,
            @RequestParam String paymentId,
            @RequestParam String signature) {

        boolean isValid = paymentService.verifyPayment(orderId, paymentId, signature);
        Map<String, Object> response = new HashMap<>();

        if (isValid) {
            response.put("status", "success");
            response.put("message", "Payment Verified Successfully");
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "failed");
            response.put("message", "Payment Verification Failed");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
