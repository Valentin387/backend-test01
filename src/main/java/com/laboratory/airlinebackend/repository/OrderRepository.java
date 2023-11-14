package com.laboratory.airlinebackend.repository;

import com.laboratory.airlinebackend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
