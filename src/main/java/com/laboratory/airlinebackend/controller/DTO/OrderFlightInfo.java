package com.laboratory.airlinebackend.controller.DTO;

import com.laboratory.airlinebackend.model.Passenger;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderFlightInfo {

    private long ID;
    private double partialPrice;
    private double discountApplied;
    private List<Passenger> passengerList;
}
