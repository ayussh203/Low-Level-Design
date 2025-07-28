package OOPS.Inheritance.MethodOverriding;

public class Main {
    public static void main(String[] args) {
        Animal obj = new Dog();
        obj.name = "Buddy";
        obj.eat();
        Animal obj1=new Animal();
        obj1.name = "Generic Animal";
        obj1.eat();
    }
}
/**
 * 
 * output
 * null is an animal.
Dog is eating dog food.
Buddy is eating.
Buddy is eating dog food.
null is an animal.
Generic Animal is eating.
 * 
 */
/**
 * 
 * class Parent {
 	String name = "Parent";
 	String getName() { return name; }
 }

 class Child extends Parent {
 	String name = "Child";
 	@Override
 	String getName() { return name; }
 }

 public class Main {
 	public static void main(String[] args) {
     	Parent p = new Child();
     	System.out.println(p.name + " & " + p.getName());
 	}
 }


 o/p

    * Parent & Child
    because Field access (`p.name`) uses the reference type (`Parent`), so prints “Parent”. But `getName()` is overridden, so it calls the `Child` version, printing “Child”.

 */