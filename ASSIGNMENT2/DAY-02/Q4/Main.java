// Strategy interface for processing payments.
interface PaymentStrategy {
    boolean processPayment(double amount);
}

// Payment strategy for credit card payments.
class CreditCardStrategy implements PaymentStrategy {
    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing credit card payment for $" + amount + ".");
        return true;
    }
}

// Payment strategy for cryptocurrency payments.
class CryptoStrategy implements PaymentStrategy {
    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing cryptocurrency payment for $" + amount + ".");
        return true;
    }
}

// The processor holds a payment strategy and delegates execution to it.
class TransactionProcessor {
    private PaymentStrategy strategy;

    public TransactionProcessor(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    public void setPaymentStrategy(PaymentStrategy strategy) {
        this.strategy = strategy;
    }

    public void executeTransaction(double amount) {
        if (strategy.processPayment(amount)) {
            System.out.println("Transaction completed successfully.");
        } else {
            System.out.println("Transaction failed.");
        }
    }
}

public class Main {
    public static void main(String[] args) {
        TransactionProcessor processor = new TransactionProcessor(new CreditCardStrategy());
        processor.executeTransaction(120.00);

        System.out.println("Switching to cryptocurrency payment strategy.");
        processor.setPaymentStrategy(new CryptoStrategy());
        processor.executeTransaction(120.00);
    }
}
