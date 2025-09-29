package com.example.booking_app.dto;

import com.example.booking_app.model.Status;

public record BookingSearchParameterDto(Long userId, Status status) {
}
