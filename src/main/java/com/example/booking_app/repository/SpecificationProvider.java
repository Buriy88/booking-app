package com.example.booking_app.repository;

import org.springframework.data.jpa.domain.Specification;
import java.util.List;

public interface SpecificationProvider<T> {
    String getKey();

    Specification<T> getSpecification(List<String> values);
}
