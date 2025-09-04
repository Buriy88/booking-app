package com.example.booking_app.controller;

import com.example.booking_app.dto.AccommodationDto;
import com.example.booking_app.dto.CreateAccommodationRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.example.booking_app.service.AccommodationService;

import java.util.List;

@RestController
@RequestMapping("/api/accommodations")
@RequiredArgsConstructor
@Tag(name = "Accommodations", description = "Endpoints for managing accommodations")
public class AccommodationController {

    private final AccommodationService accommodationService;

    @Operation(summary = "Get list of all accommodations", description = "Returns all accommodations")
    @GetMapping
    public List<AccommodationDto> getAll() {
        return accommodationService.getAllAccommodations();
    }

    @Operation(summary = "Get accommodation by ID", description = "Returns a single accommodation by ID")
    @GetMapping("/{id}")
    public AccommodationDto getById(@PathVariable Long id) {
        return accommodationService.getAccommodationById(id);
    }

    @Operation(summary = "Create a new accommodation", description = "Creates a new accommodation")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccommodationDto create( @Valid @RequestBody CreateAccommodationRequestDto dto) {
        return accommodationService.createAccommodation(dto);
    }

    @Operation(summary = "Update an accommodation", description = "Updates accommodation by ID")
    @PutMapping("/{id}")
    public AccommodationDto update(@PathVariable Long id,  @Valid @RequestBody CreateAccommodationRequestDto dto) {
        return accommodationService.updateAccommodation(id, dto);
    }

    @Operation(summary = "Delete an accommodation", description = "Deletes accommodation by ID")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        accommodationService.deleteAccommodation(id);
    }
}
