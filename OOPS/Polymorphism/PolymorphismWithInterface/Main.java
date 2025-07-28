package OOPS.Polymorphism.PolymorphismWithInterface;

interface Payment {
    void pay(double amount);
}

class CreditCardPayment implements Payment {
    @Override
    public void pay(double amount) {
        System.out.println("Paid " + amount + " using Credit Card");
    }
}

class PayPalPayment implements Payment {
    @Override
    public void pay(double amount) {
        System.out.println("Paid " + amount + " using PayPal");
    }
}

public class Main {
    public static void main(String[] args) {
        Payment payment;
        
        payment = new CreditCardPayment();
        payment.pay(100.50);
        
        payment = new PayPalPayment();
        payment.pay(200.75);
    }
}

/**
 * 
 * Why Use Polymorphism in Payment Systems?

Allows new payment methods to be added without modifying existing code.
Provides a flexible and scalable design.
Improves code readability and maintainability.
 */
