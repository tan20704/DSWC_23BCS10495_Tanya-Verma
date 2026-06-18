import java.util.LinkedHashMap;
import java.util.Map;

// A small fixed-capacity cache that evicts the least recently used entry.
class VideoCache<K, V> extends LinkedHashMap<K, V> {
    private final int capacity;

    public VideoCache(int capacity) {
        super(capacity, 0.75f, true);
        this.capacity = capacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > capacity;
    }
}

public class Main {
    public static void main(String[] args) {
        VideoCache<String, String> cache = new VideoCache<>(5);

        cache.put("movie1", "Meta1");
        cache.put("movie2", "Meta2");
        cache.put("movie3", "Meta3");
        cache.put("movie4", "Meta4");
        cache.put("movie5", "Meta5");

        // Access movie1 to mark it as recently used.
        cache.get("movie1");

        // Adding the sixth movie forces eviction of the least recently used item.
        cache.put("movie6", "Meta6");

        System.out.println("Cache contents after adding 6 items and accessing movie1:");
        cache.keySet().forEach(key -> System.out.println(" - " + key));
    }
}
