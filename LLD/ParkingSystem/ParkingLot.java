package LLD.ParkingSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import LLD.ParkingSystem.Vehicle.Vehicle;
import LLD.ParkingSystem.entities.ParkingFloor;
import LLD.ParkingSystem.entities.ParkingTicket;
import LLD.ParkingSystem.strategy.fee.FeeStrategy;
import LLD.ParkingSystem.strategy.fee.FlatRateFeeStrategy;
import LLD.ParkingSystem.strategy.parking.BestFitStrategy;
import LLD.ParkingSystem.strategy.parking.ParkingSpot;

public class ParkingLot {
    private static ParkingLot instance;
    private final Map<String,ParkingTicket> activeTickets;
    private final List<ParkingFloor> parkingFloors=new ArrayList<>();
    private FeeStrategy feeStrategy;
    private ParkingSpot parkingStrategy;

    private ParkingLot()
    {
        this.feeStrategy=new FlatRateFeeStrategy();
        this.parkingStrategy=new BestFitStrategy();
        this.activeTickets = new ConcurrentHashMap<>();

    }
    public static synchronized ParkingLot getInstance()
    {
        if(instance==null)
        {
            instance=new ParkingLot();
        }
        return instance;
    }
    public void addParkingFloor(ParkingFloor floor)
    {
        parkingFloors.add(floor);
    }
    public void setFeeStrategy(FeeStrategy feeStrategy)
    {
        this.feeStrategy=feeStrategy;
    }
    public void setParkingStrategy(ParkingSpot parkingStrategy)
    {
        this.parkingStrategy=parkingStrategy;
    }
    public Optional<ParkingTicket> parkVehicle(LLD.ParkingSystem.Vehicle.Vehicle vehicle)
    {
        var spot=parkingStrategy.findSpot(parkingFloors, vehicle);
        if(spot.isEmpty())
        {
            return Optional.empty();
        }
        var parkingSpot=spot.get();
        parkingSpot.parkVehicle(vehicle);
        var ticket=new ParkingTicket(vehicle, parkingSpot);
        activeTickets.put(ticket.getTicketId(), ticket);
         System.out.printf("%s parked at %s. Ticket: %s\n", vehicle.getLicensePlate(), spot.get().getSpotId(), ticket.getTicketId());
        return Optional.of(ticket);
    }
    public Optional<Double> unparkVehicle(String licenseNumber)
    {
        ParkingTicket ticket=activeTickets.remove(licenseNumber);
        if(ticket==null)
        {
              System.out.println("Ticket not found");
            return Optional.empty();
        }
        ticket.setExitTime();
        ticket.getSpot().unparkVehicle();
        Double parkingFee=feeStrategy.CalculateFee(ticket);
        return Optional.of(parkingFee);

    }
}
