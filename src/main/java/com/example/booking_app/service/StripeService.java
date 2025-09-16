package com.example.booking_app.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class StripeService {
    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    public Session createCheckoutSession(Long bookingId, BigDecimal amount) throws StripeException {
        SessionCreateParams params = SessionCreateParams.builder()
            .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setSuccessUrl(
                "http://localhost:8081/api/payments/success?session_id={CHECKOUT_SESSION_ID}")
            .setCancelUrl(
                "http://localhost:8081/api/payments/cancel?session_id={CHECKOUT_SESSION_ID}")
            .addLineItem(
                SessionCreateParams.LineItem.builder()
                    .setQuantity(1L)
                    .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency("usd")
                            .setUnitAmount(
                                amount.multiply(BigDecimal.valueOf(100)).longValue()) // у центах
                            .setProductData(
                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                    .setName("Booking #" + bookingId)
                                    .build()
                            )
                            .build()
                    )
                    .build()
            )
            .build();

        return Session.create(params);
    }
}
