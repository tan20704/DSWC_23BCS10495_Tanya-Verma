// Base vehicle type for all car variants.
abstract class Vehicle {
    public abstract void drive();
}

// A traditional gas-powered car.
class GasCar extends Vehicle {
    @Override
    public void drive() {
        System.out.println("GasCar is driving using a combustion engine.");
    }
}

// An electric car with a special firmware update behavior.
class ElectricCar extends Vehicle {
    @Override
    public void drive() {
        System.out.println("ElectricCar is driving silently with electric power.");
    }

    public void updateFirmware() {
        System.out.println("ElectricCar firmware has been updated.");
    }
}

public class Main {
    public static void main(String[] args) {
        Vehicle[] vehicles = new Vehicle[] {
            new GasCar(),
            new ElectricCar()
        };

        for (Vehicle vehicle : vehicles) {
            vehicle.drive();
            if (vehicle instanceof ElectricCar) {
                ElectricCar electric = (ElectricCar) vehicle;
                electric.updateFirmware();
            } else {
                System.out.println("Skipping firmware update for non-electric vehicle.");
            }

            System.out.println();
        }
    }
}
