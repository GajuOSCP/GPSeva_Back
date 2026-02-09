package com.example.Gpseva.controller;

import com.razorpay.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

   @Value("${razorpay.key.id}")
   private String keyId;

   @Value("${razorpay.key.secret}")
   private String keySecret;

   @PostMapping("/create-order")
   public Map<String, Object> createOrder(@RequestBody Map<String, Object> data) throws RazorpayException {

       int amount = Integer.parseInt(data.get("amount").toString());

       RazorpayClient client = new RazorpayClient(keyId, keySecret);

       JSONObject options = new JSONObject();
       options.put("amount", amount);
       options.put("currency", "INR");
       options.put("receipt", "gpseva_rcpt_01");

       Order order = client.orders.create(options);

       return Map.of(
           "orderId", order.get("id"),
           "amount", order.get("amount")
       );
   }
}

