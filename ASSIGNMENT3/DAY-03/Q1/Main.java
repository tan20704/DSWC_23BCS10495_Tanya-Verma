import java.util.*;

// Represents a passenger with a stable identity based on passport number and nationality.
class Passenger {
    private String passportNumber;
    private String fullName;
    private String nationality;

    public Passenger(String passportNumber, String fullName, String nationality) {
        this.passportNumber = passportNumber;
        this.fullName = fullName;
        this.nationality = nationality;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public String getNationality() {
        return nationality;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Passenger)) {
            return false;
        }
        Passenger other = (Passenger) obj;
        return Objects.equals(passportNumber, other.passportNumber)
                && Objects.equals(nationality, other.nationality);
    }

    @Override
    public int hashCode() {
        return Objects.hash(passportNumber, nationality);
    }

    @Override
    public String toString() {
        return fullName + " [" + passportNumber + ", " + nationality + "]";
    }
}

// Manages the airline manifest, no-fly list, and global lookup by passenger.
class ManifestManager {
    private Set<Passenger> globalNoFlyList = new HashSet<>();
    private Map<String, List<Passenger>> flightRosters = new HashMap<>();
    private Map<Passenger, String> globalPassengerDirectory = new HashMap<>();

    public void addToNoFlyList(Passenger p) {
        globalNoFlyList.add(p);
    }

    public boolean processCheckIn(String flightNumber, Passenger p) {
        if (globalNoFlyList.contains(p)) {
            return false;
        }

        List<Passenger> roster = flightRosters.computeIfAbsent(flightNumber, key -> new ArrayList<>());
        roster.add(p);
        globalPassengerDirectory.put(p, flightNumber);
        return true;
    }

    public String locatePassengerFlight(Passenger p) {
        return globalPassengerDirectory.get(p);
    }

    public List<Passenger> getFlightManifest(String flightNumber) {
        return flightRosters.getOrDefault(flightNumber, Collections.emptyList());
    }
}

public class Main {
    public static void main(String[] args) {
        ManifestManager manager = new ManifestManager();

        Passenger alice = new Passenger("P1234567", "Alice Johnson", "USA");
        Passenger bob = new Passenger("P2345678", "Bob Carter", "USA");
        Passenger aliceUpdated = new Passenger("P1234567", "Alice Smith", "USA");
        Passenger charlie = new Passenger("P3456789", "Charlie Chen", "CAN");

        manager.addToNoFlyList(charlie);

        System.out.println("Check-in Alice: " + manager.processCheckIn("SKY100", alice));
        System.out.println("Check-in Bob: " + manager.processCheckIn("SKY100", bob));
        System.out.println("Check-in Charlie (banned): " + manager.processCheckIn("SKY100", charlie));

        // The lookup should succeed even though Alice's name changed.
        System.out.println("Locate Alice: " + manager.locatePassengerFlight(aliceUpdated));

        System.out.println("SKY100 manifest order:");
        for (Passenger passenger : manager.getFlightManifest("SKY100")) {
            System.out.println(" - " + passenger);
        }
    }
}
