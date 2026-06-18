import java.util.*;
import java.util.stream.*;

// Base class for any temporal entity.
abstract class TemporalEntity {
    protected String entityName;
    protected int originYear;

    public TemporalEntity(String entityName, int originYear) {
        this.entityName = entityName;
        this.originYear = originYear;
    }
}

// Human entity with no extra fields.
class HumanEntity extends TemporalEntity {
    public HumanEntity(String entityName, int originYear) {
        super(entityName, originYear);
    }
}

// Artifact entity with radioactivity.
class ArtifactEntity extends TemporalEntity {
    private boolean isRadioactive;

    public ArtifactEntity(String entityName, int originYear, boolean isRadioactive) {
        super(entityName, originYear);
        this.isRadioactive = isRadioactive;
    }

    public boolean isRadioactive() {
        return isRadioactive;
    }
}

// Historical event containing multiple entities.
class HistoricalEvent {
    private List<TemporalEntity> entities;
    private int eventYear;

    public HistoricalEvent(List<TemporalEntity> entities, int eventYear) {
        this.entities = entities;
        this.eventYear = eventYear;
    }

    public List<TemporalEntity> getEntities() {
        return entities;
    }

    public int getEventYear() {
        return eventYear;
    }
}

@FunctionalInterface
interface ParadoxChecker {
    boolean isParadox(TemporalEntity entity, int eventYear);
}

@FunctionalInterface
interface ThreatMapper {
    String mapThreat(TemporalEntity entity);
}

// Analyzes nested historical events and detects paradoxical entities.
class ParadoxAnalyzer {
    public List<String> detectParadoxes(List<HistoricalEvent> timeline, ParadoxChecker checker, ThreatMapper mapper) {
        return timeline.stream()
                .flatMap(event -> event.getEntities().stream()
                        .filter(entity -> checker.isParadox(entity, event.getEventYear()))
                        .map(mapper::mapThreat))
                .collect(Collectors.toList());
    }
}

public class Main {
    public static void main(String[] args) {
        List<HistoricalEvent> timeline = Arrays.asList(
                new HistoricalEvent(Arrays.asList(
                        new HumanEntity("Time Traveler", 3020),
                        new ArtifactEntity("Ancient Relic", 1500, true)
                ), 2500),
                new HistoricalEvent(Arrays.asList(
                        new HumanEntity("Future Diplomat", 3100),
                        new ArtifactEntity("Lost Device", 2800, false)
                ), 2900)
        );

        ParadoxChecker checker = (entity, eventYear) -> entity.originYear > eventYear;
        ThreatMapper mapper = entity -> entity.entityName + " detected out of time!";

        ParadoxAnalyzer analyzer = new ParadoxAnalyzer();
        List<String> paradoxes = analyzer.detectParadoxes(timeline, checker, mapper);

        System.out.println("Detected paradoxes:");
        paradoxes.forEach(System.out::println);
    }
}
