import java.util.*;
import java.util.stream.*;

// Base class for DNA samples.
abstract class DNASample {
    protected String sampleId;
    protected double purityPercentage;

    public DNASample(String sampleId, double purityPercentage) {
        this.sampleId = sampleId;
        this.purityPercentage = purityPercentage;
    }
}

// Human DNA sample with blood type.
class HumanSample extends DNASample {
    private String bloodType;

    public HumanSample(String sampleId, double purityPercentage, String bloodType) {
        super(sampleId, purityPercentage);
        this.bloodType = bloodType;
    }

    public String getBloodType() {
        return bloodType;
    }
}

// Alien DNA sample with silicon-based flag.
class AlienSample extends DNASample {
    private boolean isSiliconBased;

    public AlienSample(String sampleId, double purityPercentage, boolean isSiliconBased) {
        super(sampleId, purityPercentage);
        this.isSiliconBased = isSiliconBased;
    }

    public boolean isSiliconBased() {
        return isSiliconBased;
    }
}

@FunctionalInterface
interface ViabilityChecker {
    boolean isViable(DNASample sample);
}

@FunctionalInterface
interface GenomeMapper {
    String mapGenome(DNASample sample);
}

// Classifies viable DNA samples and groups them by type.
class Sequencer {
    public Map<String, List<String>> classifyGenomes(List<DNASample> samples, ViabilityChecker checker, GenomeMapper mapper) {
        return samples.stream()
                .filter(checker::isViable)
                .collect(Collectors.groupingBy(
                        sample -> sample.getClass().getSimpleName(),
                        Collectors.mapping(mapper::mapGenome, Collectors.toList())
                ));
    }
}

public class Main {
    public static void main(String[] args) {
        List<DNASample> samples = Arrays.asList(
                new HumanSample("HS-001", 92.3, "O-"),
                new HumanSample("HS-002", 78.0, "A+"),
                new AlienSample("AS-001", 85.0, true),
                new AlienSample("AS-002", 65.0, false)
        );

        ViabilityChecker checker = sample -> sample.purityPercentage >= 80.0;
        GenomeMapper mapper = sample -> {
            if (sample instanceof HumanSample) {
                return "ID: " + sample.sampleId + " (Type: " + ((HumanSample) sample).getBloodType() + ")";
            }
            return "ID: " + sample.sampleId + " (Silicon: " + ((AlienSample) sample).isSiliconBased() + ")";
        };

        Sequencer sequencer = new Sequencer();
        Map<String, List<String>> classified = sequencer.classifyGenomes(samples, checker, mapper);

        System.out.println("Classified genomes:");
        classified.forEach((type, list) -> {
            System.out.println(type + ":");
            list.forEach(item -> System.out.println(" - " + item));
        });
    }
}
