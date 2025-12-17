# LFU Cache (Least Frequently Used)

Java implementation of a thread-safe LFU cache with O(1) average-time `get`, `put`, and eviction.  
Data structures:
- `Map<K, Node<K,V>>` to locate nodes by key.
- `Map<Integer, DoublyLinkedList<K,V>>` to group nodes by frequency.
- `minFreq` tracks the smallest frequency present for O(1) eviction.

Eviction policy:
- Remove the node with the lowest frequency.
- On frequency ties, evict the least-recently-used node within that frequency bucket (tracked by the doubly linked list order).

Thread safety:
- Public operations (`get`, `put`, `remove`) are `synchronized` for simple coarse-grained safety.

Files:
- `Node.java` – key/value holder with frequency and list links.
- `DoublyLinkedList.java` – per-frequency MRU/LRU ordering.
- `LFUCache.java` – core cache logic, eviction, frequency promotion.
- `LFUCacheDemo.java` – runnable usage example.

