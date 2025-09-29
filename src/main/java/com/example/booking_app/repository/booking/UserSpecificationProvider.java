package com.example.booking_app.repository.booking;

import com.example.booking_app.model.Booking;
import com.example.booking_app.repository.SpecificationProvider;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import java.util.List;

@Component("user")
public class UserSpecificationProvider implements SpecificationProvider<Booking> {
    public static final String KEY = "user";

    @Override
    public String getKey() {
        return KEY;
    }

    @Override
    public Specification<Booking> getSpecification(List<String> userIds) {
        return (root, query, cb) -> {
            CriteriaBuilder.In<Long> predicate = cb.in(root.get("user").get("id"));
            userIds.stream().map(Long::valueOf).forEach(predicate::value);
            return predicate;
        };
    }
}
