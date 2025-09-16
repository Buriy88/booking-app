package com.example.booking_app.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class BookingDto {
    private Long id;

    private String userEmail;

    private Long accommodationId;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private String status;
}
