package OOPS.Abstraction.PaymentSystemExample;

// Abstract class for Payment
abstract class Payment {
    double amount;
    
    Payment(double amount) {
        this.amount = amount;
    }
    
    abstract void pay(); // Abstract method
}

// Implementing payment methods
class CreditCardPayment extends Payment {
    CreditCardPayment(double amount) {
        super(amount);
    }
    
    @Override
    void pay() {
        System.out.println("Paid " + amount + " using Credit Card");
    }
}

class PayPalPayment extends Payment {
    PayPalPayment(double amount) {
        super(amount);
    }
    
    @Override
    void pay() {
        System.out.println("Paid " + amount + " using PayPal");
    }
}

public class Main {
    public static void main(String[] args) {
        Payment payment;
        
        payment = new CreditCardPayment(150.75);
        payment.pay();
        
        payment = new PayPalPayment(200.50);
        payment.pay();
    }
}

/**
 * 
 * Why Use Abstraction in Payment Systems?

Allows multiple payment methods without modifying existing code.
Improves maintainability and scalability.
Provides a common contract for different payment types.
 */