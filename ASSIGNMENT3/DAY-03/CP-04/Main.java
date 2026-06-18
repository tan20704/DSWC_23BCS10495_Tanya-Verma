import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Transaction {
    private final String employeeName;
    private final double amount;

    public Transaction(String employeeName, double amount) {
        this.employeeName = employeeName;
        this.amount = amount;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public double getAmount() {
        return amount;
    }
}

public class Main {
    public static void main(String[] args) {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction("Alice", 1200.50));
        transactions.add(new Transaction("Bob", 800.00));
        transactions.add(new Transaction("Alice", 450.00));
        transactions.add(new Transaction("Charlie", 800.00));
        transactions.add(new Transaction("Bob", 200.00));
        transactions.add(new Transaction("Diana", 1600.00));
        transactions.add(new Transaction("Charlie", 100.00));

        Map<String, Double> totalsByEmployee = new HashMap<>();

        for (Transaction transaction : transactions) {
            String name = transaction.getEmployeeName();
            double amount = transaction.getAmount();
            totalsByEmployee.put(name, totalsByEmployee.getOrDefault(name, 0.0) + amount);
        }

        List<Map.Entry<String, Double>> sortedResults = new ArrayList<>(totalsByEmployee.entrySet());
        sortedResults.sort((entry1, entry2) -> {
            int amountCompare = Double.compare(entry2.getValue(), entry1.getValue());
            if (amountCompare != 0) {
                return amountCompare;
            }
            return entry1.getKey().compareTo(entry2.getKey());
        });

        System.out.println("Employee revenue ranking:");
        for (Map.Entry<String, Double> entry : sortedResults) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}
