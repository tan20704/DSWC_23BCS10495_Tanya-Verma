// Abstract parent class that calls setup() during construction.
abstract class DatabaseConfig {
    public DatabaseConfig() {
        System.out.println("DatabaseConfig constructor start.");
        setup();
        System.out.println("DatabaseConfig constructor end.");
    }

    protected abstract void setup();
}

// Child class that overrides setup but initializes its own field later.
class SecureConfig extends DatabaseConfig {
    private String authToken = "Bearer123";

    public SecureConfig() {
        System.out.println("SecureConfig constructor start.");
        System.out.println("SecureConfig constructor end. authToken = " + authToken);
    }

    @Override
    protected void setup() {
        System.out.println("SecureConfig.setup() sees authToken = " + authToken);
    }
}

public class Main {
    public static void main(String[] args) {
        new SecureConfig();
    }
}
