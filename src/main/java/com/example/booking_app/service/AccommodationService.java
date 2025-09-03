package com.example.booking_app.service;

import com.example.booking_app.dto.AccommodationDto;
import com.example.booking_app.dto.CreateAccommodationRequestDto;

import java.util.List;

public interface AccommodationService {
    AccommodationDto createAccommodation(CreateAccommodationRequestDto accommodation);
    List<AccommodationDto> getAllAccommodations();
    AccommodationDto getAccommodationById(Long id);
    AccommodationDto updateAccommodation(Long id, CreateAccommodationRequestDto accommodation);
    void deleteAccommodation(Long id);
}
