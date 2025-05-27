package com.simranfarrukh.parking_lot.repository;

import com.simranfarrukh.parking_lot.model.ParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long> {

    // Fetch all available spots
    List<ParkingSpot> findByOccupiedFalse();

    // Fetch all occupied spots
    List<ParkingSpot> findByOccupiedTrue();

    // Count number of occupied spots
    long countByOccupiedTrue();

    // Get first occupied spot
    Optional<ParkingSpot> findFirstByOccupiedTrueOrderByIdAsc();
}