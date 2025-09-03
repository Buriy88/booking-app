package com.example.booking_app.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Accommodation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Type type;

    private String location;

    private String size;

    @ElementCollection
    @CollectionTable(
            name = "accommodation_amenities",
            joinColumns = @JoinColumn(name = "accommodation_id")
    )
    @Column(name = "amenity")
    private List<String> amenities = new ArrayList<>();

    private BigDecimal dailyRate;

    private Integer availability;


}
