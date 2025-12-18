package LLD.ParkingSystem.entities;

import java.util.Date;
import java.util.UUID;

import LLD.ParkingSystem.Vehicle.Vehicle;

public class ParkingTicket {
    private final String ticketId;
    private final Vehicle vehicle;
    private final  ParkingSpot spot;
    private final long entryTime;
    private long exitTime;

    public ParkingTicket( Vehicle vehicle,ParkingSpot spot) {
        this.ticketId = UUID.randomUUID().toString();
        this.vehicle = vehicle;
        this.spot = spot;
        this.entryTime = new Date().getTime();
    }
    public String getTicketId() {
        return ticketId;
    }
    public Vehicle getVehicle() {
        return vehicle;
    }
    public ParkingSpot getSpot() {
        return spot;
    }
    public long getEntryTime() {
        return entryTime;
    }   
    public long getExitTime() {
        return exitTime;
    }

    public void setExitTime(){
        this.exitTime=new Date().getTime();
    }
}
