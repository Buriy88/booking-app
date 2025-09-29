package com.example.booking_app.util;

import com.example.booking_app.dto.BookingDto;
import com.example.booking_app.dto.BookingRequestDto;
import com.example.booking_app.model.Accommodation;
import com.example.booking_app.model.Booking;
import com.example.booking_app.model.User;
import static com.example.booking_app.util.BookingTestConstants.ACCOMMODATION_ID;
import static com.example.booking_app.util.BookingTestConstants.CHECK_IN;
import static com.example.booking_app.util.BookingTestConstants.CHECK_OUT;
import static com.example.booking_app.util.BookingTestConstants.STATUS;
import static com.example.booking_app.util.BookingTestConstants.USER_EMAIL;
import static com.example.booking_app.util.BookingTestConstants.USER_ID;
import static com.example.booking_app.util.BookingTestConstants.VALID_ID;

public class BookingTestUtil {

    public static BookingRequestDto createBookingRequest() {
        BookingRequestDto dto = new BookingRequestDto();
        dto.setAccommodationId(ACCOMMODATION_ID);
        dto.setCheckInDate(CHECK_IN);
        dto.setCheckOutDate(CHECK_OUT);
        return dto;
    }

    public static User createUser() {
        User user = new User();
        user.setId(USER_ID);
        user.setEmail(USER_EMAIL);
        return user;
    }

    public static Accommodation createAccommodation() {
        Accommodation acc = new Accommodation();
        acc.setId(ACCOMMODATION_ID);
        acc.setLocation("Kyiv");
        return acc;
    }

    public static Booking createBooking(User user, Accommodation acc) {
        Booking booking = new Booking();
        booking.setId(VALID_ID);
        booking.setUser(user);
        booking.setAccommodation(acc);
        booking.setCheckInDate(CHECK_IN);
        booking.setCheckOutDate(CHECK_OUT);
        booking.setStatus(STATUS);
        return booking;
    }

    public static BookingDto createBookingDto() {
        BookingDto dto = new BookingDto();
        dto.setId(VALID_ID);
        dto.setUserEmail(USER_EMAIL);
        dto.setAccommodationId(ACCOMMODATION_ID);
        dto.setCheckInDate(CHECK_IN);
        dto.setCheckOutDate(CHECK_OUT);
        dto.setStatus(STATUS.name());
        return dto;
    }
}
