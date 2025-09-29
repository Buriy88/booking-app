package com.example.booking_app.dto;

import com.example.booking_app.model.PaymentStatus;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class PaymentResponseDto {
    private Long id;

    private Long bookingId;

    private PaymentStatus status;

    private BigDecimal amountToPay;

    private String sessionUrl;
}
