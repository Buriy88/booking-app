package com.example.booking_app.controller;

import com.example.booking_app.dto.AccommodationDto;
import com.example.booking_app.dto.CreateAccommodationRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import com.example.booking_app.service.AccommodationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accommodations")
@RequiredArgsConstructor
@Tag(name = "Accommodations", description = "Endpoints for managing accommodations")
public class AccommodationController {

    private final AccommodationService accommodationService;

    @Operation(summary = "Get list of all accommodations",
            description = "Returns all accommodations")
    @GetMapping
    @PreAuthorize("hasAnyRole('CUSTOMER', 'MANAGER')")
    public List<AccommodationDto> getAll() {
        return accommodationService.getAllAccommodations();
    }

    @Operation(summary = "Get accommodation by ID",
            description = "Returns a single accommodation by ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'CUSTOMER')")
    public AccommodationDto getById(@PathVariable Long id) {
        return accommodationService.getAccommodationById(id);
    }

    @Operation(summary = "Create a new accommodation", description = "Creates a new accommodation")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('MANAGER')")
    public AccommodationDto create(@Valid @RequestBody CreateAccommodationRequestDto dto) {
        return accommodationService.createAccommodation(dto);
    }

    @Operation(summary = "Update an accommodation", description = "Updates accommodation by ID")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public AccommodationDto update(@PathVariable Long id,
                                   @Valid @RequestBody CreateAccommodationRequestDto dto) {
        return accommodationService.updateAccommodation(id, dto);
    }

    @Operation(summary = "Delete an accommodation", description = "Deletes accommodation by ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('MANAGER')")
    public void delete(@PathVariable Long id) {
        accommodationService.deleteAccommodation(id);
    }
}
