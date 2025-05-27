package com.simranfarrukh.parking_lot.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ParkingSpot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean occupied; // Indicates if the spot is taken

    private Long vehicleId; // Vehicle occupying this spot (if needed)
    private String vehicleLicensePlate; // License plate of the occupying vehicle
}
