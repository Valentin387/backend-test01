package com.laboratory.airlinebackend.controller;
import com.laboratory.airlinebackend.controller.DTO.FlightState;
import com.laboratory.airlinebackend.controller.DTO.RegisterRequestFlight;
import com.laboratory.airlinebackend.controller.DTO.SeatState;
import com.laboratory.airlinebackend.controller.service.SeatCreatorService;
import com.laboratory.airlinebackend.model.Flight;
import com.laboratory.airlinebackend.model.Offer;
import com.laboratory.airlinebackend.model.Seat;
import com.laboratory.airlinebackend.repository.FlightRepository;
import com.laboratory.airlinebackend.repository.OfferRepository;
import com.laboratory.airlinebackend.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import java.util.*;


@RestController
@RequestMapping("/api/flight")
@CrossOrigin("http://localhost:5173")
@RequiredArgsConstructor
public class FlightController {

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private SeatCreatorService seatCreatorService;

    @Autowired
    private OfferRepository offerRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createNewFlight(
            @RequestBody RegisterRequestFlight requestNewFlight
    ){
        var currentDate = new java.util.Date();
        if (requestNewFlight.getFlightDate().before(currentDate)) {
            return ResponseEntity.badRequest().body("Flight date must be after current date");
        }
        try{
            int assignedSeats;
            int firstClassSeatsQuantity;
            int economicClassSeatsQuantity;
            int firstClassSeatRows;
            int economicClassSeatRows;
            //System.out.println(requestNewFlight);

            if (requestNewFlight.isInternational()) {
                //International
                assignedSeats = 250;
                firstClassSeatsQuantity = 50;
                firstClassSeatRows = 9; //round up firstClassSeatsQuantity/6
                economicClassSeatsQuantity = 200;
                economicClassSeatRows = 34; //round up economicClassSeatsQuantity/6
            } else {
                //National
                assignedSeats = 150;
                firstClassSeatsQuantity = 25;
                firstClassSeatRows = 5; //round up firstClassSeatsQuantity/6
                economicClassSeatsQuantity = 125;
                economicClassSeatRows = 21; //round up economicClassSeatsQuantity/6
            }
            var flight = Flight.builder()
                .flightDate(requestNewFlight.getFlightDate())
                .origin(requestNewFlight.getOrigin())
                .destination(requestNewFlight.getDestination())
                .flightDuration(requestNewFlight.getFlightDuration())
                .arrivalDate(requestNewFlight.getArrivalDate())
                .costByPerson(requestNewFlight.getCostByPerson())
                .isInternational(requestNewFlight.isInternational())
                .state(FlightState.ON_TIME.toString())
                .availableSeats(assignedSeats)
                .build();

            // Check for valid offers
            List<Offer> validOffers = offerRepository.findValidOffers(
                    flight.getOrigin(),
                    flight.getDestination(),
                    flight.getFlightDate()
            );

            if (validOffers.size() > 0) {
                flight.setCostByPersonOffer(
                        // Multiply by 100 to keep two decimal places, then round, and divide by 100
                        Math.round(
                                (flight.getCostByPerson() - ( validOffers.get(0).getDiscount()/100 * flight.getCostByPerson() ))
                                * 100.0
                        )/100.0
                );
            }
            flightRepository.save(flight);

            //now let's create the seats for this flight
            seatCreatorService.create_seats(
                    //assignedSeats,
                    firstClassSeatsQuantity,
                    firstClassSeatRows,
                    economicClassSeatsQuantity,
                    economicClassSeatRows,
                    flight);

            return ResponseEntity.ok("Flight created succesfully \n" +
                    "added economic class seats: " + economicClassSeatsQuantity + "\n" +
                    "added first class seats: " + firstClassSeatsQuantity);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating flight");
        }

    }

    @GetMapping("/list/active")
    public ResponseEntity<?> getActiveFlights(){
        try{
            List<Flight> flights = flightRepository.getOnTimeFlights();
            return ResponseEntity.ok(flights);
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Error getting active flights");
        }
    }

