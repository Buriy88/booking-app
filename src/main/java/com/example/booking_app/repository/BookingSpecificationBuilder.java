// BookingSpecificationBuilder.java

package com.example.booking_app.repository;

import com.example.booking_app.model.Booking;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

@Component
public class BookingSpecificationBuilder {
    private final SpecificationProviderManager<Booking> specManager;

    public BookingSpecificationBuilder(SpecificationProviderManager<Booking> specManager) {
        this.specManager = specManager;
    }

    public Specification<Booking> build(Map<String, List<String>> params) {
        Specification<Booking> spec = null;

        for (Map.Entry<String, List<String>> entry : params.entrySet()) {
            Specification<Booking> nextSpec =
                specManager.getSpecification(entry.getKey(), entry.getValue());
            if (spec == null) {
                spec = nextSpec;
            } else {
                spec = spec.and(nextSpec);
            }
        }

        return spec;
    }
}
