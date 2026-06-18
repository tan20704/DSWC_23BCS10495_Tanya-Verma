import java.util.LinkedHashMap;
import java.util.Map;

// LRUCache extends LinkedHashMap to automatically evict the least recently used entry.
class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private final int capacity;

    public LRUCache(int capacity) {
        super(capacity, 0.75f, true); // accessOrder = true
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }
}

public class Main {
    public static void main(String[] args) {
        LRUCache<String, String> cache = new LRUCache<>(5);

        // Add exactly five items to the cache.
        cache.put("Item1", "Movie A");
        cache.put("Item2", "Movie B");
        cache.put("Item3", "Movie C");
        cache.put("Item4", "Movie D");
        cache.put("Item5", "Movie E");

        System.out.println("Initial cache order: " + cache.keySet());

        // Access the first item so it becomes most recently used.
        cache.get("Item1");
        System.out.println("After accessing Item1: " + cache.keySet());

        // Add a sixth item. The least recently used entry should be evicted.
        cache.put("Item6", "Movie F");
        System.out.println("After adding Item6: " + cache.keySet());

        System.out.println("Expected eviction: Item2 should be gone because it was the least recently used.");
    }
}
