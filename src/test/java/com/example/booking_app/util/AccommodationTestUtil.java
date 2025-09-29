package com.example.booking_app.util;

import com.example.booking_app.dto.AccommodationDto;
import com.example.booking_app.dto.CreateAccommodationRequestDto;
import com.example.booking_app.model.Type;
import java.math.BigDecimal;
import java.util.List;


import static com.example.booking_app.util.AccommodationTestConstants.*;

public class AccommodationTestUtil {

    public static CreateAccommodationRequestDto createInvalidAccommodationRequestDto() {
        CreateAccommodationRequestDto dto = new CreateAccommodationRequestDto();
        dto.setType(TYPE);
        dto.setLocation(LOCATION);
        dto.setSize(null);
        dto.setAmenities(AMENITIES);
        dto.setDailyRate(INVALID_DAILY_RATE);
        dto.setAvailability(INVALID_AVAILABILITY);
        return dto;
    }

    public static CreateAccommodationRequestDto createAccommodationRequest() {
        CreateAccommodationRequestDto dto = new CreateAccommodationRequestDto();
        dto.setType(TYPE);
        dto.setLocation(LOCATION);
        dto.setSize(SIZE);
        dto.setAmenities(AMENITIES);
        dto.setDailyRate(DAILY_RATE);
        dto.setAvailability(AVAILABILITY);
        return dto;
    }

    public static AccommodationDto createKyivApartment() {
        return new AccommodationDto()
            .setId(1L)
            .setType(Type.APARTMENT)
            .setLocation("Kyiv, Khreshchatyk St. 10")
            .setSize("1 Bedroom")
            .setDailyRate(new BigDecimal("30.00"))
            .setAvailability(5)
            .setAmenities(List.of("WiFi", "Air Conditioning"));
    }

    public static AccommodationDto createLvivHouse() {
        return new AccommodationDto()
            .setId(2L)
            .setType(Type.HOUSE)
            .setLocation("Lviv, Franka St. 25")
            .setSize("3 Bedroom")
            .setDailyRate(new BigDecimal("120.00"))
            .setAvailability(2)
            .setAmenities(List.of("Garden", "Parking"));
    }

    public static AccommodationDto createOdessaCondo() {
        return new AccommodationDto()
            .setId(3L)
            .setType(Type.CONDO)
            .setLocation("Odessa, Deribasivska St. 5")
            .setSize("Studio")
            .setDailyRate(new BigDecimal("50.00"))
            .setAvailability(10)
            .setAmenities(List.of("Sea View", "WiFi"));
    }

    public static List<AccommodationDto> getAllTestAccommodations() {
        return List.of(
            createKyivApartment(),
            createLvivHouse(),
            createOdessaCondo()
        );
    }

    public static CreateAccommodationRequestDto createUpdateAccommodationDto() {
        return new CreateAccommodationRequestDto()
            .setType(Type.CONDO)
            .setLocation("Updated Location")
            .setSize("Updated Studio")
            .setDailyRate(new BigDecimal("88"))
            .setAvailability(10)
            .setAmenities(List.of("Sea View", "WiFi"));
    }

}
