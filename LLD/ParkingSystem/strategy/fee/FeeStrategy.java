package LLD.ParkingSystem.strategy.fee;

import LLD.ParkingSystem.entities.ParkingTicket;

public interface FeeStrategy {
        double CalculateFee(ParkingTicket ticket);
    
} 
