package com.example.booking_app.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentRequestDto {
    @NotNull
    private Long bookingId;

}
