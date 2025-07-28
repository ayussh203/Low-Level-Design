package OOPS.Polymorphism.MethodOverloading;

class MathOperations {
    // Method with two parameters
    int add(int a, int b) {
        return a + b;
    }
    
    // Method with three parameters (overloaded)
    int add(int a, int b, int c) {
        return a + b + c;
    }
}

public class Main {
    public static void main(String[] args) {
        MathOperations math = new MathOperations();
        System.out.println("Sum (2 numbers): " + math.add(5, 10));
        System.out.println("Sum (3 numbers): " + math.add(5, 10, 15));
    }
}