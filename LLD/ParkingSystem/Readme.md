## Parking System – Low Level Design

---

### 1. Problem Statement

- **Goal**: Design and implement a scalable **Parking Lot System** that:
  - Supports **multiple floors** and **multiple spot sizes** (small, medium, large).
  - Supports **different vehicle types** (bike, car, truck) with size constraints.
  - Allocates and releases parking spots efficiently.
  - Calculates parking **fees** using **pluggable strategies**.
  - Can evolve to support different **parking policies** (nearest, farthest, best-fit) without changing core logic.

This implementation focuses on **extensibility**, **configurability**, and **clean separation of concerns**.

---

### 2.  Design Overview

- **Core domain entities**:
  - `Vehicle` hierarchy: `Vehicle` (abstract), `Bike`, `Car`, `Truck`, `VehicleSize`.
  - `ParkingSpot`: individual parking spot with size and occupancy.
  - `ParkingFloor`: group of `ParkingSpot`s for a floor.
  - `ParkingTicket`: represents a parking session (vehicle + spot + entry/exit times).
  - `ParkingLot`: **facade / orchestrator**, manages floors, active tickets, and strategies.

- **Strategy interfaces**:
  - `FeeStrategy`:
    - `FlatRateFeeStrategy`
    - `VehicleBasedFeeStrategy`
  - `ParkingSpot` (parking strategy interface):
    - `BestFitStrategy`
    - `NearestFirstStrategy`
    - `FarthestFirstStrategy`

- **Key patterns**:
  - **Singleton** for `ParkingLot`.
  - **Strategy Pattern** for:
    - Fee calculation (`FeeStrategy`).
    - Spot selection (`strategy.parking.ParkingSpot`).
  - **Composition**: `ParkingLot` composes strategies and entities instead of inheriting.
  - **Encapsulation** of concurrency in selective places (e.g., `ConcurrentHashMap`, `synchronized` methods).

---

### 3. Domain Model and Entities

#### 3.1 `Vehicle` hierarchy

```java
public abstract class Vehicle {
    private final String licensePlate;
    private final VehicleSize size;
    // getters...
}
```

- **Concrete types**:
  - `Bike` → `VehicleSize.SMALL`
  - `Car` → `VehicleSize.MEDIUM`
  - `Truck` → `VehicleSize.LARGE`

**Responsibilities**:
- Encapsulate basic **identity** (`licensePlate`) and **size category** (`VehicleSize`).
- Enforce vehicle-size mapping at construction time (e.g., a `Car` is always `MEDIUM`).

**Why an abstract `Vehicle` base class?**
- Future extension:
  - Add new vehicle types (`Bus`, `ElectricCar`, `Van`) without modifying parking logic.
- Shared behavior / properties (plate + size) live in one place.

**Tradeoffs**:
- Slightly more classes vs using enums/structs, but much better for **OO modeling** and **extensibility**.

---

#### 3.2 `VehicleSize` enum

```java
public enum VehicleSize {
    SMALL,
    MEDIUM,
    LARGE
}
```

- **Responsibility**:
  - Provide a type-safe representation of vehicle (and spot) sizes.
- Used by:
  - `Vehicle` to represent size.
  - `ParkingSpot` to represent spot capacity.
  - Strategies (e.g., fee strategies) to calculate different rates.

---

#### 3.3 `ParkingSpot` (entity)

```java
public class ParkingSpot {
    private final String spotId;
    private boolean isOccupied;
    private Vehicle parkedVehicle;
    private final VehicleSize spotSize;
    // constructors, getters, park/unpark, canFitVehicle...
}
```

**Responsibilities**:
- Represent a **single parking spot** with:
  - Unique `spotId` (e.g., `"F1-S1"`).
  - `spotSize` (SMALL, MEDIUM, LARGE).
  - Occupancy and the current `parkedVehicle` (if any).

**Key methods**:
- `boolean isOccupied()`: current occupancy.
- `synchronized boolean isAvailable()`: thread-safe availability query.
- `synchronized void parkVehicle(Vehicle vehicle)`:
  - Marks spot as occupied and remembers the `parkedVehicle`.
