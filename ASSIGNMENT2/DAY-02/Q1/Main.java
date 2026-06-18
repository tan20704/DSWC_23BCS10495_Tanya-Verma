// This interface defines the battery-related behavior shared by wireless devices.
interface BatteryOperated {
    int getBatteryLevel();
    void triggerRechargeAlert();
}

// SmartDevice is the common abstract base for all smart home devices.
abstract class SmartDevice {
    protected String deviceId;
    protected String deviceName;

    public SmartDevice(String deviceId, String deviceName) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
    }

    // Every device must be able to run its own diagnostic action.
    public abstract void runDiagnostic();
}

// A hardwired smart light that does not use batteries.
class SmartLight extends SmartDevice {
    public SmartLight(String deviceId, String deviceName) {
        super(deviceId, deviceName);
    }

    @Override
    public void runDiagnostic() {
        System.out.println(deviceName + " (" + deviceId + ") is checking the ceiling light circuit.");
    }
}

// A wireless camera that also supports battery checks.
class SmartCamera extends SmartDevice implements BatteryOperated {
    private int batteryLevel;

    public SmartCamera(String deviceId, String deviceName, int batteryLevel) {
        super(deviceId, deviceName);
        this.batteryLevel = batteryLevel;
    }

    @Override
    public void runDiagnostic() {
        System.out.println(deviceName + " (" + deviceId + ") is recording a diagnostic feed.");
    }

    @Override
    public int getBatteryLevel() {
        return batteryLevel;
    }

    @Override
    public void triggerRechargeAlert() {
        System.out.println("Recharge alert: " + deviceName + " battery is low (" + batteryLevel + "%). Please recharge.");
    }
}

// A wireless lock that also needs battery monitoring.
class SmartLock extends SmartDevice implements BatteryOperated {
    private int batteryLevel;

    public SmartLock(String deviceId, String deviceName, int batteryLevel) {
        super(deviceId, deviceName);
        this.batteryLevel = batteryLevel;
    }

    @Override
    public void runDiagnostic() {
        System.out.println(deviceName + " (" + deviceId + ") is testing lock and sensor response.");
    }

    @Override
    public int getBatteryLevel() {
        return batteryLevel;
    }

    @Override
    public void triggerRechargeAlert() {
        System.out.println("Recharge alert: " + deviceName + " battery is low (" + batteryLevel + "%). Please recharge.");
    }
}

// The HomeHub coordinates the nightly diagnostics for all devices.
class HomeHub {
    public void executeNightlyRoutine(SmartDevice[] devices) {
        for (SmartDevice device : devices) {
            device.runDiagnostic();

            // Use instanceof to safely identify battery-powered devices.
            if (device instanceof BatteryOperated) {
                BatteryOperated batteryDevice = (BatteryOperated) device;
                if (batteryDevice.getBatteryLevel() < 20) {
                    batteryDevice.triggerRechargeAlert();
                }
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        SmartDevice[] devices = new SmartDevice[] {
            new SmartLight("SL-001", "Kitchen Light"),
            new SmartCamera("SC-101", "Front Door Camera", 15),
            new SmartLock("LK-501", "Main Entrance Lock", 85)
        };

        HomeHub hub = new HomeHub();
        hub.executeNightlyRoutine(devices);
    }
}
