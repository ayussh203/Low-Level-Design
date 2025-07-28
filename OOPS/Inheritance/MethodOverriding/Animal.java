package OOPS.Inheritance.MethodOverriding;

public class Animal {
    String name;
    Animal()
    {
        System.out.println(this.name + " is an animal.");
    }
    void eat() {
        System.out.println(name + " is eating.");
    }
}