- `synchronized void unparkVehicle()`:
  - Clears the spot.
- `boolean canFitVehicle(Vehicle vehicle)`:
  - Encapsulates **size compatibility rules**:
    - SMALL vehicle → SMALL spot.
    - MEDIUM vehicle → MEDIUM spot.
    - LARGE vehicle → LARGE spot.

**Why keep size compatibility in `ParkingSpot`?**
- `ParkingSpot` is the authority on what it can accept.
- Encapsulation:
  - If the rules change (e.g., allow smaller vehicles in larger spots), we only update this method.

**Thread-safety considerations**:
- `parkVehicle` / `unparkVehicle` / `isAvailable` are `synchronized` to avoid race conditions when multiple threads try to park/unpark on the same spot.

---

#### 3.4 `ParkingFloor`

```java
public class ParkingFloor {
    private final int floorNumber;
    private final Map<String, ParkingSpot> parkingSpots;
    // addSpot, findAvailableSpot, displayAvailibility...
}
```

**Responsibilities**:
- Group and manage parking spots per floor.
- Provide a **floor-local search** for an appropriate spot for a given `Vehicle`.
- Provide basic aggregations (availability counts by `VehicleSize`).

**Key methods**:

- `void addSpot(ParkingSpot spot)`:
  - Registers a new spot, keyed by `spotId`.

- `synchronized Optional<ParkingSpot> findAvailableSpot(Vehicle vehicle)`:

```java
return parkingSpots.values().stream()
    .filter(spot -> !spot.isOccupied() && spot.canFitVehicle(vehicle))
    .sorted(Comparator.comparing(ParkingSpot::getSpotSize))
    .findFirst();
```

  - Filters spots:
    - Must not be occupied.
    - Must be able to fit the vehicle by size.
  - Sorts by `spotSize`:
    - Favors **smaller suitable spots first** (good for best-fit logic).
  - Returns the first match as an `Optional`.

- `void displayAvailibility()`:
  - Computes and prints the count of available spots per `VehicleSize` on this floor.

**Thread-safety**:
- `parkingSpots` is a `ConcurrentHashMap`.
- `findAvailableSpot` is `synchronized` to ensure consistent view and avoid concurrent conflicting allocations per floor.

---

#### 3.5 `ParkingTicket`

```java
public class ParkingTicket {
    private final String ticketId;
    private final Vehicle vehicle;
    private final ParkingSpot spot;
    private final long entryTime;
    private long exitTime;
    // getters, setExitTime()
}
```

**Responsibilities**:
- Represents a **parking session**, binding:
  - `vehicle`
  - `spot`
  - `entryTime`
  - `exitTime`
- Used by `FeeStrategy` to compute the fee.

**Key details**:
- `ticketId`:
  - Generated using a `UUID`, ensuring global uniqueness.
  - Used as key in `ParkingLot.activeTickets`.
- `entryTime` and `exitTime`:
  - `entryTime` set in constructor using current time.
  - `exitTime` set via `setExitTime()` on unpark.

**Why keep both vehicle and spot?**
- `vehicle`:
  - Needed for size-based fee strategies.
- `spot`:
  - Needed to unpark and potentially for location-based fee rules in the future.

---

### 4. Strategies

#### 4.1 Fee Strategy – `FeeStrategy` (Strategy Pattern)

```java
public interface FeeStrategy {
    double CalculateFee(ParkingTicket ticket);
}
```

- **Pattern**: **Strategy Pattern**
  - Different fee calculation algorithms are encapsulated behind a common interface.

##### 4.1.1 `FlatRateFeeStrategy`

```java
public class FlatRateFeeStrategy implements FeeStrategy {
    private static final double RATE_PER_HOUR = 10.0;
    @Override
    public double CalculateFee(ParkingTicket ticket) {
        long duration = ticket.getExitTime() - ticket.getEntryTime();
        long hours = (duration / (1000 * 60 * 60)) + 1;
        return hours * RATE_PER_HOUR;
    }
}
```

- **Logic**:
  - Compute parking duration in milliseconds and convert to hours.
  - Charges **10.0 per hour**, always rounding up to at least one hour.

