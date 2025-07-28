package OOPS.Inheritance.MethodOverriding;

public class Dog extends Animal {
    Dog() {
        super();
        System.out.println("Dog is eating dog food.");
    }
    void eat()
    {
        super.eat();
        System.out.println(this.name + " is eating dog food.");
    }
    
}
