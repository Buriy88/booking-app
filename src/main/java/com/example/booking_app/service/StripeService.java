package com.example.booking_app.service;

import com.example.booking_app.exception.EntityNotFoundException;
import com.example.booking_app.model.Booking;
import com.example.booking_app.repository.BookingRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class StripeService {

    private final BookingRepository bookingRepository;

    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    public Session createCheckoutSession(Long bookingId) throws StripeException {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new EntityNotFoundException("Booking with id " + bookingId
                + " not found"));
        BigDecimal accommodationPrice = booking.getAccommodation().getDailyRate();
        long days = ChronoUnit.DAYS.between(booking.getCheckInDate(),booking.getCheckOutDate());
        SessionCreateParams params = SessionCreateParams.builder()
            .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setSuccessUrl(
                "http://localhost:8081/api/payments/success?session_id={CHECKOUT_SESSION_ID}")
            .setCancelUrl(
                "http://localhost:8081/api/payments/cancel?session_id={CHECKOUT_SESSION_ID}")
            .addLineItem(
                SessionCreateParams.LineItem.builder()
                    .setQuantity(days)
                    .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                            .setCurrency("usd")
                            .setUnitAmount(
                                accommodationPrice.multiply(BigDecimal.valueOf(100)).longValue())
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
