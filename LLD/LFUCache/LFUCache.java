package LLD.LFUCache;

import java.util.HashMap;
import java.util.Map;

/**
 * Thread-safe LFU (Least Frequently Used) cache implementation.
 * Uses:
 * - keyToNode map: key -> node (for O(1) lookup)
 * - freqToList map: frequency -> doubly linked list of nodes with that frequency
 *
 * Eviction strategy:
 * - Evict least frequently used node.
 * - On ties (same frequency), evict the least recently used within that frequency,
 *   maintained by the list order (MRU at head, LRU at tail).
 */
public class LFUCache<K, V> {
    private final int capacity;
    private final Map<K, Node<K, V>> keyToNode;
    private final Map<Integer, DoublyLinkedList<K, V>> freqToList;
    private int minFreq;

    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.keyToNode = new HashMap<>();
        this.freqToList = new HashMap<>();
        this.minFreq = 0;
    }

    public synchronized V get(K key) {
        if (capacity == 0 || !keyToNode.containsKey(key)) {
            return null;
        }
        Node<K, V> node = keyToNode.get(key);
        increaseFrequency(node);
        return node.value;
    }

    public synchronized void put(K key, V value) {
        if (capacity == 0) {
            return;
        }

        if (keyToNode.containsKey(key)) {
            Node<K, V> existing = keyToNode.get(key);
            existing.value = value;
            increaseFrequency(existing);
            return;
        }

        if (keyToNode.size() == capacity) {
            evictLeastFrequent();
        }

        Node<K, V> newNode = new Node<>(key, value);
        DoublyLinkedList<K, V> list = freqToList.computeIfAbsent(1, f -> new DoublyLinkedList<>());
        list.addFirst(newNode);
        keyToNode.put(key, newNode);
        minFreq = 1;
    }

    public synchronized void remove(K key) {
        if (!keyToNode.containsKey(key)) {
            return;
        }
        Node<K, V> node = keyToNode.remove(key);
        DoublyLinkedList<K, V> list = freqToList.get(node.freq);
        if (list != null) {
            list.remove(node);
            if (list.isEmpty()) {
                freqToList.remove(node.freq);
                if (minFreq == node.freq) {
                    recalculateMinFreq();
                }
            }
        }
    }

    private void increaseFrequency(Node<K, V> node) {
        int oldFreq = node.freq;
        DoublyLinkedList<K, V> oldList = freqToList.get(oldFreq);
        if (oldList != null) {
            oldList.remove(node);
            if (oldList.isEmpty()) {
                freqToList.remove(oldFreq);
                if (minFreq == oldFreq) {
                    minFreq = oldFreq + 1;
                }
            }
        }

        int newFreq = oldFreq + 1;
        node.freq = newFreq;
        DoublyLinkedList<K, V> newList = freqToList.computeIfAbsent(newFreq, f -> new DoublyLinkedList<>());
        newList.addFirst(node);
    }

    private void evictLeastFrequent() {
        DoublyLinkedList<K, V> list = freqToList.get(minFreq);
        if (list == null) {
            return; // should not happen if invariants hold
        }
        Node<K, V> nodeToRemove = list.removeLast();
        if (nodeToRemove != null) {
            keyToNode.remove(nodeToRemove.key);
        }
        if (list.isEmpty()) {
            freqToList.remove(minFreq);
            recalculateMinFreq();
        }
    }

    private void recalculateMinFreq() {
        int newMin = Integer.MAX_VALUE;
        for (Integer freq : freqToList.keySet()) {
            if (freq < newMin) {
                newMin = freq;
            }
        }
        minFreq = freqToList.isEmpty() ? 0 : newMin;
    }
}

