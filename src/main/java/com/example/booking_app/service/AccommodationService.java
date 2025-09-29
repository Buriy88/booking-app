package com.example.booking_app.service;

import com.example.booking_app.dto.accommodation.AccommodationDto;
import com.example.booking_app.dto.accommodation.CreateAccommodationRequestDto;
import java.util.List;

public interface AccommodationService {
    AccommodationDto createAccommodation(CreateAccommodationRequestDto accommodation);

    List<AccommodationDto> getAllAccommodations();

    AccommodationDto getAccommodationById(Long id);

    AccommodationDto updateAccommodation(Long id,
                                         CreateAccommodationRequestDto accommodation);

    void deleteAccommodation(Long id);
}
