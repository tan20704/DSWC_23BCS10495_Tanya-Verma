// Base class that defines a common interest rate field and getter.
class BaseAccount {
    double interestRate = 4.5;

    public double getInterestRate() {
        return interestRate;
    }
}

// PremiumAccount overrides the interest rate field and the getter method.
class PremiumAccount extends BaseAccount {
    double interestRate = 7.5;

    @Override
    public double getInterestRate() {
        return interestRate;
    }
}

public class Main {
    public static void main(String[] args) {
        // Upcast a PremiumAccount to BaseAccount to demonstrate early vs. late binding.
        BaseAccount account = new PremiumAccount();

        System.out.println("account.interestRate = " + account.interestRate);
        System.out.println("account.getInterestRate() = " + account.getInterestRate());
        System.out.println("Explanation: field access uses the reference type (BaseAccount), while overridden methods use the actual object type (PremiumAccount).");
    }
}
