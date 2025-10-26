package com.example.booking_app.repository;

import com.example.booking_app.model.Booking;
import com.example.booking_app.model.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;

public interface BookingRepository
    extends JpaRepository<Booking, Long>, JpaSpecificationExecutor<Booking> {

    @EntityGraph(attributePaths = {"user", "accommodation"})
    List<Booking> findAll(Specification<Booking> spec);

    List<Booking> searchBookingByUser(User user);
}
