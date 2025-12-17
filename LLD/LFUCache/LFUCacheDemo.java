package LLD.LFUCache;

public class LFUCacheDemo {
    public static void main(String[] args) {
        LFUCache<Integer, String> lfuCache = new LFUCache<>(3);

        lfuCache.put(1, "One");   // freq 1
        lfuCache.put(2, "Two");   // freq 1
        lfuCache.put(3, "Three"); // freq 1

        // Access keys to change frequencies
        lfuCache.get(1); // freq(1) = 2
        lfuCache.get(1); // freq(1) = 3
        lfuCache.get(2); // freq(2) = 2

        // Insert a new key, should evict key 3 (freq 1, LRU among freq 1)
        lfuCache.put(4, "Four");

        System.out.println("Get key 3 (should be null): " + lfuCache.get(3));
        System.out.println("Get key 1 (should be 'One'): " + lfuCache.get(1));
        System.out.println("Get key 2 (should be 'Two'): " + lfuCache.get(2));
        System.out.println("Get key 4 (should be 'Four'): " + lfuCache.get(4));
    }
}

