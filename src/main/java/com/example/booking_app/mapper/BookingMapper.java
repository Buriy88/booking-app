package com.example.booking_app.mapper;

import com.example.booking_app.dto.BookingRequestDto;
import com.example.booking_app.dto.BookingDto;
import com.example.booking_app.model.Booking;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    default BookingDto toDto(Booking booking) {
        if (booking == null) {
            return null;
        }

        BookingDto dto = new BookingDto();
        dto.setId(booking.getId());

        if (booking.getUser() != null) {
            dto.setUserEmail(booking.getUser().getEmail());
        }

        if (booking.getAccommodation() != null) {
            dto.setAccommodationId(booking.getAccommodation().getId());
        }

        dto.setCheckInDate(booking.getCheckInDate());
        dto.setCheckOutDate(booking.getCheckOutDate());

        if (booking.getStatus() != null) {
            dto.setStatus(booking.getStatus().name());
        }

        return dto;
    }


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "accommodation", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateBookingFromDto(BookingRequestDto dto,
                              @MappingTarget Booking booking);
}
