package com.laboratory.airlinebackend.controller;
import com.laboratory.airlinebackend.model.Flight;
import com.laboratory.airlinebackend.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Date;


@RestController
@RequestMapping("/api/search")
@CrossOrigin("http://localhost:5173")
@RequiredArgsConstructor
public class SearchController {

    @Autowired
    FlightRepository flightRepository;

    @GetMapping("/parametrized-search")
    public ResponseEntity<?> searchFlights(
            @RequestParam  String origin,
            @RequestParam  String destination,
            @RequestParam  @DateTimeFormat(pattern = "yyyy-MM-dd") Date flightDate,
            //@RequestParam  @DateTimeFormat(pattern = "yyyy-MM-dd") Date returnDate,
            @RequestParam  int numPassengers) {
        // Your flight search logic here
        /*
        System.out.println("Origin: " + origin);
        System.out.println("Destination: " + destination);
        System.out.println("Departure Date: " + departureDate);
        //System.out.println("Return Date: " + returnDate);
        System.out.println("Number of Passengers: " + numPassengers);
        */
        // Use the parameters (origin, destination, departureDate, returnDate, numPassengers) to perform the search.
        try {
            List<Flight> flights = flightRepository.findFlightsByParameters(origin, destination, flightDate, numPassengers);
            /*if (flights.isEmpty()) {
                System.out.println("no matches");
                return ResponseEntity.badRequest().body("No flights found");
            }else{
                System.out.println(flights.size() + " matches");
            }
            for (Flight flight : flights) {
                System.out.println(flight);
            }
            System.out.println("finished");*/
            return ResponseEntity.ok(flights);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error searching flights " + e.getMessage() );
        }
        // Return the search results in the ResponseEntity.

    }

    @GetMapping("/{destinationName}")
    public ResponseEntity<List<Flight>> getOnTimeFlightsForDestination(@PathVariable String destinationName) {
        // Use the destinationName to retrieve ON_TIME flights for that destination
        try {
            List<Flight> onTimeFlights = flightRepository.getOnTimeFlightsForDestination(destinationName);

            return ResponseEntity.ok(onTimeFlights);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }

    }


}
