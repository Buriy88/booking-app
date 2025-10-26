package com.example.booking_app.service.implementation;

import com.example.booking_app.dto.booking.BookingRequestDto;
import com.example.booking_app.dto.booking.BookingDto;
import com.example.booking_app.dto.booking.BookingSearchParameterDto;
import com.example.booking_app.exception.EntityNotFoundException;
import com.example.booking_app.mapper.BookingMapper;
import com.example.booking_app.model.Accommodation;
import com.example.booking_app.model.Booking;
import com.example.booking_app.model.Status;
import com.example.booking_app.model.User;
import com.example.booking_app.repository.AccommodationRepository;
import com.example.booking_app.repository.BookingRepository;
import com.example.booking_app.repository.UserRepository;
import com.example.booking_app.repository.BookingSpecificationBuilder;
import com.example.booking_app.service.BookingService;
import com.example.booking_app.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {

    public static final String USER = "user";

    public static final String STATUS = "status";

    private final BookingRepository bookingRepository;

    private final UserRepository userRepository;

    private final BookingMapper bookingMapper;

    private final BookingSpecificationBuilder bookingSpecificationBuilder;

    private AccommodationRepository accommodationRepository;

    private final NotificationService notificationService;

    @Override
    public BookingDto createBooking(BookingRequestDto requestDto) {
        Booking booking = new Booking();
        booking.setCheckInDate(requestDto.getCheckInDate());
        booking.setCheckOutDate(requestDto.getCheckOutDate());
        booking.setUser(getCurrentUser());
        booking.setStatus(Status.PENDING);
        Accommodation accommodation =
            accommodationRepository.findById(requestDto.getAccommodationId())
                .orElseThrow(() -> new EntityNotFoundException("Booking with id:"
                    + booking.getId() + "not found"));
        booking.setAccommodation(accommodation);
        bookingRepository.save(booking);
        notificationService.sendNotification(
            "✅ New booking created! ID: " + booking.getId()
        );
        return bookingMapper.toDto(booking);
    }


    @Override
    public List<BookingDto> getBookingsByUserAndStatus(BookingSearchParameterDto dto) {
        Map<String, List<String>> paramMap = new HashMap<>();

        if (dto.status() != null) {
            paramMap.put(STATUS, List.of(dto.status().name()));
        }
        if (dto.userId() != null) {
            paramMap.put(USER, List.of(dto.userId().toString()));
        }

        Specification<Booking> spec = bookingSpecificationBuilder.build(paramMap);


        return bookingRepository.findAll(spec).stream()
            .map(bookingMapper::toDto)
            .toList();
    }


    @Override
    public List<BookingDto> getMyBookings() {
        User user = getCurrentUser();
        return bookingRepository.searchBookingByUser(user)
            .stream()
            .map(bookingMapper::toDto)
            .toList();
    }

    @Override
    public BookingDto getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Booking with id " + id
                + " not found"));
        return bookingMapper.toDto(booking);
    }

    @Override
    public BookingDto updateBooking(Long bookingId, BookingRequestDto requestDto) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new EntityNotFoundException("Booking with id " + bookingId
                + " not found"));
        bookingMapper.updateBookingFromDto(requestDto, booking);
        bookingRepository.save(booking);
        return bookingMapper.toDto(booking);
    }

    @Override
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new EntityNotFoundException("Booking with id " + bookingId
                + " not found"));
        bookingRepository.delete(booking);

        notificationService.sendNotification(
            "❌ Booking canceled! ID: " + bookingId
        );

    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException(
                "User not found with email: " + email));
    }
}