**Tradeoffs**:
- Simple and uniform pricing.
- Not very realistic for different vehicle sizes or peak/off-peak times.

##### 4.1.2 `VehicleBasedFeeStrategy`

```java
public class VehicleBasedFeeStrategy implements FeeStrategy {
    private static final Map<VehicleSize, Double> HOURLY_RATES = Map.of(
        VehicleSize.SMALL, 10.0,
        VehicleSize.MEDIUM, 20.0,
        VehicleSize.LARGE, 30.0
    );
    @Override
    public double CalculateFee(ParkingTicket ticket) {
        long duration = ticket.getExitTime() - ticket.getEntryTime();
        long hours = (duration / (1000 * 60 * 60)) + 1;
        VehicleSize vehicleSize = ticket.getVehicle().getSize();
        double ratePerHour = HOURLY_RATES.getOrDefault(vehicleSize, 0.0);
        return hours * ratePerHour;
    }
}
```

- **Logic**:
  - Similar to flat-rate, but `RATE_PER_HOUR` depends on `VehicleSize`.
- **Advantages**:
  - More realistic pricing for different vehicle types.
  - Still very simple to configure using a `Map`.

**Why Strategy here?**
- Changing pricing rules does **not** require changes in `ParkingLot`.
- We can add:
  - Time-of-day strategies.
  - Weekend vs weekday.
  - Progressive pricing.

---

#### 4.2 Parking Strategy – `strategy.parking.ParkingSpot` (Strategy Pattern)

```java
public interface ParkingSpot {
    Optional<LLD.ParkingSystem.entities.ParkingSpot> findSpot(List<ParkingFloor> floors, Vehicle vehicle);
}
```

- **Responsibility**:
  - Define the **policy for choosing a spot** among all floors given a vehicle.

##### 4.2.1 `NearestFirstStrategy`

```java
public class NearestFirstStrategy implements ParkingSpot {
    @Override
    public Optional<LLD.ParkingSystem.entities.ParkingSpot> findSpot(List<ParkingFloor> floors, Vehicle vehicle) {
        Optional<LLD.ParkingSystem.entities.ParkingSpot> bestSpot = Optional.empty();
        for (ParkingFloor floor : floors) {
            Optional<LLD.ParkingSystem.entities.ParkingSpot> spot = floor.findAvailableSpot(vehicle);
            if (spot.isPresent()) {
                bestSpot = spot;
                break;
            }
        }
        return bestSpot;
    }
}
```

- **Behavior**:
  - Iterate floors in given order (e.g., lowest floor first).
  - Return the **first floor** that has a suitable spot.
- **Use case**:
  - Users prefer the nearest/lowest floor to exits or entry.

##### 4.2.2 `FarthestFirstStrategy`

```java
public class FarthestFirstStrategy implements ParkingSpot {
    @Override
    public Optional<LLD.ParkingSystem.entities.ParkingSpot> findSpot(List<ParkingFloor> floors, Vehicle vehicle) {
        List<ParkingFloor> reversedFloor = new ArrayList<>(floors);
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
```

- **Behavior**:
  - Start from the **top (farthest)** floor and go downwards.
- **Use case**:
  - Keep lower floors free for premium customers, short-term stays, etc.

##### 4.2.3 `BestFitStrategy`

```java
public class BestFitStrategy implements ParkingSpot {
    @Override
    public Optional<LLD.ParkingSystem.entities.ParkingSpot> findSpot(List<ParkingFloor> floors, Vehicle vehicle) {
        Optional<LLD.ParkingSystem.entities.ParkingSpot> bestSpot = Optional.empty();
        for (ParkingFloor floor : floors) {
            Optional<LLD.ParkingSystem.entities.ParkingSpot> spot = floor.findAvailableSpot(vehicle);
            if (spot.isPresent()) {
                if (bestSpot.isEmpty()) {
                    bestSpot = spot;
                } else if (spot.get().getSpotSize().ordinal() < bestSpot.get().getSpotSize().ordinal()) {
                    bestSpot = spot;
                }
            }
        }
        return bestSpot;
    }
}
```

