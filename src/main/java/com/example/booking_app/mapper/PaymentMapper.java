package com.example.booking_app.mapper;

import com.example.booking_app.dto.PaymentResponseDto;
import com.example.booking_app.model.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentResponseDto toDto(Payment payment);
}
