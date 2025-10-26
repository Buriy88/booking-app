package com.example.booking_app.service;

import com.example.booking_app.dto.booking.BookingDto;
import com.example.booking_app.dto.booking.BookingRequestDto;
import com.example.booking_app.dto.booking.BookingSearchParameterDto;
import com.example.booking_app.exception.EntityNotFoundException;
import com.example.booking_app.mapper.BookingMapper;
import com.example.booking_app.model.Accommodation;
import com.example.booking_app.model.Booking;
import com.example.booking_app.model.User;
import com.example.booking_app.repository.AccommodationRepository;
import com.example.booking_app.repository.BookingRepository;
import com.example.booking_app.repository.BookingSpecificationBuilder;
import com.example.booking_app.repository.UserRepository;
import com.example.booking_app.service.implementation.BookingServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static com.example.booking_app.util.BookingTestConstants.ACCOMMODATION_ID;
import static com.example.booking_app.util.BookingTestConstants.INVALID_ID;
import static com.example.booking_app.util.BookingTestConstants.STATUS;
import static com.example.booking_app.util.BookingTestConstants.USER_ID;
import static com.example.booking_app.util.BookingTestConstants.VALID_ID;
import static com.example.booking_app.util.BookingTestUtil.createAccommodation;
import static com.example.booking_app.util.BookingTestUtil.createBooking;
import static com.example.booking_app.util.BookingTestUtil.createBookingDto;
import static com.example.booking_app.util.BookingTestUtil.createBookingRequest;
import static com.example.booking_app.util.BookingTestUtil.createUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock private BookingRepository bookingRepository;
    @Mock private UserRepository userRepository;
    @Mock private BookingMapper bookingMapper;
    @Mock private BookingSpecificationBuilder bookingSpecificationBuilder;
    @Mock private AccommodationRepository accommodationRepository;
    @Mock private NotificationService notificationService;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private User user;
    private Accommodation acc;
    private Booking booking;
    private BookingDto bookingDto;
    private BookingRequestDto createReq;

    @BeforeEach
    void setUp() {
        user = createUser();
        acc = createAccommodation();
        booking = createBooking(user, acc);
        bookingDto = createBookingDto();
        createReq = createBookingRequest();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private void authenticateTestUser() {
        var auth = new UsernamePasswordAuthenticationToken("test@example.com", "123456");
        var ctx = SecurityContextHolder.createEmptyContext();
        ctx.setAuthentication(auth);
        SecurityContextHolder.setContext(ctx);

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
    }

    @Test
    @DisplayName("createBooking: valid request → saves, notifies, returns DTO")
    void createBooking_Valid_ReturnsDto_AndSendsNotification() {
        authenticateTestUser();
        when(accommodationRepository.findById(ACCOMMODATION_ID)).thenReturn(Optional.of(acc));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(inv -> {
            Booking b = inv.getArgument(0);
            b.setId(VALID_ID);
            return b;
        });
        when(bookingMapper.toDto(any(Booking.class))).thenReturn(bookingDto);

        BookingDto result = bookingService.createBooking(createReq);

        assertNotNull(result);
        assertEquals(VALID_ID, result.getId());
        verify(bookingRepository).save(any(Booking.class));
        verify(notificationService).sendNotification(
            argThat(msg -> msg.contains("New booking created") &&
                msg.contains(String.valueOf(VALID_ID)))
        );
    }

    @Test
    @DisplayName("createBooking: accommodation not found → throws RuntimeException")
    void createBooking_AccommodationNotFound_Throws() {
        authenticateTestUser();
        when(accommodationRepository.findById(ACCOMMODATION_ID)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> bookingService.createBooking(createReq));

        assertTrue(ex.getMessage().toLowerCase().contains("accommodation not found"));
        verify(bookingRepository, never()).save(any());
        verify(notificationService, never()).sendNotification(anyString());
    }

    @Test
    @DisplayName("getBookingsByUserAndStatus: builds spec and maps list")
    @SuppressWarnings("unchecked")
    void getBookingsByUserAndStatus_ReturnsList() {
        BookingSearchParameterDto params = new BookingSearchParameterDto(USER_ID, STATUS);
        Specification<Booking> spec = (root, q, cb) -> null;

        when(bookingSpecificationBuilder.build(any(Map.class))).thenReturn(spec);
        when(bookingRepository.findAll(spec)).thenReturn(List.of(booking));
        when(bookingMapper.toDto(booking)).thenReturn(bookingDto);

        var result = bookingService.getBookingsByUserAndStatus(params);

        assertEquals(1, result.size());
        assertEquals(VALID_ID, result.get(0).getId());
        verify(bookingSpecificationBuilder).build(argThat(map ->
            map.containsKey("user") && map.containsKey("status")));
        verify(bookingRepository).findAll(spec);
    }

    @Test
    @DisplayName("getMyBookings: uses SecurityContext user → maps list")
    void getMyBookings_ReturnsList() {
        authenticateTestUser();
        when(bookingRepository.searchBookingByUser(user)).thenReturn(List.of(booking));
        when(bookingMapper.toDto(booking)).thenReturn(bookingDto);

        var result = bookingService.getMyBookings();

        assertEquals(1, result.size());
        assertEquals(VALID_ID, result.get(0).getId());
        verify(bookingRepository).searchBookingByUser(user);
    }

    @Test
    @DisplayName("getBookingById: valid id → returns DTO")
    void getBookingById_Valid_ReturnsDto() {
        when(bookingRepository.findById(VALID_ID)).thenReturn(Optional.of(booking));
        when(bookingMapper.toDto(booking)).thenReturn(bookingDto);

        var result = bookingService.getBookingById(VALID_ID);

        assertEquals(VALID_ID, result.getId());
        verify(bookingRepository).findById(VALID_ID);
    }

    @Test
    @DisplayName("getBookingById: invalid id → throws EntityNotFoundException")
    void getBookingById_Invalid_Throws() {
        when(bookingRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
            () -> bookingService.getBookingById(INVALID_ID));

        assertEquals("Booking with id " + INVALID_ID + " not found", ex.getMessage());
        verify(bookingMapper, never()).toDto(any());
    }

    @Test
    @DisplayName("updateBooking: valid id → mapper.update + save + returns DTO")
    void updateBooking_Valid_UpdatesAndReturnsDto() {
        BookingRequestDto updateReq = createBookingRequest();
        when(bookingRepository.findById(VALID_ID)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(booking)).thenReturn(booking);
        when(bookingMapper.toDto(booking)).thenReturn(bookingDto);

        var result = bookingService.updateBooking(VALID_ID, updateReq);

        assertEquals(VALID_ID, result.getId());
        verify(bookingMapper).updateBookingFromDto(updateReq, booking);
        verify(bookingRepository).save(booking);
    }

    @Test
    @DisplayName("updateBooking: invalid id → throws EntityNotFoundException")
    void updateBooking_Invalid_Throws() {
        when(bookingRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
            () -> bookingService.updateBooking(INVALID_ID, createBookingRequest()));

        verify(bookingRepository, never()).save(any());
    }

    @Test
    @DisplayName("cancelBooking: valid id → delete + notification")
    void cancelBooking_Valid_DeletesAndNotifies() {
        when(bookingRepository.findById(VALID_ID)).thenReturn(Optional.of(booking));

        bookingService.cancelBooking(VALID_ID);

        verify(bookingRepository).delete(booking);
        verify(notificationService).sendNotification(
            argThat(msg -> msg.contains("Booking canceled") &&
                msg.contains(String.valueOf(VALID_ID)))
        );
    }

    @Test
    @DisplayName("cancelBooking: invalid id → throws EntityNotFoundException, no side effects")
    void cancelBooking_Invalid_Throws() {
        when(bookingRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
            () -> bookingService.cancelBooking(INVALID_ID));

        verify(bookingRepository, never()).delete(any(Booking.class));
        verify(notificationService, never()).sendNotification(anyString());
    }
}
