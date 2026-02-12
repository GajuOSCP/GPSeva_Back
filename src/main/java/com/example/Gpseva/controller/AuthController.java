package com.example.Gpseva.controller;

import com.example.Gpseva.dto.SendOtpRequest;
import com.example.Gpseva.dto.VerifyOtpRequest;
import com.example.Gpseva.model.OtpData;
import com.example.Gpseva.service.EmailService;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final EmailService emailService;

    public AuthController(EmailService emailService) {
        this.emailService = emailService;
    }

    // 🔐 Only these emails can login
    private static final Set<String> ALLOWED_EMAILS = Set.of(
            "yashwantgadkar@gmail.com",
            "officialagrozone@gmail.com"
    );

    // 🧠 In-memory OTP storage
    private final Map<String, OtpData> otpStorage = new HashMap<>();

    private static final long OTP_VALIDITY = 5 * 60 * 1000; // 5 minutes

    // =========================
    // 1️⃣ SEND OTP (EMAIL)
    // =========================
    @PostMapping("/send-otp")
    public Map<String, Object> sendOtp(@RequestBody SendOtpRequest request) {

        String email = request.getEmail();

        if (email == null || !email.contains("@")) {
            return response(false, "Invalid email address");
        }

        // 🔥 Check allowed emails
        if (!ALLOWED_EMAILS.contains(email)) {
            return response(false, "Wrong email ID");
        }

        // Generate 6-digit OTP
        String otp = String.valueOf(new Random().nextInt(900000) + 100000);

        long expiryTime = System.currentTimeMillis() + OTP_VALIDITY;

        otpStorage.put(email, new OtpData(otp, expiryTime));

        // ✅ Send OTP via email
        emailService.sendOtpEmail(email, otp);

        return response(true, "OTP sent successfully to email");
    }

    // =========================
    // 2️⃣ VERIFY OTP
    // =========================
    @PostMapping("/verify-otp")
    public Map<String, Object> verifyOtp(@RequestBody VerifyOtpRequest request) {

        String email = request.getEmail();
        String enteredOtp = request.getOtp();

        OtpData otpData = otpStorage.get(email);

        if (otpData == null) {
            return response(false, "OTP not found or expired");
        }

        if (System.currentTimeMillis() > otpData.getExpiryTime()) {
            otpStorage.remove(email);
            return response(false, "OTP expired");
        }

        if (!otpData.getOtp().equals(enteredOtp)) {
            return response(false, "Invalid OTP");
        }

        // Remove OTP after success
        otpStorage.remove(email);

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("message", "Login successful");
        result.put("user", Map.of(
                "id", "1",
                "name", "GP Admin",
                "email", email
        ));

        return result;
    }

    private Map<String, Object> response(boolean success, String message) {
        return Map.of(
                "success", success,
                "message", message
        );
    }
}
