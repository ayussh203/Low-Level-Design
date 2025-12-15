package LLD.LRUCache;

//import org.w3c.dom.Node;

public class Node<K,V> {
    K key;
    V value;
    Node<K,V> prev;
    Node<K,V> next;
    public Node(K key,V value) {
        this.key=key;
        this.value=value;
    }
    
}
