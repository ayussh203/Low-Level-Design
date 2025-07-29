package OOPS.Encapsulation.DataHiding;

class Account {
    private double balance;

    public Account(double initialBalance) {
        this.balance = initialBalance;
    }

    private boolean validateWithdrawal(double amount) {
        return amount > 0 && amount <= balance;
    }

    public void withdraw(double amount) {
        if (validateWithdrawal(amount)) {
            balance -= amount;
            System.out.println("Withdrawal Successful: " + amount);
        } else {
            System.out.println("Insufficient balance or invalid amount");
        }
    }

    public double getBalance() {
        return balance;
    }
}

public class Main {
    public static void main(String[] args) {
        Account myAccount = new Account(1000);
        myAccount.withdraw(300);
        System.out.println("Remaining Balance: " + myAccount.getBalance());
    }
}

/**
 * 
 * Why Hide Data?

Prevents direct modification of important fields.
Ensures data integrity by validating inputs.
 */