- **Behavior**:
  - Checks all floors.
  - Among all suitable spots, picks the one with the **smallest possible size** that fits the vehicle (using `enum.ordinal()` comparison).
- **Goal**:
  - Optimize utilization by reserving larger spots for big vehicles.

**Why use a strategy interface here?**
- `ParkingLot` doesn’t know or care **how** the spot is chosen.
- Swapping the algorithm is just:

```java
parkingLot.setParkingStrategy(new NearestFirstStrategy());
// or
parkingLot.setParkingStrategy(new FarthestFirstStrategy());
// or
parkingLot.setParkingStrategy(new BestFitStrategy());
```

No changes to the rest of the codebase.

---

### 5. `ParkingLot` – Orchestrator / Facade

```java
public class ParkingLot {
    private static ParkingLot instance;
    private final Map<String, ParkingTicket> activeTickets;
    private final List<ParkingFloor> parkingFloors = new ArrayList<>();
    private FeeStrategy feeStrategy;
    private ParkingSpot parkingStrategy;
    // constructor, getInstance, addParkingFloor, setFeeStrategy, setParkingStrategy, parkVehicle, unparkVehicle...
}
```

#### 5.1 Singleton Pattern

- `private static ParkingLot instance;`
- `private ParkingLot() { ... }`
- `public static synchronized ParkingLot getInstance() { ... }`

**Why Singleton?**
- Models the real-world concept of a **single parking lot system instance** in this demo.
- Simplifies access for the console `ParkingLotDemo`.

**Thread-safety**:
- `getInstance()` is `synchronized` to ensure only one instance is created in a concurrent environment.

*Note:* For large-scale systems, you might avoid a strict singleton and use dependency injection instead.

#### 5.2 Fields and Composition

- `activeTickets`:
  - `ConcurrentHashMap<String, ParkingTicket>` keyed by ticket ID.
  - Tracks ongoing parking sessions.
- `parkingFloors`:
  - In-memory list of `ParkingFloor` objects.
- `feeStrategy`:
  - Current **pluggable** strategy to compute fees.
- `parkingStrategy`:
  - Current **pluggable** strategy to pick spots.

#### 5.3 Core Methods

##### `addParkingFloor(ParkingFloor floor)`
- Adds a new floor to the system.

##### `setFeeStrategy(FeeStrategy feeStrategy)`
- Switches pricing strategy at runtime.

##### `setParkingStrategy(ParkingSpot parkingStrategy)`
- Switches spot allocation policy at runtime.

##### `Optional<ParkingTicket> parkVehicle(Vehicle vehicle)`

```java
var spot = parkingStrategy.findSpot(parkingFloors, vehicle);
if (spot.isEmpty()) {
    return Optional.empty();
}
var parkingSpot = spot.get();
parkingSpot.parkVehicle(vehicle);
var ticket = new ParkingTicket(vehicle, parkingSpot);
activeTickets.put(ticket.getTicketId(), ticket);
System.out.printf("%s parked at %s. Ticket: %s\n",
        vehicle.getLicensePlate(), spot.get().getSpotId(), ticket.getTicketId());
return Optional.of(ticket);
```

- **Flow**:
  1. Delegates spot-finding to `parkingStrategy`.
  2. If no spot is found: return `Optional.empty()` (parking failed).
  3. Else:
     - Mark spot as occupied.
     - Create a new `ParkingTicket`.
     - Store it in `activeTickets` keyed by `ticketId`.
     - Log parking information.

##### `Optional<Double> unparkVehicle(String ticketId)`

```java
ParkingTicket ticket = activeTickets.remove(ticketId);
if (ticket == null) {
    System.out.println("Ticket not found");
    return Optional.empty();
}
ticket.setExitTime();
ticket.getSpot().unparkVehicle();
Double parkingFee = feeStrategy.CalculateFee(ticket);
return Optional.of(parkingFee);
```

- **Flow**:
  1. Look up and remove the ticket from `activeTickets`.
  2. If ticket not found, return empty.
  3. Set `exitTime`.
  4. Unpark vehicle from spot.
  5. Delegate fee computation to `feeStrategy`.
  6. Return total fee.

