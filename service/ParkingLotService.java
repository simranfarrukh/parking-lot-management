package com.simranfarrukh.parking_lot.service;

import com.simranfarrukh.parking_lot.model.ParkingSpot;
import com.simranfarrukh.parking_lot.model.Vehicle;
import com.simranfarrukh.parking_lot.repository.ParkingSpotRepository;
import com.simranfarrukh.parking_lot.repository.VehicleRepository;
import jakarta.annotation.PreDestroy;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityManager;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

@Service
@RequiredArgsConstructor
public class ParkingLotService {
    private final ParkingSpotRepository parkingSpotRepository;
    private final VehicleRepository vehicleRepository;
    private final Queue<Vehicle> waitingQueue = new LinkedList<>();
    private final EntityManager entityManager;

    /**
     * Deletes all data and resets the parking database
     */
    @PreDestroy
    @Transactional
    public void resetDatabase() {
        entityManager.createNativeQuery("DELETE FROM parking_spot").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE parking_spot ALTER COLUMN id RESTART WITH 1").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM vehicle").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE vehicle ALTER COLUMN id RESTART WITH 1").executeUpdate();
        waitingQueue.clear(); // Clears the waiting queue
        initializeParkingSpots(); // Recreates initial parking spots
    }

    /**
     * Initializes parking spots (ensures there are at least 2)
     */
    private void initializeParkingSpots() {
        long existingSpots = parkingSpotRepository.count();
        if (existingSpots < 2) {
            for (int i = (int) existingSpots; i < 2; i++) {
                parkingSpotRepository.save(ParkingSpot.builder().occupied(false).vehicleId(null).build());
            }
        }
    }

    /**
     * Allocates a parking spot to a vehicle; adds to queue if spots are full
     */
    public ResponseEntity<?> allocateSpot(Vehicle vehicle) {
        vehicleRepository.save(vehicle);
        Optional<ParkingSpot> availableSpot = parkingSpotRepository.findByOccupiedFalse().stream().findFirst();
        if (availableSpot.isPresent()) {
            ParkingSpot spot = availableSpot.get();
            spot.setOccupied(true);
            spot.setVehicleId(vehicle.getId()); // Keep ID if needed
            spot.setVehicleLicensePlate(vehicle.getLicensePlate()); // Store license plate
            parkingSpotRepository.save(spot);
            return ResponseEntity.ok(spot);
        }
        waitingQueue.add(vehicle);
        return ResponseEntity.status(201).body("Parking full. Added to waiting queue.");
    }

    // Tracks last deallocated spot for round-robin logic
    private int lastDeallocatedIndex = 0;

    /**
     * Handles deallocation and assignment in round-robin fashion
     * If queue is empty, deallocates spots in order
     */
    public boolean deallocateAndAssign() {
        List<ParkingSpot> occupiedSpots = parkingSpotRepository.findByOccupiedTrue();

        if (!occupiedSpots.isEmpty()) {
            if (!waitingQueue.isEmpty()) {
                // Get next spot for deallocation using round-robin method
                ParkingSpot spot = occupiedSpots.get(lastDeallocatedIndex % occupiedSpots.size());
                spot.setOccupied(false);
                spot.setVehicleId(null);
                spot.setVehicleLicensePlate(null);
                parkingSpotRepository.save(spot);

                // Assign next vehicle from the queue
                Vehicle nextVehicle = waitingQueue.poll();
                if (nextVehicle != null) {
                    spot.setOccupied(true);
                    spot.setVehicleId(nextVehicle.getId());
                    spot.setVehicleLicensePlate(nextVehicle.getLicensePlate());
                    parkingSpotRepository.save(spot);
                }
            } else {
                // Queue is empty, start deallocating occupied spots in a round-robin fashion
                ParkingSpot spot = occupiedSpots.get(lastDeallocatedIndex % occupiedSpots.size());
                spot.setOccupied(false);
                spot.setVehicleId(null);
                spot.setVehicleLicensePlate(null);
                parkingSpotRepository.save(spot);
            }

            // Move to the next spot for next time
            lastDeallocatedIndex++;

            return true;
        }
        return false;
    }

    /**
     * Fetch all available parking spots
     */
    public List<ParkingSpot> getAvailableSpots() {
        return parkingSpotRepository.findByOccupiedFalse();
    }

    /**
     * Retrieves vehicles in waiting queue
     */
    public List<Vehicle> getWaitingQueue() {
        return new LinkedList<>(waitingQueue);
    }
}