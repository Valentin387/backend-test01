package com.laboratory.airlinebackend.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "tblSeat")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ID;

    private String state;

    @Column(length = 50)
    private String seatClass;

    private int flightId;
    private int passengerId;
    private char letter;
    private int number;
    private boolean hasExtraSpace;
}
