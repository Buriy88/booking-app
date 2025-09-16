package com.example.booking_app.repository.booking;

import com.example.booking_app.model.Booking;
import com.example.booking_app.repository.SpecificationProvider;
import jakarta.persistence.criteria.CriteriaBuilder;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component("status")
public class StatusSpecificationProvider implements SpecificationProvider<Booking> {
    public static final String KEY = "status";

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public Specification<Booking> getSpecification(List<String> statuses) {
        return (root, query, cb) -> {
            CriteriaBuilder.In<String> predicate = cb.in(root.get("status"));
            statuses.forEach(predicate::value);
            return predicate;
        };
    }
}
