import java.util.*;
import java.util.stream.*;

// Abstract cargo base class for all cargo types.
abstract class Cargo {
    protected String containerId;
    protected double valueInCredits;
    protected boolean isHazardous;

    public Cargo(String containerId, double valueInCredits, boolean isHazardous) {
        this.containerId = containerId;
        this.valueInCredits = valueInCredits;
        this.isHazardous = isHazardous;
    }
}

// Standard cargo with no extra fields.
class StandardCargo extends Cargo {
    public StandardCargo(String containerId, double valueInCredits, boolean isHazardous) {
        super(containerId, valueInCredits, isHazardous);
    }
}

// Biological cargo includes an extra shielding property.
class BiologicalCargo extends Cargo {
    private boolean isShielded;

    public BiologicalCargo(String containerId, double valueInCredits, boolean isHazardous, boolean isShielded) {
        super(containerId, valueInCredits, isHazardous);
        this.isShielded = isShielded;
    }

    public boolean isShielded() {
        return isShielded;
    }
}

// Custom functional interface to inspect cargo safety.
@FunctionalInterface
interface CargoInspector {
    boolean inspect(Cargo cargo);
}

// Custom functional interface to compress cargo telemetry.
@FunctionalInterface
interface CargoCompressor {
    String compress(Cargo cargo);
}

// Processor that builds a single stream pipeline for cargo filtering and compression.
class ManifestProcessor {
    public List<String> processManifest(List<Cargo> manifest, CargoInspector inspector, CargoCompressor compressor) {
        return manifest.stream()
                .filter(inspector::inspect)
                .filter(cargo -> cargo.valueInCredits >= 1000.0)
                .map(compressor::compress)
                .collect(Collectors.toList());
    }
}

public class Main {
    public static void main(String[] args) {
        List<Cargo> manifest = Arrays.asList(
                new StandardCargo("ALPHA-99", 5000.50, false),
                new BiologicalCargo("BETA-21", 1200.00, true, false),
                new BiologicalCargo("GAMMA-04", 2200.75, true, true),
                new StandardCargo("DELTA-33", 950.00, false)
        );

        CargoInspector inspector = cargo -> {
            if (cargo.isHazardous && cargo instanceof BiologicalCargo) {
                return ((BiologicalCargo) cargo).isShielded();
            }
            return true;
        };

        CargoCompressor compressor = cargo -> {
            String idFragment = cargo.containerId.length() >= 4 ? cargo.containerId.substring(0, 4) : cargo.containerId;
            int intValue = (int) cargo.valueInCredits;
            return idFragment + "-" + intValue;
        };

        ManifestProcessor processor = new ManifestProcessor();
        List<String> compressed = processor.processManifest(manifest, inspector, compressor);

        System.out.println("Compressed safe cargo telemetry:");
        compressed.forEach(System.out::println);
    }
}
