package OOPS.Abstraction.UsingInterface;

interface Animal {
    void makeSound(); // Abstract method
}

// Implementing the interface in Dog class
class Dog implements Animal {
    @Override
    public void makeSound() {
        System.out.println("Dog barks");
    }
}

// Implementing the interface in Cat class
class Cat implements Animal {
    @Override
    public void makeSound() {
        System.out.println("Cat meows");
    }
}

public class Main {
    public static void main(String[] args) {
        Animal myDog = new Dog();
        myDog.makeSound();
        
        Animal myCat = new Cat();
        myCat.makeSound();
    }
}

/**
 * 
 * Why Use Interfaces?

Promotes full abstraction (hides all implementation details).
Supports multiple inheritance in Java (a class can implement multiple interfaces).
Provides a standard way for different classes to implement behaviors.
 */