**Note**: The demo calls `unparkVehicle` with `ticket.getTicketId()`, which matches the map key.

---

### 6. Demo Flow (`ParkingLotDemo`)

```java
ParkingLot parkingLot = ParkingLot.getInstance();

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
```

- Initializes floors and spots.
- Sets the fee strategy (can be swapped to `FlatRateFeeStrategy` if desired).

```java
Vehicle bike = new Bike("B-123");
Vehicle car = new Car("C-456");
Vehicle truck = new Truck("T-789");

Optional<ParkingTicket> bikeTicketOpt = parkingLot.parkVehicle(bike);
Optional<ParkingTicket> carTicketOpt = parkingLot.parkVehicle(car);
Optional<ParkingTicket> truckTicketOpt = parkingLot.parkVehicle(truck);
```

- Demonstrates spot allocation per vehicle size and current strategy.

```java
Vehicle car2 = new Car("C-999");
Optional<ParkingTicket> car2TicketOpt = parkingLot.parkVehicle(car2);

Vehicle bike2 = new Bike("B-000");
Optional<ParkingTicket> failedBikeTicketOpt = parkingLot.parkVehicle(bike2);
```

- Demonstrates:
  - Allocation to another floor.
  - Behavior when no suitable spot exists.

```java
if (carTicketOpt.isPresent()) {
    Optional<Double> fee = parkingLot.unparkVehicle(carTicketOpt.get().getTicketId());
    fee.ifPresent(f -> System.out.printf("Car C-456 unparked. Fee: $%.2f\n", f));
}
```

- Demonstrates un-parking and fee calculation.

---

### 7. Patterns and Tradeoffs

- **Singleton (`ParkingLot`)**:
  - **Pros**: Simple global access; mirrors a single-lot concept; easy in small apps/demos.
  - **Cons**: Harder to unit-test in isolation; not ideal for large, distributed systems.

- **Strategy Pattern (Fee + Parking)**:
  - **Pros**:
    - Open/closed principle: can add new strategies without modifying `ParkingLot`.
    - Runtime configurability.
  - **Cons**:
    - Slight indirection; more small classes to manage.

- **Composition over Inheritance**:
  - `ParkingLot` *has-a* strategy and floors, it doesn’t inherit from them.
  - Makes behavior swaps safe and localized.

- **Concurrency**:
  - `ConcurrentHashMap` for `activeTickets` and `parkingSpots` on floors.
  - `synchronized` blocks for spot-level operations.
  - Good for basic thread safety; not a fully optimized high-throughput system.

- **Data Modeling**:
  - Mapping of vehicle to spot via `ParkingTicket` allows:
    - Clear lifecycle tracking.
    - Flexible fee computation.
  - `VehicleSize` + `ParkingSpot.canFitVehicle` encapsulate size rules.

---

### 8. Possible Extensions

- **Dynamic Pricing**:
  - New `FeeStrategy` implementations for:
    - Surge pricing on peak hours.
    - Discounts for long-term parking.

- **Reservations**:
  - Add `Reservation` entity and integrate it with `ParkingLot`.
  - Reserve a spot for a given vehicle/time window.

- **Different Spot Types**:
  - EV charging spots, handicapped spots, VIP spots, etc.
  - Extend `ParkingSpot` or add a type field and adapt `canFitVehicle`/strategies.

- **Monitoring & Reporting**:
  - Track historical usage, revenue, and average occupancy per floor.

- **Multiple Lots**:
  - Replace Singleton with DI-managed instances; support multiple `ParkingLot`s in a city model.

---

### 9. Summary

- The Parking System LLD is built around:
  - Clear domain entities (`Vehicle`, `ParkingSpot`, `ParkingFloor`, `ParkingTicket`).
  - A central orchestrator (`ParkingLot`) that delegates pricing and spot selection to pluggable strategies.
  - Use of **Strategy** and **Singleton** patterns to balance configurability and simplicity.
- This design is well-suited for interviews and as a foundation to grow into a richer parking-management system by adding more strategies, entities, and rules without rewriting core logic.


