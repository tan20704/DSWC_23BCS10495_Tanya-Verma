class HardwareLockException
        extends Exception {

    HardwareLockException(String msg) {
        super(msg);
    }
}

class SensorCorruptionException
        extends RuntimeException {

    SensorCorruptionException(String msg) {
        super(msg);
    }
}

class TelemetryStream
        implements AutoCloseable {

    public void read() {
        System.out.println("Reading Data...");
    }

    public void close() {
        System.out.println("Stream Closed");
    }
}

public class Prob4Day1
{

    static void parse()
            throws HardwareLockException {

        try(TelemetryStream t =new TelemetryStream()) {

            t.read();

            throw new HardwareLockException("File Locked");
        }
    }

    public static void main(String[] args) {

        try {
            parse();
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}