import java.util.*;
import java.util.stream.*;

// Base class representing a memory engram.
abstract class MemoryEngram {
    protected String engramId;
    protected double clarityScore;
    protected boolean isCorrupted;

    public MemoryEngram(String engramId, double clarityScore, boolean isCorrupted) {
        this.engramId = engramId;
        this.clarityScore = clarityScore;
        this.isCorrupted = isCorrupted;
    }
}

// Standard engram without extra clearance requirements.
class StandardEngram extends MemoryEngram {
    public StandardEngram(String engramId, double clarityScore, boolean isCorrupted) {
        super(engramId, clarityScore, isCorrupted);
    }
}

// Classified engram with security clearance.
class ClassifiedEngram extends MemoryEngram {
    private int securityClearanceLevel;

    public ClassifiedEngram(String engramId, double clarityScore, boolean isCorrupted, int securityClearanceLevel) {
        super(engramId, clarityScore, isCorrupted);
        this.securityClearanceLevel = securityClearanceLevel;
    }

    public int getSecurityClearanceLevel() {
        return securityClearanceLevel;
    }
}

// Custom functional interface to validate memories.
@FunctionalInterface
interface EngramValidator {
    boolean validate(MemoryEngram engram);
}

// Custom functional interface to translate memories.
@FunctionalInterface
interface EngramTranslator {
    String translate(MemoryEngram engram);
}

// Processor class that handles the memory stream pipeline.
class CortexProcessor {
    public List<String> processMemories(List<MemoryEngram> engrams, EngramValidator validator, EngramTranslator translator) {
        return engrams.stream()
                .filter(validator::validate)
                .filter(e -> e.clarityScore >= 50.0)
                .map(translator::translate)
                .collect(Collectors.toList());
    }
}

public class Main {
    public static void main(String[] args) {
        List<MemoryEngram> engrams = Arrays.asList(
                new StandardEngram("E-001", 75.0, false),
                new ClassifiedEngram("E-002", 85.5, false, 4),
                new ClassifiedEngram("E-003", 90.0, false, 2),
                new StandardEngram("E-004", 45.0, false)
        );

        EngramValidator validator = engram -> {
            if (engram.isCorrupted) {
                return false;
            }
            if (engram instanceof ClassifiedEngram) {
                return ((ClassifiedEngram) engram).getSecurityClearanceLevel() <= 3;
            }
            return true;
        };

        EngramTranslator translator = engram -> "ENGRAM-" + engram.engramId + " | CLARITY: " + engram.clarityScore + "%";

        CortexProcessor processor = new CortexProcessor();
        List<String> safeMemories = processor.processMemories(engrams, validator, translator);

        System.out.println("Safe memory translations:");
        safeMemories.forEach(System.out::println);
    }
}
