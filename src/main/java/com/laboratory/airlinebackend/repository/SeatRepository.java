package com.laboratory.airlinebackend.repository;

import com.laboratory.airlinebackend.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {
}
