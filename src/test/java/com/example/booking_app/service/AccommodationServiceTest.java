package com.example.booking_app.service;

import static com.example.booking_app.util.AccommodationTestConstants.AMENITIES;
import static com.example.booking_app.util.AccommodationTestConstants.AVAILABILITY;
import static com.example.booking_app.util.AccommodationTestConstants.DAILY_RATE;
import static com.example.booking_app.util.AccommodationTestConstants.INVALID_ID;
import static com.example.booking_app.util.AccommodationTestConstants.LOCATION;
import static com.example.booking_app.util.AccommodationTestConstants.SIZE;
import static com.example.booking_app.util.AccommodationTestConstants.TYPE;
import static com.example.booking_app.util.AccommodationTestConstants.VALID_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.booking_app.dto.AccommodationDto;
import com.example.booking_app.dto.CreateAccommodationRequestDto;
import com.example.booking_app.exception.EntityNotFoundException;
import com.example.booking_app.mapper.AccommodationMapper;
import com.example.booking_app.model.Accommodation;
import com.example.booking_app.repository.AccommodationRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccommodationServiceTest {

    @Mock
    private AccommodationRepository accommodationRepository;
    @Mock
    private AccommodationMapper accommodationMapper;
    @InjectMocks
    private AccommodationServiceImpl accommodationService;

    private CreateAccommodationRequestDto requestDto;

    private Accommodation mappedAccommodation;

    private Accommodation savedAccommodation;

    private AccommodationDto mappedDto;

    @BeforeEach
    void setUp() {
        requestDto = new CreateAccommodationRequestDto();
        requestDto.setType(TYPE);
        requestDto.setLocation(LOCATION);
        requestDto.setSize(SIZE);
        requestDto.setDailyRate(DAILY_RATE);
        requestDto.setAvailability(AVAILABILITY);
        requestDto.setAmenities(AMENITIES);

        mappedAccommodation = new Accommodation();
        mappedAccommodation.setType(TYPE);
        mappedAccommodation.setLocation(LOCATION);
        mappedAccommodation.setSize(SIZE);
        mappedAccommodation.setDailyRate(DAILY_RATE);
        mappedAccommodation.setAvailability(AVAILABILITY);
        mappedAccommodation.setAmenities(AMENITIES);

        savedAccommodation = new Accommodation();
        savedAccommodation.setId(VALID_ID);
        savedAccommodation.setType(TYPE);
        savedAccommodation.setLocation(LOCATION);
        savedAccommodation.setSize(SIZE);
        savedAccommodation.setDailyRate(DAILY_RATE);
        savedAccommodation.setAvailability(AVAILABILITY);
        savedAccommodation.setAmenities(AMENITIES);

        mappedDto = new AccommodationDto();
        mappedDto.setId(VALID_ID);
        mappedDto.setType(TYPE);
        mappedDto.setLocation(LOCATION);
        mappedDto.setSize(SIZE);
        mappedDto.setDailyRate(DAILY_RATE);
        mappedDto.setAvailability(AVAILABILITY);
        mappedDto.setAmenities(AMENITIES);
    }

    @Test
    @DisplayName("createAccommodation_ValidRequest_ReturnsDto")
    void createAccommodation_ValidRequest_ReturnsDto() {
        when(accommodationMapper.toEntity(requestDto)).thenReturn(mappedAccommodation);
        when(accommodationRepository.save(mappedAccommodation)).thenReturn(savedAccommodation);
        when(accommodationMapper.toDto(any(Accommodation.class))).thenReturn(mappedDto);

        AccommodationDto result = accommodationService.createAccommodation(requestDto);

        assertNotNull(result);
        assertEquals(LOCATION, result.getLocation());
        assertEquals(SIZE, result.getSize());
        assertEquals(DAILY_RATE, result.getDailyRate());
        verify(accommodationRepository).save(mappedAccommodation);
    }

    @Test
    @DisplayName("getAccommodationById_ValidId_ReturnsDto")
    void getAccommodationById_ValidId_ReturnsDto() {
        when(accommodationRepository.findById(VALID_ID))
            .thenReturn(Optional.of(savedAccommodation));
        when(accommodationMapper.toDto(savedAccommodation)).thenReturn(mappedDto);

        AccommodationDto result = accommodationService.getAccommodationById(VALID_ID);

        assertEquals(mappedDto, result);
    }

    @Test
    @DisplayName("getAccommodationById_InvalidId_ThrowsException")
    void getAccommodationById_InvalidId_ThrowsException() {
        when(accommodationRepository.findById(INVALID_ID))
            .thenReturn(Optional.empty());

        Exception ex = assertThrows(EntityNotFoundException.class,
            () -> accommodationService.getAccommodationById(INVALID_ID));

        assertEquals("Accommodation with id " + INVALID_ID + " not found",
            ex.getMessage());
    }

    @Test
    @DisplayName("getAllAccommodations_ReturnsListOfDtos")
    void getAllAccommodations_ReturnsListOfDtos() {
        when(accommodationRepository.findAll()).thenReturn(List.of(savedAccommodation));
        when(accommodationMapper.toDto(savedAccommodation)).thenReturn(mappedDto);

        List<AccommodationDto> result = accommodationService.getAllAccommodations();

        assertEquals(1, result.size());
        assertEquals(mappedDto, result.get(0));
    }

    @Test
    @DisplayName("updateAccommodation_ValidRequest_ReturnsUpdatedDto")
    void updateAccommodation_ValidRequest_ReturnsUpdatedDto() {
        when(accommodationRepository.findById(VALID_ID))
            .thenReturn(Optional.of(savedAccommodation));
        when(accommodationRepository.save(any(Accommodation.class))).thenReturn(savedAccommodation);
        when(accommodationMapper.toDto(savedAccommodation)).thenReturn(mappedDto);

        AccommodationDto result = accommodationService.updateAccommodation(
            VALID_ID, requestDto);

        assertEquals(mappedDto, result);
    }

    @Test
    @DisplayName("updateAccommodation_InvalidId_ThrowsException")
    void updateAccommodation_InvalidId_ThrowsException() {
        when(accommodationRepository.findById(INVALID_ID))
            .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
            () -> accommodationService.updateAccommodation(INVALID_ID, requestDto));
    }

    @Test
    @DisplayName("deleteAccommodation_ValidId_DeletesEntity")
    void deleteAccommodation_ValidId_DeletesEntity() {
        when(accommodationRepository.findById(VALID_ID))
            .thenReturn(Optional.of(savedAccommodation));

        accommodationService.deleteAccommodation(VALID_ID);

        verify(accommodationRepository).delete(savedAccommodation);
    }

    @Test
    @DisplayName("deleteAccommodation_InvalidId_ThrowsException")
    void deleteAccommodation_InvalidId_ThrowsException() {
        when(accommodationRepository.findById(INVALID_ID))
            .thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
            () -> accommodationService.deleteAccommodation(INVALID_ID));
    }
}
