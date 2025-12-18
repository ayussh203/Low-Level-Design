package LLD.ParkingSystem.entities;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import LLD.ParkingSystem.Vehicle.Vehicle;
import LLD.ParkingSystem.Vehicle.VehicleSize;

public class ParkingFloor {
    private final int floorNumber;
    private final Map<String, ParkingSpot> parkingSpots; // spotId to ParkingSpot mapping

    public ParkingFloor(int floorNumber)
    {
        this.floorNumber = floorNumber;
        this.parkingSpots = new ConcurrentHashMap<>();
    }

    public void addSpot(ParkingSpot spot)
    {
        parkingSpots.put(spot.getSpotId(),spot);
    }
    public synchronized Optional<ParkingSpot> findAvailableSpot(Vehicle vehicle)
    {
        return parkingSpots.values().stream()
        
        .filter(spot->!spot.isOccupied() && spot.canFitVehicle(vehicle))
        .sorted(Comparator.comparing(ParkingSpot::getSpotSize))
        .findFirst();
    }
    public void displayAvailibility()
    {
        Map<VehicleSize,Long> availableCounts=new HashMap<>();
        for(ParkingSpot spot:parkingSpots.values())
        {
            if(!spot.isOccupied())
            {
                VehicleSize size=spot.getSpotSize();
                availableCounts.put(size,availableCounts.getOrDefault(size, 0L)+1);
            }
        }
        for(VehicleSize size:VehicleSize.values())
        {
            long count=availableCounts.getOrDefault(size, 0L);
            System.out.println("Available spots of size "+size+" : "+count);
        }
    }
    
}
