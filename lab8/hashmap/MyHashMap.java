package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    @Override
    public void clear() {
        for (int i = 0; i < buckets.length; i++) {
            buckets[i].clear();
        }
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    @Override
    public V get(K key) {
        int pos = key.hashCode() & 0x7FFFFFFF % buckets.length;
        Collection<Node> bucket = buckets[pos];
        for (Node i : bucket) {
            if (i.key.equals(key)) {
                return i.value;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return size;
    }

    private void resize(int capacity) {
        Collection<Node>[] newBuckets = createTable(capacity);
        for (int i = 0; i < capacity; i++) {
            newBuckets[i] = createBucket();
        }
        for (Collection<Node> bucket : buckets) {
            for (Node node : bucket) {
                int pos = node.key.hashCode() & 0x7FFFFFFF % capacity;
                newBuckets[pos].add(node);
            }
        }
        buckets = newBuckets;
    }

    @Override
    public void put(K key, V value) {
        int pos = key.hashCode() & 0x7FFFFFFF % buckets.length;
        Collection<Node> bucket = buckets[pos];
        boolean st = false;
        for (Node i : bucket) {
            if (i.key.equals(key)) {
                i.value = value;
                st = true;
            }
        }
        if (!st) {
            Node newNode = createNode(key, value);
            bucket.add(newNode);
            size++;
            if (size > factor * buckets.length) {
                resize(buckets.length * 2);
            }
        }
    }

    @Override
    public Set<K> keySet() {
        return new KeySet();
    }

    private class KeySet extends AbstractSet<K> {

        @Override
        public Iterator<K> iterator() {
            return MyHashMap.this.iterator();
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public boolean contains(Object o) {
            return MyHashMap.this.containsKey((K) o);
        }

        @Override
        public boolean remove(Object o) {
            if (MyHashMap.this.containsKey((K) o)) {
                MyHashMap.this.remove((K) o);
                return true;
            }
            return false;
        }
    }



    @Override
    public V remove(K key) {
        int pos = key.hashCode() & 0x7FFFFFFF % buckets.length;
        Collection<Node> bucket = buckets[pos];

        Node needToremove = null;
        for (Node node : bucket) {
            if (node.key.equals(key)) {
                needToremove = node;
                break;
            }
        }
        if (needToremove != null) {
            V value = needToremove.value;
            bucket.remove(needToremove);
            size--;
            return value;
        }
        return null;
        /*
        else:
        // 获取桶的迭代器
        Iterator<Node> iterator = bucket.iterator();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            if (node.key.equals(key)) {
                V value = node.value;
                // 使用迭代器的 remove() 方法，这是安全的操作
                iterator.remove();
                size--;
                return value;
            }
        }
        */
    }

    @Override
    public V remove(K key, V value) {
        int pos = key.hashCode() & 0x7FFFFFFF % buckets.length;
        Collection<Node> bucket = buckets[pos];

        Node needToremove = null;
        for (Node node : bucket) {
            if (node.key.equals(key) && node.value.equals(value)) {
                needToremove = node;
                break;
            }
        }
        if (needToremove != null) {
            bucket.remove(needToremove);
            size--;
            return value;
        }
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return new HashMapIter();
    }

    private class HashMapIter implements Iterator<K> {

        private int nodeLeft;
        private int bucketsIndex;
        private Iterator<Node> Iter;

        public HashMapIter() {
            nodeLeft = size;
            bucketsIndex = 0;
            findNextIter();
        }

        public void findNextIter() {
            if (Iter != null && Iter.hasNext()) {
                return;
            }
            while (bucketsIndex < buckets.length) {
                Iterator<Node> temp = buckets[bucketsIndex].iterator();
                if (temp.hasNext()) {
                    Iter = temp;
                    return;
                }
                bucketsIndex++;
            }
        }

        @Override
        public boolean hasNext() {
            return nodeLeft > 0;
        }

        @Override
        public K next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            if (!Iter.hasNext()) {
                bucketsIndex++;
                findNextIter();
            }
            nodeLeft--;
            return Iter.next().key;
        }
    }

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    private int size;
    private double factor;
    // You should probably define some more!

    /** Constructors */
    public MyHashMap() {
        this(16, 0.75);
    }

    public MyHashMap(int initialSize) {
        this(initialSize, 0.75);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        buckets = createTable(initialSize);
        factor = maxLoad;
        for (int i = 0; i < initialSize; i++) {
            buckets[i] = createBucket();
        }
        size = 0;
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        return new Collection[tableSize];
    }

    // Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!

}
