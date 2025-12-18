package LLD.ParkingSystem.strategy.parking;

import java.util.List;
import java.util.Optional;

import LLD.ParkingSystem.Vehicle.Vehicle;
import LLD.ParkingSystem.entities.ParkingFloor;

public class NearestFirstStrategy implements ParkingSpot {

    @Override
    public Optional<LLD.ParkingSystem.entities.ParkingSpot> findSpot(List<ParkingFloor> floors, Vehicle vehicle) 
    {
        Optional<LLD.ParkingSystem.entities.ParkingSpot> bestSpot=Optional.empty();
        for(ParkingFloor floor:floors)
        {
           Optional<LLD.ParkingSystem.entities.ParkingSpot> spot= floor.findAvailableSpot(vehicle);
           if(spot.isPresent())
           {
            bestSpot=spot;;
            break;
           }
        }
        return bestSpot;
    }
    
}
