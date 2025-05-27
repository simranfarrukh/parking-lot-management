package com.simranfarrukh.parking_lot.repository;

import com.simranfarrukh.parking_lot.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {}