import java.util.*;

// Priority levels used by the triage system.
enum TriageLevel {
    CRITICAL,
    URGENT,
    STABLE
}

// Patient objects are ordered first by severity then by arrival time.
class Patient implements Comparable<Patient> {
    private String name;
    private TriageLevel severity;
    private long arrivalTime;

    public Patient(String name, TriageLevel severity, long arrivalTime) {
        this.name = name;
        this.severity = severity;
        this.arrivalTime = arrivalTime;
    }

    public String getName() {
        return name;
    }

    public TriageLevel getSeverity() {
        return severity;
    }

    @Override
    public int compareTo(Patient other) {
        int severityCompare = Integer.compare(this.severity.ordinal(), other.severity.ordinal());
        if (severityCompare != 0) {
            return severityCompare;
        }
        return Long.compare(this.arrivalTime, other.arrivalTime);
    }

    @Override
    public String toString() {
        return name + " (" + severity + ", " + arrivalTime + ")";
    }
}

// Manages incoming patients using a priority queue.
class TriageManager {
    private PriorityQueue<Patient> waitingRoom = new PriorityQueue<>();

    public void admitPatient(Patient p) {
        waitingRoom.add(p);
    }

    public Patient getNextPatient() {
        return waitingRoom.poll();
    }
}

public class Main {
    public static void main(String[] args) {
        TriageManager triage = new TriageManager();

        triage.admitPatient(new Patient("Alice", TriageLevel.URGENT, 1L));
        triage.admitPatient(new Patient("Bob", TriageLevel.CRITICAL, 3L));
        triage.admitPatient(new Patient("Clara", TriageLevel.CRITICAL, 2L));
        triage.admitPatient(new Patient("David", TriageLevel.STABLE, 4L));

        System.out.println("Treating patients in priority order:");
        Patient next;
        while ((next = triage.getNextPatient()) != null) {
            System.out.println(" - " + next);
        }
    }
}
