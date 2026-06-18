// PricingStrategy defines the contract for any discount or pricing rule.
interface PricingStrategy {
    double calculate(double total);
}

// Concrete strategy applying a 20% Black Friday discount.
class BlackFridayStrategy implements PricingStrategy {
    @Override
    public double calculate(double total) {
        double discounted = total * 0.8;
        System.out.println("BlackFridayStrategy: new total after 20% off = $" + discounted);
        return discounted;
    }
}

// Concrete strategy applying a flat VIP discount.
class VIPStrategy implements PricingStrategy {
    @Override
    public double calculate(double total) {
        double discounted = Math.max(0, total - 50);
        System.out.println("VIPStrategy: new total after $50 off = $" + discounted);
        return discounted;
    }
}

public class Main {
    public static void main(String[] args) {
        PricingStrategy[] activeDiscounts = new PricingStrategy[] {
            new BlackFridayStrategy(),
            new VIPStrategy()
        };

        double total = 500.00;
        System.out.println("Starting total = $" + total);

        for (PricingStrategy strategy : activeDiscounts) {
            total = strategy.calculate(total);
        }

        System.out.println("Final total after all strategies = $" + total);
    }
}
