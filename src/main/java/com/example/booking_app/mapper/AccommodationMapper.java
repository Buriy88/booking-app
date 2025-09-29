package com.example.booking_app.mapper;

import com.example.booking_app.dto.AccommodationDto;
import com.example.booking_app.dto.CreateAccommodationRequestDto;
import com.example.booking_app.model.Accommodation;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface AccommodationMapper {
    @Mapping(target = "id", ignore = true)
    Accommodation toEntity(CreateAccommodationRequestDto createAccommodationRequestDto);

    @Mapping(target = "id", ignore = true)
    default Accommodation toModel(AccommodationDto dto) {
        if (dto == null) {
            return null;
        }
        Accommodation accommodation = new Accommodation();
        accommodation.setType(dto.getType());
        accommodation.setLocation(dto.getLocation());
        accommodation.setSize(dto.getSize());
        accommodation.setDailyRate(dto.getDailyRate());
        accommodation.setAmenities(dto.getAmenities());

        return accommodation;
    }

    AccommodationDto toDto(Accommodation accommodation);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateAccommodationFromDto(CreateAccommodationRequestDto dto,
                                    @MappingTarget Accommodation accommodation);
}

