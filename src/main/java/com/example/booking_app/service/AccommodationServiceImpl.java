package com.example.booking_app.service;

import com.example.booking_app.dto.AccommodationDto;
import com.example.booking_app.dto.CreateAccommodationRequestDto;
import java.util.List;

import com.example.booking_app.exception.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import com.example.booking_app.mapper.AccommodationMapper;
import com.example.booking_app.model.Accommodation;
import org.springframework.stereotype.Service;
import com.example.booking_app.repository.AccommodationRepository;

@Service
@Transactional
@AllArgsConstructor
public class AccommodationServiceImpl implements  AccommodationService {
    private final AccommodationRepository accommodationRepository;
    private final AccommodationMapper accommodationMapper;


    @Override
    public AccommodationDto createAccommodation(CreateAccommodationRequestDto createAccommodation) {
        Accommodation accommodation = accommodationMapper.toEntity(createAccommodation);
        accommodationRepository.save(accommodation);
        return accommodationMapper.toDto(accommodation);

    }

    @Override
    public List<AccommodationDto> getAllAccommodations() {
        return accommodationRepository.findAll()
                .stream()
                .map(accommodationMapper::toDto)
                .toList();

    }

    @Override
    public AccommodationDto getAccommodationById(Long id) {
        Accommodation accommodation = accommodationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Accommodation with id " + id
                        + " not found"));
        return accommodationMapper.toDto(accommodation);
    }

    @Override
    public AccommodationDto updateAccommodation(Long id, CreateAccommodationRequestDto updateAccommodation) {
        Accommodation accommodation = accommodationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Accommodation with id " + id
                        + " not found"));
        accommodationMapper.updateAccommodationFromDto(updateAccommodation, accommodation);
        accommodationRepository.save(accommodation);
        return accommodationMapper.toDto(accommodation);
    }

    @Override
    public void deleteAccommodation(Long id) {
        Accommodation accommodation = accommodationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Accommodation with id " + id
                + " not found"));
        accommodationRepository.delete(accommodation);
    }
}
