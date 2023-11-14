package com.laboratory.airlinebackend.repository;

import com.laboratory.airlinebackend.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
}
