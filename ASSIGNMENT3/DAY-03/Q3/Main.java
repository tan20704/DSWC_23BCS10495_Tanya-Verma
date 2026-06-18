import java.util.concurrent.*;

// Represents a trade order in the exchange.
class Order {
    private String orderId;
    private double amount;

    public Order(String orderId, double amount) {
        this.orderId = orderId;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return orderId + "($" + amount + ")";
    }
}

// Exchange manager uses thread-safe collections to hold orders per ticker.
class ExchangeManager {
    private ConcurrentHashMap<String, CopyOnWriteArrayList<Order>> orderBook = new ConcurrentHashMap<>();

    public void placeOrder(String ticker, Order order) {
        CopyOnWriteArrayList<Order> orders = orderBook.computeIfAbsent(ticker, key -> new CopyOnWriteArrayList<>());
        orders.add(order);
    }

    public CopyOnWriteArrayList<Order> getOrders(String ticker) {
        return orderBook.getOrDefault(ticker, new CopyOnWriteArrayList<>());
    }
}

public class Main {
    public static void main(String[] args) {
        ExchangeManager exchange = new ExchangeManager();

        exchange.placeOrder("BTC", new Order("ORD-001", 0.5));
        exchange.placeOrder("BTC", new Order("ORD-002", 1.2));
        exchange.placeOrder("ETH", new Order("ORD-003", 2.0));

        System.out.println("BTC orders: " + exchange.getOrders("BTC"));
        System.out.println("ETH orders: " + exchange.getOrders("ETH"));
    }
}
