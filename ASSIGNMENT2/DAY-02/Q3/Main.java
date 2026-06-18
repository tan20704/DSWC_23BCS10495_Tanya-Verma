// Represents the valid states of the vault door.
enum DoorState {
    OPEN,
    CLOSED,
    LOCKED
}

// Custom exception used when an invalid state transition is attempted.
class IllegalStateTransitionException extends RuntimeException {
    public IllegalStateTransitionException(String message) {
        super(message);
    }
}

// VaultDoor encapsulates its internal state and exposes only safe operations.
class VaultDoor {
    private DoorState state = DoorState.OPEN;

    public void closeDoor() {
        if (state == DoorState.OPEN) {
            state = DoorState.CLOSED;
            System.out.println("VaultDoor is now CLOSED.");
        } else {
            System.out.println("VaultDoor is already " + state + ".");
        }
    }

    public void lockDoor() {
        if (state == DoorState.OPEN) {
            throw new IllegalStateTransitionException("Cannot lock the door while it is OPEN. Close the door first.");
        }
        if (state == DoorState.CLOSED) {
            state = DoorState.LOCKED;
            System.out.println("VaultDoor is now LOCKED.");
        } else {
            System.out.println("VaultDoor is already LOCKED.");
        }
    }

    public void unlockDoor() {
        if (state == DoorState.LOCKED) {
            state = DoorState.CLOSED;
            System.out.println("VaultDoor is now UNLOCKED and CLOSED.");
        } else {
            System.out.println("VaultDoor is not locked; current state is " + state + ".");
        }
    }
}

public class Main {
    public static void main(String[] args) {
        VaultDoor vaultDoor = new VaultDoor();

        // Demonstrate fail-fast behavior when attempting an invalid transition.
        try {
            vaultDoor.lockDoor();
        } catch (IllegalStateTransitionException ex) {
            System.out.println("IllegalStateTransitionException caught: " + ex.getMessage());
        }

        vaultDoor.closeDoor();
        vaultDoor.lockDoor();
        vaultDoor.unlockDoor();
    }
}
