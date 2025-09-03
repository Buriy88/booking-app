package com.example.booking_app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import com.example.booking_app.model.Type;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class CreateAccommodationRequestDto {
    @NotNull
    private Type type;
    @NotBlank
    private String location;
    @NotBlank
    private String size;
    private List<String> amenities = new ArrayList<>();

    @NotNull
    @Positive
    private BigDecimal dailyRate;

    @NotNull
    @PositiveOrZero
    private Integer availability;

}
