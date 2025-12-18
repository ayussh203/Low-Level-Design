package LLD.ParkingSystem.strategy.parking;

import java.util.List;
import java.util.Optional;

import LLD.ParkingSystem.Vehicle.Vehicle;
import LLD.ParkingSystem.entities.ParkingFloor;

public class BestFitStrategy implements ParkingSpot {

    @Override
    public Optional<LLD.ParkingSystem.entities.ParkingSpot> findSpot(List<ParkingFloor> floors, Vehicle vehicle) {
        // Implementation for Best Fit Strategy
           Optional<LLD.ParkingSystem.entities.ParkingSpot> bestSpot=Optional.empty();
           for(ParkingFloor floor:floors)
           {
            Optional<LLD.ParkingSystem.entities.ParkingSpot> spot=floor.findAvailableSpot(vehicle);
            if(spot.isPresent())
            {   if(bestSpot.isEmpty()){

            
                bestSpot=spot;}
                else if(spot.get().getSpotSize().ordinal()<bestSpot.get().getSpotSize().ordinal())
                {
                    bestSpot=spot;
                }
            }
           }
           return bestSpot;
    }
    
}
