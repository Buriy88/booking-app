package com.example.booking_app.controller;

import com.example.booking_app.dto.BookingDto;
import com.example.booking_app.dto.BookingRequestDto;
import com.example.booking_app.dto.BookingSearchParameterDto;
import com.example.booking_app.model.Status;
import com.example.booking_app.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Tag(name = "Bookings", description = "Managing users' bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Create a new booking", 
        description = "Permits the creation of new accommodation bookings.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Booking successfully created"),
        @ApiResponse(responseCode = "400", description = "Invalid booking request"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public BookingDto createBooking(@RequestBody @Valid BookingRequestDto requestDto) {
        return bookingService.createBooking(requestDto);
    }

    @GetMapping
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Get bookings by user and status (for managers)",
        description = "Retrieves bookings based on user ID and their status.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Bookings retrieved successfully"),
        @ApiResponse(responseCode = "403", description = "Forbidden for non-managers")
    })
    public List<BookingDto> getBookings(
        @Parameter(description = "User ID") 
        @RequestParam(required = false) Long userId,
        @Parameter(description = "Booking status") @RequestParam(required = false) String status) {
        return bookingService.getBookingsByUserAndStatus(
            new BookingSearchParameterDto(userId, status != null ? Status.valueOf(status) : null)
        );
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Get my bookings", 
        description = "Retrieves bookings of the currently authenticated user.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User bookings retrieved successfully"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public List<BookingDto> getMyBookings() {
        return bookingService.getMyBookings();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER','MANAGER')")
    @Operation(summary = "Get booking by ID", 
        description = "Provides information about a specific booking.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Booking retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    public BookingDto getBookingById(@PathVariable Long id) {
        return bookingService.getBookingById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Update booking", 
        description = "Allows users to update their booking details.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Booking updated successfully"),
        @ApiResponse(responseCode = "404", description = "Booking not found"),
        @ApiResponse(responseCode = "403", description = "Forbidden for non-owners")
    })
    public BookingDto updateBooking(
        @PathVariable Long id,
        @RequestBody @Valid BookingRequestDto requestDto) {
        return bookingService.updateBooking(id, requestDto);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Partially update booking", 
        description = "Allows users to partially update their booking details.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Booking updated successfully"),
        @ApiResponse(responseCode = "404", description = "Booking not found"),
        @ApiResponse(responseCode = "403", description = "Forbidden for non-owners")
    })
    public BookingDto partiallyUpdateBooking(
        @PathVariable Long id,
        @RequestBody BookingRequestDto requestDto) {
        return bookingService.updateBooking(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "Cancel booking", description = "Enables the cancellation of bookings.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Booking cancelled successfully"),
        @ApiResponse(responseCode = "404", description = "Booking not found"),
        @ApiResponse(responseCode = "403", description = "Forbidden for non-owners")
    })
    public void cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
    }
}
