package com.example.booking_app.service;

import com.example.booking_app.dto.PaymentRequestDto;
import com.example.booking_app.dto.PaymentResponseDto;
import com.example.booking_app.model.Booking;
import com.example.booking_app.model.Payment;
import com.example.booking_app.model.PaymentStatus;
import com.example.booking_app.repository.BookingRepository;
import com.example.booking_app.repository.PaymentRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    private final BookingRepository bookingRepository;

    private final StripeService stripeService;

    public List<PaymentResponseDto> getPayments(Long userId) {
        return paymentRepository.findAll().stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }

    public PaymentResponseDto createPayment(PaymentRequestDto request) throws StripeException {
        Booking booking = bookingRepository.findById(request.getBookingId())
            .orElseThrow(() -> new RuntimeException("Booking not found"));

        BigDecimal dailyRate = booking.getAccommodation().getDailyRate();
        long days = ChronoUnit.DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate());
        BigDecimal totalAmount = dailyRate.multiply(BigDecimal.valueOf(days));

        Session session =
            stripeService.createCheckoutSession(request.getBookingId());

        Payment payment = new Payment();
        payment.setBookingId(request.getBookingId());
        payment.setAmountToPay(totalAmount);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setSessionId(session.getId());
        payment.setSessionUrl(session.getUrl());
        paymentRepository.save(payment);

        return mapToDto(payment);
    }

    public PaymentResponseDto handleSuccess(String sessionId) throws StripeException {
        Payment payment = paymentRepository.findBySessionId(sessionId)
            .orElseThrow(() -> new RuntimeException("Payment not found"));
        payment.setStatus(PaymentStatus.PAID);
        paymentRepository.save(payment);
        return mapToDto(payment);
    }

    public PaymentResponseDto handleCancel(String sessionId) {
        Payment payment = paymentRepository.findBySessionId(sessionId)
            .orElseThrow(() -> new RuntimeException("Payment not found"));
        payment.setStatus(PaymentStatus.CANCELED);
        paymentRepository.save(payment);
        return mapToDto(payment);
    }

    private PaymentResponseDto mapToDto(Payment payment) {
        PaymentResponseDto dto = new PaymentResponseDto();
        dto.setId(payment.getId());
        dto.setBookingId(payment.getBookingId());
        dto.setStatus(payment.getStatus());
        dto.setAmountToPay(payment.getAmountToPay());
        dto.setSessionUrl(payment.getSessionUrl());
        return dto;
    }
}
