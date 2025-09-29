package com.example.booking_app.dto;

import com.example.booking_app.model.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(exclude = "id")
public class AccommodationDto {
    private Long id;

    private Type type;

    private String location;

    private String size;

    private List<String> amenities = new ArrayList<>();

    private BigDecimal dailyRate;

    private Integer availability;
}
