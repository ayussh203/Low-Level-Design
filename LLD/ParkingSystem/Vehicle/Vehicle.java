package LLD.ParkingSystem.Vehicle;

public abstract class Vehicle {

    private final String licensePlate;
    private final VehicleSize size; 
    public Vehicle(String licensePlate, VehicleSize size) {
        this.licensePlate = licensePlate;
        this.size = size;
    }
    public String getLicensePlate() {
        return licensePlate;
    }
    public VehicleSize getSize() {
        return size;
    }
    
}
