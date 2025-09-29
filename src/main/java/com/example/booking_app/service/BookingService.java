package com.example.booking_app.service;

import com.example.booking_app.dto.BookingRequestDto;
import com.example.booking_app.dto.BookingDto;
import com.example.booking_app.dto.BookingSearchParameterDto;
import java.util.List;

public interface BookingService {

    BookingDto createBooking(BookingRequestDto requestDto);

    List<BookingDto> getBookingsByUserAndStatus(BookingSearchParameterDto dto);


    List<BookingDto> getMyBookings();

    BookingDto getBookingById(Long id);

    BookingDto updateBooking(Long bookingId, BookingRequestDto requestDto);

    void cancelBooking(Long bookingId);
}
