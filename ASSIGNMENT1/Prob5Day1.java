import java.util.concurrent.atomic.AtomicInteger;

class DroneHive {

    AtomicInteger totalReturned =new AtomicInteger(0);

    volatile boolean emergencyAbort =false;

    public void droneReturned() {
        totalReturned.incrementAndGet();
    }

    public void abortMission() {
        emergencyAbort = true;
    }
}

public class Prob5Day1{
    public static void main(String[] args) {

        DroneHive d = new DroneHive();

        d.droneReturned();
        d.droneReturned();

        System.out.println(d.totalReturned.get());

        d.abortMission();

        System.out.println(d.emergencyAbort);
    }
}