package OOPS.ClassesAndObjects.Car;

public class Car{
	private String color;
    private String model;
    private String year;
    private String make;

    Car(String color,String model,String year,String make)
    {
        this.color=color;
        this.model=model;
        this.year=year;
        this.make=make;
    }
    public void displayInfo()
    {
        System.out.println("Car make:"+make);
        System.out.println("Car Model: " + model);
        System.out.println("Car Year: " + year);
        System.out.println("Car Color: " + color);
    }
}

