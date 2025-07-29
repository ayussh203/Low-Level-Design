package OOPS.Abstraction.AbstractClasses;

abstract class Vehicle {
    String brand;
    
    // Constructor
    Vehicle(String brand) {
        this.brand = brand;
    }
    
    // Abstract method (must be implemented by subclasses)
    abstract void start();
    
    // Concrete method (can be inherited)
    void displayBrand() {
        System.out.println("Brand: " + brand);
    }
}

// Subclass implementing the abstract method
class Car extends Vehicle {
    Car(String brand) {
        super(brand);
    }
    
    @Override
    void start() {
        System.out.println("Car is starting...");
    }
}

public class Main {
    public static void main(String[] args) {
        Vehicle myCar = new Car("Toyota");
        myCar.displayBrand();
        myCar.start();
    }
}

/**
 * 
 * Why Use Abstract Classes?

Allows defining common behavior that subclasses must implement.
Enables partial abstraction (can have both abstract and concrete methods).
Prevents direct instantiation of base classes.

 */