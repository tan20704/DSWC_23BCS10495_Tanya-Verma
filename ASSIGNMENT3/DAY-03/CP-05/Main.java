import java.util.HashMap;
import java.util.Map;

class MaliciousKey {
    private final String id;

    public MaliciousKey(String id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return 1; // Force every key into the same hash bucket.
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        MaliciousKey other = (MaliciousKey) obj;
        return id.equals(other.id);
    }

    @Override
    public String toString() {
        return id;
    }
}

public class Main {
    public static void main(String[] args) {
        Map<MaliciousKey, String> map = new HashMap<>();

        for (int i = 1; i <= 10; i++) {
            map.put(new MaliciousKey("ID_" + i), "Value" + i);
        }

        System.out.println("Map size after insertion: " + map.size());
        System.out.println("Lookup test for ID_9: " + map.get(new MaliciousKey("ID_9")));
        System.out.println("Explanation: every key hashes to the same bucket, causing a long linked chain inside that bucket.");
        System.out.println("In Java 8+, the JVM may treeify this bucket once it gets large enough, converting the chain into a balanced tree to restore performance.");
    }
}
