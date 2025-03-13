package com.example.auth.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    private final RazorpayClient razorpayClient;

    public PaymentService(
            @Value("${razorpay.key.id}") String razorpayKeyId,
            @Value("${razorpay.key.secret}") String razorpayKeySecret) throws Exception {
        // Ensure API keys are not null
        if (razorpayKeyId == null || razorpayKeyId.isEmpty() || razorpayKeySecret == null || razorpayKeySecret.isEmpty()) {
            throw new IllegalArgumentException("Razorpay API credentials are missing! Please check application.properties.");
        }
        this.razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
    }

    /**
     * Creates a new Razorpay order.
     *
     * @param amount   Order amount (in INR)
     * @param currency Currency type (e.g., "INR")
     * @return Razorpay Order JSON
     */
    public Map<String, String> createOrder(double amount, String currency) {
        Map<String, String> response = new HashMap<>();
        try {
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", (int) (amount * 100)); // Convert to paise
            orderRequest.put("currency", currency);
            orderRequest.put("receipt", "txn_" + System.currentTimeMillis());

            Order order = razorpayClient.orders.create(orderRequest);
            response.put("orderId", order.get("id"));
            response.put("amount", order.get("amount").toString());
            response.put("currency", order.get("currency"));
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", "Failed to create order: " + e.getMessage());
        }
        return response;
    }

    /**
     * Verifies payment using Razorpay's signature verification
     */
    public boolean verifyPayment(String orderId, String paymentId, String signature) {
        try {
            String generatedSignature = HmacSHA256(orderId + "|" + paymentId, "your_secret_key");
            return generatedSignature.equals(signature);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String HmacSHA256(String data, String key) throws Exception {
        javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA256");
        javax.crypto.spec.SecretKeySpec secretKeySpec = new javax.crypto.spec.SecretKeySpec(key.getBytes(), "HmacSHA256");
        mac.init(secretKeySpec);
        return bytesToHex(mac.doFinal(data.getBytes()));
    }

    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
