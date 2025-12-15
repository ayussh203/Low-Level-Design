## LRU Cache – Low Level Design (LLD) and Detailed Explanation

---

### 1. Problem Statement

**Objective**: Design and implement an in-memory **Least Recently Used (LRU) Cache** that:

- **Stores key–value pairs** with a fixed **capacity**.
- Supports the following operations in **O(1)** average time:
  - **`get(K key)`** – Return the value for the key if present, otherwise `null`. Accessing a key marks it as **most recently used**.
  - **`put(K key, V value)`** – Insert a new key–value pair or update an existing key. If capacity is full, **evict the least recently used** entry.
  - **`remove(K key)`** – Explicitly remove a key from the cache if present.
- Is **generic**: keys and values are of types `K` and `V` respectively.
- Is **thread-safe** for concurrent access using Java synchronization.

---

### 2.  Design Overview

The LRU cache is implemented using a combination of:

- **HashMap `<K, Node<K, V>>`**
  - Maps each key `K` directly to its corresponding `Node<K, V>` in the internal linked list.
  - Provides **O(1)** average time complexity for lookup, insertion, and deletion.

- **Custom Doubly Linked List of `Node<K, V>`**
  - Maintains the **recency order** of cache entries.
  - **Head** corresponds to the **most recently used (MRU)** end.
  - **Tail** corresponds to the **least recently used (LRU)** end.
  - Supports:
    - `addFirst(node)` – Insert node at MRU position.
    - `remove(node)` – Remove a given node from the list.
    - `moveToFront(node)` – Mark a node as MRU.
    - `removeLast()` – Remove and return the LRU node.
  - All operations run in **O(1)** time.

**Key invariant**:  
For every key present in the cache:

- There is exactly one entry in the **HashMap**.
- There is exactly one node in the **doubly linked list**.

The map and the linked list are always kept in sync by the cache methods.

---

### 3. Class-Level LLD

The LRU cache consists of the following classes in the `LLD.LRUCache` package:

- `Node<K, V>`
- `DoublyLinkedList<K, V>`
- `LRUCache<K, V>`
- `LRUCacheDemo`

#### 3.1 `Node<K, V>`

```java
public class Node<K,V> {
    K key;
    V value;
    Node<K,V> prev;
    Node<K,V> next;

    public Node(K key, V value) {
        this.key = key;
        this.value = value;
    }
}
```

**Responsibility**:

- Represents a single cache entry plus its position in the doubly linked list.

**Fields**:

- **`key`**: The cache key.
- **`value`**: The associated value.
- **`prev` / `next`**: Links to neighboring nodes in the doubly linked list.

**Design Notes**:

- Storing `key` inside the node is essential for correctly removing the key from the map when the node is evicted.
- Fields are package-private so that `LRUCache` and `DoublyLinkedList` can manipulate them directly without getters/setters, keeping the implementation simple and fast.

---

#### 3.2 `DoublyLinkedList<K, V>`

```java
public class DoublyLinkedList<K,V> {
    private final Node<K,V> head;
    private final Node<K,V> tail;

    public DoublyLinkedList() {
        head = new Node<>(null, null);
        tail = new Node<>(null, null);
        head.next = tail;
        tail.prev = head;
    }

    public void addFirst(Node<K,V> node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }

    public void remove(Node<K,V> node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    public void moveToFront(Node<K,V> node) {
        remove(node);
        addFirst(node);
    }

    public Node<K,V> removeLast() {
        if (tail.prev == head) {
            return null;
        }
        Node<K,V> last = tail.prev;
        remove(last);
        return last;
    }
}
```

**Responsibility**:

- Maintain the ordering from **most recently used** (near `head`) to **least recently used** (near `tail`).

**Key Design Choices**:

- **Sentinel `head` and `tail` nodes**:
  - They are dummy nodes that do not contain real data.
  - Initial state: `head.next = tail`, `tail.prev = head`.
  - They simplify insertion/removal logic by avoiding null checks on boundaries.

**Operations**:

- **`addFirst(node)`**:
  - Inserts the node right after `head` → node becomes MRU.
  - Time complexity: **O(1)**.

- **`remove(node)`**:
  - Unlinks the node from its neighbors by rewiring `prev.next` and `next.prev`.
  - Assumes the node is already in the list.
  - Time complexity: **O(1)**.

- **`moveToFront(node)`**:
  - Composite operation: `remove(node)` then `addFirst(node)`.
  - Used to mark a node as **most recently used** after a successful `get` or `put` update.
  - Time complexity: **O(1)**.

- **`removeLast()`**:
  - If the list only has `head` and `tail`, it returns `null` (list is empty).
  - Otherwise, removes the node just before `tail` (the **least recently used** node) and returns it.
  - Time complexity: **O(1)**.

---

#### 3.3 `LRUCache<K, V>`

