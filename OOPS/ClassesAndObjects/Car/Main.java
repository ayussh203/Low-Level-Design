package OOPS.ClassesAndObjects.Car;

public class Main {
    public static void main(String[] args)
    {
        Car obj1=new Car("red","Toyota","Corolla","2020");
        Car obj2=new Car("white", "Mahindra", "2025", "2025");
        obj1.displayInfo();
        obj2.displayInfo();
    }
}
