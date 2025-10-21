package com.example.booking_app.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SpecificationProviderManager<T> {


    private final Map<String, SpecificationProvider<T>> providers;

    @Autowired
    public SpecificationProviderManager(List<SpecificationProvider<?>> providerList) {
        this.providers = providerList.stream()
            .map(p -> (SpecificationProvider<T>) p)
            .collect(Collectors.toMap(SpecificationProvider::getKey, p -> p)); // CHANGED
    }

    public Specification<T> getSpecification(String key, List<String> values) {
        if (!providers.containsKey(key)) {
            throw new IllegalArgumentException("No provider found for key: " + key);
        }
        return providers.get(key).getSpecification(values);
    }
}
