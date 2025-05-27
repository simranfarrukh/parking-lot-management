package com.simranfarrukh.parking_lot.controller;

import com.simranfarrukh.parking_lot.model.ParkingSpot;
import com.simranfarrukh.parking_lot.model.Vehicle;
import com.simranfarrukh.parking_lot.service.ParkingLotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000") // Allows frontend running on localhost to access the backend
@RestController
@RequestMapping("/parking")
public class ParkingController {

    /**
     * Service layer to handle business logic
     */
    private final ParkingLotService parkingLotService;

    public ParkingController(ParkingLotService parkingLotService) {
        this.parkingLotService = parkingLotService;
    }

    /**
     * Allocates a parking spot to a vehicle
     */
    @PostMapping("/allocate")
    public ResponseEntity<?> allocateSpot(@RequestBody Vehicle vehicle) {
        return parkingLotService.allocateSpot(vehicle);
    }

    /**
     * Fetches all available parking spots
     */
    @GetMapping("/available-spots")
    public ResponseEntity<List<ParkingSpot>> getAvailableSpots() {
        return ResponseEntity.ok(parkingLotService.getAvailableSpots());
    }

    /**
     * Retrieves vehicles currently in the waiting queue
     */
    @GetMapping("/waiting-queue")
    public ResponseEntity<List<Vehicle>> getWaitingQueue() {
        return ResponseEntity.ok(parkingLotService.getWaitingQueue());
    }

    /**
     * Deallocates a spot & assigns next vehicle
     */
    @DeleteMapping("/deallocate")
    public ResponseEntity<?> deallocateSpot() {
        boolean success = parkingLotService.deallocateAndAssign();
        return success ? ResponseEntity.ok("Deallocation successful.") : ResponseEntity.status(404).body("No occupied spots available.");
    }

    /**
     * Resets the parking database
     */
    @DeleteMapping("/reset")
    public ResponseEntity<?> resetDatabase() {
        parkingLotService.resetDatabase();
        return ResponseEntity.ok("Database successfully reset!");
    }
}