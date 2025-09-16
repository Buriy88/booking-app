package com.example.booking_app.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

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
