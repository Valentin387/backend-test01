package com.laboratory.airlinebackend.controller.DTO;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestOrder {

    private long ID;

    @Column(length = 100)
    private String COD;

    private long userID;
    private long shoppingCartID;
    private long cardID;
    private double totalAmount;
    private java.util.Date Date;

    @Column(length = 100)
    private String paymentDetails;

    private List<OrderFlightInfo> orderFlightInfoList;



}
