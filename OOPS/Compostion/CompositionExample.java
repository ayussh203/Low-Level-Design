package OOPS.Compostion;

class Engine {
    private int horsepower;

    public Engine(int horsepower) {
        this.horsepower = horsepower;
    }

    public void start() {
        System.out.println("Engine started with " + horsepower + " HP.");
    }
}

class Wheel {
    private String type;

    public Wheel(String type) {
        this.type = type;
    }

    public void rotate() {
        System.out.println("The " + type + " wheel is rotating.");
    }
}

class Transmission {
    private String type;

    public Transmission(String type) {
        this.type = type;
    }

    public void shiftGear() {
        System.out.println("Transmission shifted: " + type);
    }
}

class Car {
    private Engine engine;
    private Wheel wheel;
    private Transmission transmission;

    public Car(Engine engine, Wheel wheel, Transmission transmission) {
        this.engine = engine;
        this.wheel = wheel;
        this.transmission = transmission;
    }

    public void drive() {
        engine.start();
        wheel.rotate();
        transmission.shiftGear();
        System.out.println("Car is moving!");
    }
}

public class CompositionExample {
    public static void main(String[] args) {
        Engine engine = new Engine(150);
        Wheel wheel = new Wheel("Alloy");
        Transmission transmission = new Transmission("Automatic");
        
        Car car = new Car(engine, wheel, transmission);
        car.drive();
    }
}