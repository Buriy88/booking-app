package com.example.booking_app.dto.booking;

import com.example.booking_app.model.Status;

public record BookingSearchParameterDto(Long userId, Status status) {
}
