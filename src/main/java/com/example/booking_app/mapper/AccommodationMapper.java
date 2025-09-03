package com.example.booking_app.mapper;

import com.example.booking_app.dto.AccommodationDto;
import com.example.booking_app.dto.CreateAccommodationRequestDto;
import com.example.booking_app.model.Accommodation;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface AccommodationMapper {
    Accommodation toEntity(CreateAccommodationRequestDto createAccommodationRequestDto);
    AccommodationDto toDto(Accommodation accommodation);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAccommodationFromDto(CreateAccommodationRequestDto dto, @MappingTarget Accommodation accommodation);
}

