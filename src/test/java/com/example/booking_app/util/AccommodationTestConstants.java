package com.example.booking_app.util;

import com.example.booking_app.model.Type;

import java.math.BigDecimal;
import java.util.List;

public class AccommodationTestConstants {
    public static final Long VALID_ID = 1L;

    public static final Long INVALID_ID = 999L;

    public static final Type TYPE = Type.APARTMENT;

    public static final String LOCATION = "Kyiv, Khreshchatyk St. 10";

    public static final String SIZE = "1 Bedroom";

    public static final Integer INVALID_AVAILABILITY = -2;

    public static final BigDecimal DAILY_RATE = BigDecimal.valueOf(30.00);

    public static final BigDecimal INVALID_DAILY_RATE = BigDecimal.valueOf(-30.00);

    public static final Integer AVAILABILITY = 5;

    public static final List<String> AMENITIES = List.of("WiFi", "Air Conditioning");
}
