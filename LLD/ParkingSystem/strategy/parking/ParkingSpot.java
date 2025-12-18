package LLD.ParkingSystem.strategy.parking;

import java.util.List;
import java.util.Optional;

import LLD.ParkingSystem.Vehicle.Vehicle;
import LLD.ParkingSystem.entities.ParkingFloor;

public interface ParkingSpot  {

    Optional<LLD.ParkingSystem.entities.ParkingSpot> findSpot(List<ParkingFloor> floors, Vehicle vehicle);

    
} 
