package OOPS.Encapsulation.PaymentProcessingExample;

class PaymentProcessor{
    private String cardNumber;
    private double amount;
    public PaymentProcessor(String cardNumber, double amount) {
        this.cardNumber = maskCardNumber(cardNumber);;
        this.amount = amount;

    }
    private String maskCardNumber(String cardNumber)
    {
        return "****-****-****-" + cardNumber.substring(cardNumber.length() - 4);
    }
    public void processPayment()
    {
        System.out.println("Processing payment of " + amount + " using card: " + cardNumber);
    }

}
public class Main {
    public static void main(String[] args) {
        PaymentProcessor payment = new PaymentProcessor("1234567812345678", 250.00);
        payment.processPayment();
    }
}

/**
 * 
 * 
 * Encapsulation is used in many real-world applications such as:

Banking Systems - Ensuring account details are private.
Healthcare Applications - Protecting patient records.
E-Commerce Platforms - Hiding payment processing details.
 */

 /**
  * 
  Why Use Encapsulation in Payment Processing?

Protects sensitive data (e.g., credit card numbers).
Hides unnecessary details from users.
Ensures secure transactions.
  */