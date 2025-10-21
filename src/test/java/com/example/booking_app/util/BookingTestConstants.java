package com.example.booking_app.util;

import com.example.booking_app.model.Status;

import java.time.LocalDate;

public class BookingTestConstants {
    public static final Long VALID_ID = 100L;
    public static final Long INVALID_ID = 999L;

    public static final Long USER_ID = 42L;
    public static final String USER_EMAIL = "user@example.com";

    public static final Long ACCOMMODATION_ID = 10L;

    public static final LocalDate CHECK_IN = LocalDate.of(2025, 9, 25);
    public static final LocalDate CHECK_OUT = LocalDate.of(2025, 9, 28);

    public static final Status STATUS = Status.PENDING;
}