    /*
    //Only use this endpoint once!
    private Map<String, String> cityToCountryMap;
    private Random random;
    int count = 1;
    @GetMapping("/populate-the-database")
    public ResponseEntity<?> populateDatabaseWithFlightsAndSeats(){
        try{
            cityToCountryMap = new HashMap<>();
            cityToCountryMap.put("Madrid", "Spain");
            cityToCountryMap.put("Londres", "United Kingdom");
            cityToCountryMap.put("New York", "United States");
            cityToCountryMap.put("Buenos Aires", "Argentina");
            cityToCountryMap.put("Miami", "United States");
            cityToCountryMap.put("Pereira", "Colombia");
            cityToCountryMap.put("Bogotá", "Colombia");
            cityToCountryMap.put("Medellín", "Colombia");
            cityToCountryMap.put("Cali", "Colombia");
            cityToCountryMap.put("Cartagena", "Colombia");
            random = new Random();
            int assignedSeats;
            int firstClassSeatsQuantity;
            int economicClassSeatsQuantity;
            int firstClassSeatRows;
            int economicClassSeatRows;

            List<String> cities = new ArrayList<>(cityToCountryMap.keySet());
            for (String origin : cities){
                for (String destination : cities){
                    if (!origin.equals(destination)){
                        String country1 = cityToCountryMap.get(origin);
                        String country2 = cityToCountryMap.get(destination);
                        boolean isInternational = !country1.equals(country2);

                        if (isInternational) {
                            //International
                            assignedSeats = 250;
                            firstClassSeatsQuantity = 50;
                            firstClassSeatRows = 9; //round up firstClassSeatsQuantity/6
                            economicClassSeatsQuantity = 200;
                            economicClassSeatRows = 34; //round up economicClassSeatsQuantity/6
                        } else {
                            //National
                            assignedSeats = 150;
                            firstClassSeatsQuantity = 25;
                            firstClassSeatRows = 5; //round up firstClassSeatsQuantity/6
                            economicClassSeatsQuantity = 125;
                            economicClassSeatRows = 21; //round up economicClassSeatsQuantity/6
                        }
                        //flight date
                        int year = 2024;
                        int dayOfYear = random.nextInt(366);
                        long millisInDay = 24 * 60 * 60 * 1000;
                        long flightDateMillis = new Date(year - 1900, 0, 1).getTime() + dayOfYear * millisInDay;
                        //arrival date
                        int maxHours = 24;
                        int hoursToAdd = random.nextInt(maxHours);
                        long arrivalDateMillis = flightDateMillis + hoursToAdd * 60 * 60 * 1000;
                        //duration
                        long durationMillis = arrivalDateMillis - flightDateMillis;
                        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Assuming you want the duration in UTC time.

                        var flight = Flight.builder()
                                .flightDate(new Date(flightDateMillis))
                                .origin(origin)
                                .destination(destination)
                                .flightDuration(dateFormat.format(new Date(durationMillis)))
                                .arrivalDate(new Date(arrivalDateMillis))
                                .costByPerson(Math.round((100 + random.nextDouble() * 400)*100.0)/100.0)
                                .isInternational(isInternational)
                                .state(FlightState.ON_TIME.toString())
                                .availableSeats(assignedSeats)
                                .build();

                        System.out.println(count + ". Origin: "+ origin + " Destination: " + destination +
                                " isInternational: " + isInternational + " assignedSeats: " + assignedSeats +
                                "flightDate: " + flight.getFlightDate() + " arrivalDate: " + flight.getArrivalDate()
                                + "duration: " + flight.getFlightDuration() + "cost: " + flight.getCostByPerson());
                        flightRepository.save(flight);
                        //now let's create the seats for this flight
                        seatCreatorService.create_seats(
                                //assignedSeats,
                                firstClassSeatsQuantity,
                                firstClassSeatRows,
                                economicClassSeatsQuantity,
                                economicClassSeatRows,
                                flight);
                    count++;
                    }
                }
            }


            return ResponseEntity.ok("Database populated succesfully");
        }catch (Exception e) {
            return ResponseEntity.badRequest().body("Error populating database");
        }
    }
    */

}
