// Abstract base class for all delivery drones.
abstract class DeliveryDrone {
    protected String droneId;

    public DeliveryDrone(String droneId) {
        this.droneId = droneId;
    }

    // Every drone must implement its own delivery behavior.
    public abstract void deliverPackage();
}

// Airborne drones need to fly and request clearance from air traffic control.
interface Airborne {
    void flyToDestination();

    default void requestAirTrafficClearance() {
        System.out.println("Requesting air traffic clearance for airborne operation.");
    }
}

// Ground-based drones travel on sidewalks and streets.
interface GroundBased {
    void navigateSidewalks();
}

// A standard quadcopter that only flies.
class Quadcopter extends DeliveryDrone implements Airborne {
    public Quadcopter(String droneId) {
        super(droneId);
    }

    @Override
    public void deliverPackage() {
        requestAirTrafficClearance();
        flyToDestination();
        System.out.println("Quadcopter " + droneId + " delivered the package by air.");
    }

    @Override
    public void flyToDestination() {
        System.out.println("Quadcopter " + droneId + " is flying to its delivery destination.");
    }
}

// A city rover that only drives on the ground.
class CityRover extends DeliveryDrone implements GroundBased {
    public CityRover(String droneId) {
        super(droneId);
    }

    @Override
    public void deliverPackage() {
        navigateSidewalks();
        System.out.println("CityRover " + droneId + " delivered the package on the ground.");
    }

    @Override
    public void navigateSidewalks() {
        System.out.println("CityRover " + droneId + " is navigating sidewalks to reach the customer.");
    }
}

// Hybrid VTOL can both fly and drive, illustrating interface-based capability composition.
class HybridVTOL extends DeliveryDrone implements Airborne, GroundBased {
    public HybridVTOL(String droneId) {
        super(droneId);
    }

    @Override
    public void deliverPackage() {
        requestAirTrafficClearance();
        flyToDestination();
        navigateSidewalks();
        System.out.println("HybridVTOL " + droneId + " completed a hybrid air-ground delivery.");
    }

    @Override
    public void flyToDestination() {
        System.out.println("HybridVTOL " + droneId + " is flying to the drop zone.");
    }

    @Override
    public void navigateSidewalks() {
        System.out.println("HybridVTOL " + droneId + " is navigating sidewalks for the final segment.");
    }
}

public class Main {
    public static void main(String[] args) {
        DeliveryDrone[] fleet = new DeliveryDrone[] {
            new Quadcopter("QC-01"),
            new CityRover("CR-42"),
            new HybridVTOL("HV-77")
        };

        for (DeliveryDrone drone : fleet) {
            drone.deliverPackage();
            System.out.println();
        }
    }
}
