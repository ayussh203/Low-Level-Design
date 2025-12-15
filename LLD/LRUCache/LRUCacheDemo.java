package LLD.LRUCache;

public class LRUCacheDemo {
    public static void main(String[] args) {
        LRUCache<Integer, String> lruCache = new LRUCache<>(3);

        lruCache.put(1, "One");
        lruCache.put(2, "Two");
        lruCache.put(3, "Three");

        System.out.println("Get key 2: " + lruCache.get(2)); // Access key 2

        lruCache.put(4, "Four"); // This should evict key 1

        System.out.println("Get key 1 (should be null): " + lruCache.get(1)); // Should return null
        System.out.println("Get key 3: " + lruCache.get(3)); // Should return "Three"
        System.out.println("Get key 4: " + lruCache.get(4)); // Should return "Four"
    }
}
