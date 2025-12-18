package LLD.ParkingSystem;

import java.util.Optional;

import LLD.ParkingSystem.Vehicle.Bike;
import LLD.ParkingSystem.Vehicle.Car;
import LLD.ParkingSystem.Vehicle.Truck;
import LLD.ParkingSystem.Vehicle.Vehicle;
import LLD.ParkingSystem.Vehicle.VehicleSize;
import LLD.ParkingSystem.entities.ParkingFloor;
import LLD.ParkingSystem.entities.ParkingSpot;
import LLD.ParkingSystem.entities.ParkingTicket;
import LLD.ParkingSystem.strategy.fee.VehicleBasedFeeStrategy;

public class ParkingLotDemo {
    public static void main(String[] args)
    {
        ParkingLot parkingLot=ParkingLot.getInstance();
         ParkingFloor floor1 = new ParkingFloor(1);
        floor1.addSpot(new ParkingSpot("F1-S1", VehicleSize.SMALL));
        floor1.addSpot(new ParkingSpot("F1-M1", VehicleSize.MEDIUM));
        floor1.addSpot(new ParkingSpot("F1-L1", VehicleSize.LARGE));

        ParkingFloor floor2 = new ParkingFloor(2);
        floor2.addSpot(new ParkingSpot("F2-M1", VehicleSize.MEDIUM));
        floor2.addSpot(new ParkingSpot("F2-M2", VehicleSize.MEDIUM));

        parkingLot.addParkingFloor(floor1);
        parkingLot.addParkingFloor(floor2);
         parkingLot.setFeeStrategy(new VehicleBasedFeeStrategy());
           System.out.println("\n--- Vehicle Entries ---");
        floor1.displayAvailibility();
        floor2.displayAvailibility();

        Vehicle bike = new Bike("B-123");
        Vehicle car = new Car("C-456");
        Vehicle truck = new Truck("T-789");

        Optional<ParkingTicket> bikeTicketOpt = parkingLot.parkVehicle(bike);

        Optional<ParkingTicket> carTicketOpt = parkingLot.parkVehicle(car);

        Optional<ParkingTicket> truckTicketOpt = parkingLot.parkVehicle(truck);

        System.out.println("\n--- Availability after parking ---");
        floor1.displayAvailibility();
        floor2.displayAvailibility();

        // 3. Simulate another car entry (should go to floor 2)
        Vehicle car2 = new Car("C-999");
        Optional<ParkingTicket> car2TicketOpt = parkingLot.parkVehicle(car2);

        // 4. Simulate a vehicle entry that fails (no available spots)
        Vehicle bike2 = new Bike("B-000");
        Optional<ParkingTicket> failedBikeTicketOpt = parkingLot.parkVehicle(bike2);

        // 5. Simulate vehicle exits and fee calculation
        System.out.println("\n--- Vehicle Exits ---");
        if(carTicketOpt.isPresent())
        {
            Optional<Double> fee = parkingLot.unparkVehicle(carTicketOpt.get().getTicketId());
             fee.ifPresent(f -> System.out.printf("Car C-456 unparked. Fee: $%.2f\n", f));
        }
         System.out.println("\n--- Availability after one car leaves ---");
  floor1.displayAvailibility();
        floor2.displayAvailibility();
    }
}
