import java.util.*;

// Represents a transaction in the e-commerce system.
class Transaction {
    private String status;
    private String category;
    private double amount;

    public Transaction(String status, String category, double amount) {
        this.status = status;
        this.category = category;
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }
}

// Uses Java Streams to calculate revenue without explicit loops or conditionals.
class SalesAnalyzer {
    public double calculateElectronicsRevenue(List<Transaction> transactions) {
        return transactions.stream()
                .filter(tx -> "COMPLETED".equals(tx.getStatus()))
                .filter(tx -> "ELECTRONICS".equals(tx.getCategory()))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }
}

public class Main {
    public static void main(String[] args) {
        List<Transaction> transactions = Arrays.asList(
                new Transaction("COMPLETED", "ELECTRONICS", 150.0),
                new Transaction("PENDING", "ELECTRONICS", 200.0),
                new Transaction("COMPLETED", "GROCERIES", 80.0),
                new Transaction("COMPLETED", "ELECTRONICS", 340.0)
        );

        SalesAnalyzer analyzer = new SalesAnalyzer();
        double revenue = analyzer.calculateElectronicsRevenue(transactions);
        System.out.println("Electronics revenue from completed transactions: $" + revenue);
    }
}
