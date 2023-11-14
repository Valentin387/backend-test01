package com.laboratory.airlinebackend.repository;

import com.laboratory.airlinebackend.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Query("SELECT f FROM tblFlight f WHERE f.origin = :origin and f.destination = :destination " +
            "and f.flightDate = :flightDate and " +
            "f.availableSeats >= :numPassengers and f.state = 'ON_TIME'")
    List<Flight> findFlightsByParameters(String origin, String destination, Date flightDate, int numPassengers);

    @Query("SELECT f FROM tblFlight f WHERE f.destination = :destinationName and f.state = 'ON_TIME'")
    List<Flight> getOnTimeFlightsForDestination(String destinationName);

    @Query("SELECT f FROM tblFlight f WHERE f.state = 'ON_TIME'")
    List<Flight> getOnTimeFlights();

    @Modifying
    @Query("UPDATE tblFlight f SET f.costByPersonOffer = (f.costByPerson - (:discount*f.costByPerson)) " +
            "WHERE f.origin = :origin " +
            "AND f.destination = :destination " +
            "AND f.flightDate <= :validDateRange " +
            "AND f.state = 'ON_TIME' ")
    void updateCostByPersonOffer(double discount, String origin, String destination, Date validDateRange);

    @Modifying
    @Query("UPDATE tblFlight f SET f.costByPersonOffer = 0.0 " +
            "WHERE f.origin = :origin " +
            "AND f.destination = :destination " +
            "AND f.flightDate <= :validDateRange " +
            "AND f.state = 'ON_TIME' ")
    void resetCostByPersonOffer(String origin, String destination, Date validDateRange);
}
