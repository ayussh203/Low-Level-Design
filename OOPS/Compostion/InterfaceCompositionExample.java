package OOPS.Compostion;

interface Engine {
    void start();
}

class PetrolEngine implements Engine {
    public void start() {
        System.out.println("Petrol Engine started.");
    }
}

class DieselEngine implements Engine {
    public void start() {
        System.out.println("Diesel Engine started.");
    }
}

class Car {
    private Engine engine;

    public Car(Engine engine) {
        this.engine = engine;
    }

    public void startCar() {
        engine.start();
        System.out.println("Car is ready to go!");
    }
}

public class InterfaceCompositionExample {
    public static void main(String[] args) {
        Car petrolCar = new Car(new PetrolEngine());
        petrolCar.startCar();
        
        Car dieselCar = new Car(new DieselEngine());
        dieselCar.startCar();
    }
}