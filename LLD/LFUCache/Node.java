package LLD.LFUCache;

public class Node<K, V> {
    K key;
    V value;
    int freq;
    Node<K, V> prev;
    Node<K, V> next;

    public Node(K key, V value) {
        this.key = key;
        this.value = value;
        this.freq = 1; // new nodes start with frequency 1
    }
}

