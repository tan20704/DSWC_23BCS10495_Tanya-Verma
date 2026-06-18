// First interface with a default logging implementation.
interface AuditTracker {
    default void log() {
        System.out.println("AuditTracker: logging audit activity.");
    }
}

// Second interface with another default logging implementation.
interface PerformanceTracker {
    default void log() {
        System.out.println("PerformanceTracker: logging performance metrics.");
    }
}

// MasterService implements both interfaces and resolves the diamond problem.
class MasterService implements AuditTracker, PerformanceTracker {
    @Override
    public void log() {
        System.out.println("MasterService: resolving default method conflict.");
        AuditTracker.super.log();
        PerformanceTracker.super.log();
    }
}

public class Main {
    public static void main(String[] args) {
        MasterService service = new MasterService();
        service.log();
    }
}
