package LLD.ParkingSystem.strategy.parking;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import LLD.ParkingSystem.Vehicle.Vehicle;
import LLD.ParkingSystem.entities.ParkingFloor;

public class FarthestFirstStrategy implements ParkingSpot{
    @Override
     public Optional<LLD.ParkingSystem.entities.ParkingSpot> findSpot(List<ParkingFloor> floors, Vehicle vehicle) {
        List<ParkingFloor> reversedFloor=new ArrayList<>(floors);
        Collections.reverse(reversedFloor);
        for (ParkingFloor floor : reversedFloor) {
            Optional<LLD.ParkingSystem.entities.ParkingSpot> spot = floor.findAvailableSpot(vehicle);
            if (spot.isPresent()) {
                return spot;
            }
        }
        return Optional.empty();
    }
    
}
