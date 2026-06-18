import java.util.*;
import java.util.stream.*;

// Base class for engine telemetry logs.
abstract class EngineLog {
    protected String timestamp;
    protected double coreTemperature;
    protected boolean isAnomaly;

    public EngineLog(String timestamp, double coreTemperature, boolean isAnomaly) {
        this.timestamp = timestamp;
        this.coreTemperature = coreTemperature;
        this.isAnomaly = isAnomaly;
    }
}

// Nominal log entry without extra fields.
class NominalLog extends EngineLog {
    public NominalLog(String timestamp, double coreTemperature, boolean isAnomaly) {
        super(timestamp, coreTemperature, isAnomaly);
    }
}

// Critical log entry with an error code.
class CriticalLog extends EngineLog {
    private String errorCode;

    public CriticalLog(String timestamp, double coreTemperature, boolean isAnomaly, String errorCode) {
        super(timestamp, coreTemperature, isAnomaly);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}

@FunctionalInterface
interface LogAuditor {
    boolean audit(EngineLog log);
}

@FunctionalInterface
interface HeatExtractor {
    double extractHeat(EngineLog log);
}

// Processor that converts logs into a primitive double stream and finds the peak temperature.
class TelemetryProcessor {
    public double getPeakCriticalTemp(List<EngineLog> logs, LogAuditor auditor, HeatExtractor extractor) {
        return logs.stream()
                .filter(auditor::audit)
                .mapToDouble(extractor::extractHeat)
                .max()
                .orElse(0.0);
    }
}

public class Main {
    public static void main(String[] args) {
        List<EngineLog> logs = Arrays.asList(
                new NominalLog("2026-06-09T12:00:00Z", 800.5, false),
                new CriticalLog("2026-06-09T12:01:00Z", 1200.0, false, "OVERHEAT"),
                new CriticalLog("2026-06-09T12:02:00Z", 950.0, true, "LEAK"),
                new NominalLog("2026-06-09T12:03:00Z", 760.0, true)
        );

        LogAuditor auditor = log -> log.isAnomaly || (log instanceof CriticalLog && "OVERHEAT".equals(((CriticalLog) log).getErrorCode()));
        HeatExtractor extractor = log -> log.coreTemperature;

        TelemetryProcessor processor = new TelemetryProcessor();
        double peakTemp = processor.getPeakCriticalTemp(logs, auditor, extractor);

        System.out.println("Peak critical temperature: " + peakTemp);
    }
}
