import java.util.HashMap;
import java.util.Map;

// A mutable key class used to demonstrate why map keys must remain immutable.
class SessionKey {
    String sessionId;

    public SessionKey(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public int hashCode() {
        return sessionId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        SessionKey other = (SessionKey) obj;
        return sessionId.equals(other.sessionId);
    }
}

public class Main {
    public static void main(String[] args) {
        Map<SessionKey, String> authTokens = new HashMap<>();
        SessionKey key = new SessionKey("USER_123");

        authTokens.put(key, "Token123");
        System.out.println("Stored token for original key: " + authTokens.get(key));

        // Mutate the key after insertion, breaking the map lookup contract.
        key.sessionId = "USER_456";

        System.out.println("Attempting lookup after mutating the key...");
        System.out.println("Lookup result: " + authTokens.get(key));
        System.out.println("Explanation: the key's hashCode changed after insertion, so the HashMap can no longer find the original bucket.");
    }
}
