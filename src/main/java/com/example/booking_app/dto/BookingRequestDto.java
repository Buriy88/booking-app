package com.example.booking_app.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class BookingRequestDto {
    private Long accommodationId;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;
}
