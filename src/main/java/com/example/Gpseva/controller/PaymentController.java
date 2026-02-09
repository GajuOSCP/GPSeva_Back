package com.example.Gpseva.controller;

import com.example.Gpseva.entity.Payment;
import com.example.Gpseva.repository.PaymentRepository;
import com.razorpay.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    private final PaymentRepository paymentRepository;

    public PaymentController(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    // ✅ 1️⃣ CREATE ORDER (SAVE TO DB)
    @PostMapping("/create-order")
    public Map<String, Object> createOrder(@RequestBody Map<String, Object> data)
            throws RazorpayException {

        int amountInRupees = Integer.parseInt(data.get("amount").toString());
        String userId = data.getOrDefault("userId", "UNKNOWN").toString();

        int amountInPaise = amountInRupees * 100;

        RazorpayClient client = new RazorpayClient(keyId, keySecret);

        JSONObject options = new JSONObject();
        options.put("amount", amountInPaise);
        options.put("currency", "INR");
        options.put("receipt", "gpseva_rcpt_" + System.currentTimeMillis());

        Order order = client.orders.create(options);

        // 🔥 SAVE ORDER IN DB
        Payment payment = new Payment();
        payment.setUserId(userId);
        payment.setRazorpayOrderId(order.get("id"));
        payment.setAmount(order.get("amount"));
        payment.setCurrency("INR");
        payment.setStatus("CREATED");
        payment.setReceipt(options.getString("receipt"));
        payment.setCreatedAt(LocalDateTime.now());

        paymentRepository.save(payment);

        return Map.of(
                "orderId", order.get("id"),
                "amount", order.get("amount")
        );
    }

    // ✅ 2️⃣ VERIFY PAYMENT & UPDATE DB
    @PostMapping("/verify")
    public String verifyPayment(@RequestBody Map<String, String> data)
            throws RazorpayException {

        String razorpayOrderId = data.get("razorpay_order_id");
        String razorpayPaymentId = data.get("razorpay_payment_id");
        String razorpaySignature = data.get("razorpay_signature");

        Payment payment = paymentRepository
                .findByRazorpayOrderId(razorpayOrderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // 🔐 VERIFY SIGNATURE
        JSONObject options = new JSONObject();
        options.put("razorpay_order_id", razorpayOrderId);
        options.put("razorpay_payment_id", razorpayPaymentId);
        options.put("razorpay_signature", razorpaySignature);

        boolean isValid = Utils.verifyPaymentSignature(options, keySecret);

        if (!isValid) {
            payment.setStatus("FAILED");
            paymentRepository.save(payment);
            throw new RuntimeException("Payment verification failed");
        }

        // ✅ UPDATE PAYMENT AS PAID
        payment.setRazorpayPaymentId(razorpayPaymentId);
        payment.setRazorpaySignature(razorpaySignature);
        payment.setStatus("PAID");
        payment.setPaidAt(LocalDateTime.now());

        paymentRepository.save(payment);

        return "Payment verified and saved successfully";
    }
}