```java
public class LRUCache<K,V> {
    private final int capacity;
    private final Map<K,Node<K,V>> map;
    private final DoublyLinkedList<K,V> dll;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>();
        this.dll = new DoublyLinkedList<>();
    }

    public synchronized V get(K key) {
        if (!map.containsKey(key)) {
            return null;
        }
        Node<K,V> node = map.get(key);
        dll.moveToFront(node);
        return node.value;
    }

    public synchronized void put(K key, V value) {
        if (map.containsKey(key)) {
            Node<K,V> node = map.get(key);
            node.value = value;
            dll.moveToFront(node);
        } else {
            if (map.size() == capacity) {
                Node<K,V> lru = dll.removeLast();
                if (lru != null) {
                    map.remove(lru.key);
                }
            }
            Node<K,V> newNode = new Node<>(key, value);
            dll.addFirst(newNode);
            map.put(key, newNode);
        }
    }

    public synchronized void remove(K key) {
        if (!map.containsKey(key)) {
            return;
        }
        Node<K,V> node = map.get(key);
        dll.remove(node);
        map.remove(key);
    }
}
```

**Fields**:

- **`capacity`**:
  - Maximum number of entries allowed in the cache.
- **`map`** (`Map<K, Node<K,V>>`):
  - Stores mapping from key to its corresponding node in the list.
  - Ensures O(1) average-time lookup and deletion.
- **`dll`** (`DoublyLinkedList<K, V>`):
  - Maintains recency order of elements for eviction.

---

##### 3.3.1 Constructor

- Initializes:
  - The maximum **capacity**.
  - A **`HashMap`** as the backing map.
  - An empty **doubly linked list** with sentinel head and tail.

**Initial State**:

- `map.size() == 0`
- Linked list contains only `head` and `tail`.

---

##### 3.3.2 `get(K key)`

**Intended semantics**:

1. Check if the key exists in the map:
   - If **not present**, return `null` (cache miss).
2. If present:
   - Retrieve the corresponding `Node<K, V>` from `map`.
   - Call `dll.moveToFront(node)` to mark it as **most recently used**.
   - Return `node.value`.

**Effects**:

- Keeps the LRU ordering updated based on read access.
- Recently accessed keys move to the front (MRU).

**Time Complexity**:

- `map.containsKey` + `map.get` → O(1) average.
- `moveToFront` (remove + addFirst) → O(1).
- Overall: **O(1)** average per `get`.

**Thread Safety**:

- Method is declared `synchronized`, so at most one thread can execute `get`, `put`, or `remove` on the same `LRUCache` instance at a time.

---

##### 3.3.3 `put(K key, V value)`

**Two main scenarios**:

1. **Key already exists (update)**:
   - Retrieve the existing `Node<K, V>` from `map`.
   - Update `node.value = value`.
   - Call `dll.moveToFront(node)` to mark the key as MRU.

2. **Key does not exist (insert)**:
   - If `map.size() == capacity`:
     - The cache is full → need to **evict** one entry.
     - Call `dll.removeLast()` to remove the LRU node from the list.
     - Remove the evicted node’s key from the map.
   - Create a new `Node<K, V>` with the key and value.
   - Add that node to the **front** of the list via `dll.addFirst(newNode)` (MRU position).
   - Insert key → node mapping into `map`.

**Eviction Policy**:

- When capacity is reached and a new key is inserted:
  - The **least recently used** key (at the tail of the list) is removed.

**Time Complexity**:

- Map operations: O(1) average.
- List operations (`removeLast`, `addFirst`, `moveToFront`): O(1).
- Overall: **O(1)** average per `put`.

**Thread Safety**:

- Entire method is `synchronized`, protecting:
  - Map and list updates.
  - The capacity and eviction logic.

---

##### 3.3.4 `remove(K key)`

**Steps**:

1. Check if map contains the key:
   - If not present, return immediately (no-op).
2. If present:
   - Get the corresponding node from `map`.
   - Remove the node from the doubly linked list using `dll.remove(node)`.
   - Remove the key from `map`.

**Time Complexity**:

- Map lookup + deletion: O(1) average.
- List removal: O(1).
- Overall: **O(1)**.

**Use Cases**:

- Explicit invalidation of certain keys.
- Manual control over memory usage.

**Thread Safety**:

- `synchronized` ensures no concurrent modification conflicts.

---

#### 3.4 `LRUCacheDemo`

```java
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
```

**Demonstrates**:

- Basic usage of `LRUCache<Integer, String>` with capacity `3`.
- Insertion of three entries (`1`, `2`, `3`).
- Access of key `2` (making it MRU).
- Inserting a fourth key (`4`) causes **eviction** of the least recently used key (`1`).
- Subsequent `get` calls show which keys are present or evicted.

---

### 4. Detailed LRU Behavior Example

Using the demo scenario:

