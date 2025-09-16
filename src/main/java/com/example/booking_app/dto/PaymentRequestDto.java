package com.example.booking_app.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class PaymentRequestDto {
    @NotNull
    private Long bookingId;

    @NotNull
    @Min(1)
    private BigDecimal amount;
}
