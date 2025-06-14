package com.interview.employee_management.service;

import com.interview.employee_management.model.PaymentRequest;
import com.interview.employee_management.model.PaymentResponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;

@Service
public class StripeService {

    @Value("${app.stripe.api.key}")
    private String stripeApiKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    public PaymentResponse createCheckoutSession(PaymentRequest paymentRequest) {
        try {
            // Convert amount to cents (Stripe requires amount in smallest currency unit)
            long amountInCents = paymentRequest.getAmount().multiply(new BigDecimal(100)).longValue();

            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(paymentRequest.getSuccessUrl())
                    .setCancelUrl(paymentRequest.getCancelUrl())
                    .addLineItem(
                            SessionCreateParams.LineItem.builder()
                                    .setPriceData(
                                            SessionCreateParams.LineItem.PriceData.builder()
                                                    .setCurrency(paymentRequest.getCurrency())
                                                    .setUnitAmount(amountInCents)
                                                    .setProductData(
                                                            SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                    .setName(paymentRequest.getProductName())
                                                                    .build()
                                                    )
                                                    .build()
                                    )
                                    .setQuantity(1L)
                                    .build()
                    )
                    .build();

            Session session = Session.create(params);
            return new PaymentResponse(
                    session.getId(),
                    session.getUrl(),
                    "success",
                    "Checkout session created successfully"
            );
        } catch (StripeException e) {
            return new PaymentResponse(
                    null,
                    null,
                    "error",
                    "Error creating checkout session: " + e.getMessage()
            );
        }
    }
}
