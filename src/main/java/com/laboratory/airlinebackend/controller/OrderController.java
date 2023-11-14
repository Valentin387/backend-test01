package com.laboratory.airlinebackend.controller;
import com.laboratory.airlinebackend.controller.DTO.RegisterRequestOrder;
import com.laboratory.airlinebackend.repository.FlightRepository;
import com.laboratory.airlinebackend.repository.OrderRepository;
import com.laboratory.airlinebackend.repository.SeatRepository;
import com.laboratory.airlinebackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/order")
@CrossOrigin("http://localhost:5173")
@RequiredArgsConstructor
public class OrderController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/findPassenger/{DNI}")
    public ResponseEntity<?> findPassenger(@PathVariable String DNI){
        try{
            var passenger = userRepository.findByDNI(DNI);
            return ResponseEntity.ok(passenger);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("That passenger is not a registered user " + e.getMessage());
        }
    }


    @PostMapping("/purchase")
    public ResponseEntity<?> purchase(
            @RequestBody RegisterRequestOrder requestNewOrder
    ){
        try{

            //1. save the passengers in tblPassenger

            //2. use the Shopping Cart ID to get the list of seats
            //for each seat, you assign the respective passenger's ID randomly
            // according to their class and then update the tblFlight's available seats field

            //3. you update the tblCard simulating the payment process

            //4. save the order's info in tblOrder

            //5. crear nuevo shopping cart y asignarlo al usuario

            //6. mandar e-mail con la info de la compra a cada pasajero, en cada vuelo



            return ResponseEntity.ok("Purchase carried out successfully");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error during the payment process " + e.getMessage());
        }
    }


}
