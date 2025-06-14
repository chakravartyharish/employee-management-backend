package com.interview.employee_management.controller;

import com.interview.employee_management.model.PaymentRequest;
import com.interview.employee_management.model.PaymentResponse;
import com.interview.employee_management.service.StripeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final StripeService stripeService;

    @Autowired
    public PaymentController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping("/create-checkout-session")
    public ResponseEntity<PaymentResponse> createCheckoutSession(@RequestBody PaymentRequest paymentRequest) {
        PaymentResponse response = stripeService.createCheckoutSession(paymentRequest);
        if ("success".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/success")
    public String paymentSuccess() {
        return "Payment successful! Thank you for your purchase.";
    }

    @GetMapping("/cancel")
    public String paymentCancel() {
        return "Payment canceled. You have not been charged.";
    }
}
