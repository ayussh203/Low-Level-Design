package LLD.ParkingSystem.strategy.fee;

import java.util.Map;

import LLD.ParkingSystem.Vehicle.VehicleSize;

public class VehicleBasedFeeStrategy implements FeeStrategy {

     private static final Map<VehicleSize, Double> HOURLY_RATES = Map.of(
            VehicleSize.SMALL, 10.0,
            VehicleSize.MEDIUM, 20.0,
            VehicleSize.LARGE, 30.0
    );
    @Override
    public double CalculateFee(LLD.ParkingSystem.entities.ParkingTicket ticket) {
        long duration = ticket.getExitTime() - ticket.getEntryTime();
        long hours = (duration / (1000 * 60 * 60)) + 1;
        VehicleSize vehicleSize = ticket.getVehicle().getSize();
        double ratePerHour = HOURLY_RATES.getOrDefault(vehicleSize, 0.0);
        return hours * ratePerHour;
    }
    
}