1. **Initial**: capacity = 3, cache empty.

2. `put(1, "One")`  
   - Map: `{1}`  
   - List (MRU → LRU): `[1]`

3. `put(2, "Two")`  
   - Map: `{1, 2}`  
   - List: `[2, 1]` (2 is MRU)

4. `put(3, "Three")`  
   - Map: `{1, 2, 3}`  
   - List: `[3, 2, 1]`

5. `get(2)`  
   - Key `2` is present → returns `"Two"`.  
   - Moves `2` to MRU position.  
   - List: `[2, 3, 1]`

6. `put(4, "Four")` (capacity full)  
   - Need to evict LRU: node corresponding to key `1`.  
   - Evict `1` from both list and map.  
   - Insert `4` as MRU.  
   - Final Map: `{2, 3, 4}`  
   - List: `[4, 2, 3]`

7. `get(1)` → `null` (evicted).  
8. `get(3)` → `"Three"` (and `3` becomes MRU).  
9. `get(4)` → `"Four"` (and `4` becomes MRU).

This illustrates **exact LRU semantics**: the least recently *accessed* element is evicted upon capacity overflow.

---

### 5. Time and Space Complexity

- **Time Complexity per operation**:
  - `get` → **O(1)** average.
  - `put` → **O(1)** average (including eviction).
  - `remove` → **O(1)** average.

- **Space Complexity**:
  - Up to `capacity` `Node` objects plus the two sentinel nodes.
  - `HashMap` holding up to `capacity` key → node mappings.
  - Overall: **O(capacity)**.

---

### 6. Synchronization and Concurrency Design

#### 6.1 Synchronization Strategy

The `LRUCache` class is made **thread-safe** by using **coarse-grained synchronization**:

- `public synchronized V get(K key)`
- `public synchronized void put(K key, V value)`
- `public synchronized void remove(K key)`

This means:

- All public cache operations are **mutually exclusive** on a single `LRUCache` instance.
- Only one thread at a time can execute **any** of these methods.

#### 6.2 Why Synchronization is Needed

- The cache maintains **shared mutable state**:
  - `map` and `dll` must always be consistent with each other.
  - `put` may:
    - Modify both `map` and `dll`.
    - Evict entries.
  - `get` changes the recency order via `moveToFront`.
  - `remove` touches both structures.
- Without synchronization, concurrent threads could:
  - See inconsistent map/list states.
  - Corrupt the linked list pointers.
  - Break the LRU invariant.

By synchronizing methods:

- Each operation sees a **stable view** of internal state.
- All modifications are atomic with respect to other operations.

#### 6.3 Trade-offs of Coarse-Grained Locking

**Advantages**:

- Very simple to reason about.
- Hard to introduce deadlocks because only one intrinsic lock (`this`) is used.
- Ensures strong consistency between map and list.

**Disadvantages**:

- **Limited concurrency**:
  - Even purely read-like operations (`get`) that conceptually could run in parallel are serialized.
  - In high-contention scenarios, this can become a performance bottleneck.

#### 6.4 Possible Enhancements (Conceptual)

If higher concurrency is needed, several optimizations are possible conceptually:

- Use `ReentrantLock` or `ReentrantReadWriteLock` for more fine-grained locking.
- Partition the cache into **segments** (each with its own LRU and lock) to reduce contention.
- Use well-tested libraries like **Caffeine** or **Guava Cache** for production-ready concurrent caches.

For educational and interview purposes, this implementation uses a simple `synchronized` approach to keep the LLD clear and focused on the core LRU mechanism.

---

### 7. Design Trade-offs Summary

- **Map + Doubly Linked List**:
  - Classic pattern to achieve **O(1)** LRU cache operations.
  - Map provides direct access; list maintains eviction order.

- **Sentinel head/tail nodes**:
  - Simplify edge case handling for insert/remove.
  - Avoid extra conditionals and null checks in the linked list logic.

- **Generics (`K`, `V`)**:
  - Make the cache reusable for any key and value types.
  - Increase flexibility without affecting runtime complexity.

- **Coarse-Grained Synchronization**:
  - Simple and safe thread-safety mechanism.
  - Trades off scalability under heavy multi-threaded load.

---

### 8. Usage Summary

```java
LRUCache<Integer, String> cache = new LRUCache<>(3);

cache.put(1, "One");
cache.put(2, "Two");
cache.put(3, "Three");

String value = cache.get(2);  // "Two", 2 becomes MRU

cache.put(4, "Four");         // Evicts the least recently used key

cache.remove(3);              // Explicit removal of key 3
```

- Use `put` to insert or update values.
- Use `get` to fetch values and refresh recency.
- Use `remove` to explicitly invalidate entries.

This completes a detailed low-level design explanation of the **thread-safe LRU cache** in this module, including data structures, operations, eviction policy, synchronization strategy, and trade-offs.

