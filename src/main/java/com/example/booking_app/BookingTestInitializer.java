package com.example.booking_app;

import com.example.booking_app.dto.BookingDto;
import com.example.booking_app.dto.BookingSearchParameterDto;
import com.example.booking_app.model.Accommodation;
import com.example.booking_app.model.Booking;
import com.example.booking_app.model.Role;
import com.example.booking_app.model.Status;
import com.example.booking_app.model.Type;
import com.example.booking_app.model.User;
import com.example.booking_app.repository.AccommodationRepository;
import com.example.booking_app.repository.BookingRepository;
import com.example.booking_app.repository.UserRepository;
import com.example.booking_app.service.BookingService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookingTestInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    private final BookingRepository bookingRepository;

    private final AccommodationRepository accommodationRepository;

    private final BookingService bookingService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        Accommodation acc = new Accommodation();
        acc.setType(Type.APARTMENT);
        acc.setLocation("Kyiv");
        acc.setSize("50sqm");
        acc.setDailyRate(new BigDecimal("30"));
        acc.setAvailability(5);
        accommodationRepository.save(acc);

        User user = userRepository.findByEmail("test@example.com")
                .orElseGet(() -> {
                    User u = new User();
                    u.setEmail("test@example.com");
                    u.setPassword(passwordEncoder.encode("123456"));
                    u.setRole(Role.ROLE_MANAGER);
                    return userRepository.save(u);
                });

        Booking b1 = new Booking();
        b1.setUser(user);
        b1.setStatus(Status.PENDING);
        b1.setCheckInDate(LocalDate.now());
        b1.setCheckOutDate(LocalDate.now().plusDays(2));
        b1.setAccommodation(acc);
        bookingRepository.save(b1);

        Booking b2 = new Booking();
        b2.setUser(user);
        b2.setStatus(Status.CONFIRMED);
        b2.setCheckInDate(LocalDate.now().plusDays(3));
        b2.setCheckOutDate(LocalDate.now().plusDays(5));
        b2.setAccommodation(acc);
        bookingRepository.save(b2);

        User otherUser = new User();
        otherUser.setEmail("1234567@example.com");
        otherUser.setPassword(passwordEncoder.encode("123456"));
        otherUser.setRole(Role.ROLE_CUSTOMER);
        userRepository.save(otherUser);
        System.out.println("дійшло");

        Booking b3 = new Booking();
        b3.setUser(otherUser);
        b3.setStatus(Status.PENDING);
        b3.setCheckInDate(LocalDate.now());
        b3.setCheckOutDate(LocalDate.now().plusDays(2));
        b3.setAccommodation(acc);
        bookingRepository.save(b3);
        System.out.println("дійшло знову");

        BookingSearchParameterDto searchDto = new BookingSearchParameterDto(null, Status.PENDING);
        System.out.println("пробую");
        List<BookingDto> results = bookingService.getBookingsByUserAndStatus(searchDto);
        System.out.println("працює чи ні");

        System.out.println("Bookings for user " + otherUser.getEmail() + " with status PENDING:");
        results.forEach(System.out::println);
    }
}
