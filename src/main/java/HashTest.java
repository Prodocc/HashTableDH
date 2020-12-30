import java.util.Arrays;

public class HashTest<V, K> {
    private float loadFactor = 0.75f;
    private final int initialSize = 8;
    private int items_count;
    private int tableSize;
    private int bucketsCount = 0;
    private Object[] buckets = new Object[initialSize];

    public HashTest() {
        this.tableSize = initialSize;
    }

    private class Bucket<K, V> {
        @Override
        public String toString() {
            return "Bucket{" +
                    "hash=" + hash +
                    ", key=" + key +
                    ", value=" + value +
                    ", next=" + next +
                    '}';
        }

        final int hash;
        final K key;
        V value;
        Bucket<K, V> next;

        public Bucket(K key, V value) {
            this.key = key;
            this.value = value;
            next = null;
            hash = hashing(key.hashCode());
        }
    }

    private int hash1(int key) {
        return key % tableSize;
    }

    private int hash2(int key) {
        return 1 + (key % tableSize - 1);
    }

    private int hashing(int hash) {
        return hash % items_count;
    }

    public V put(K key, V value) {
        Bucket<K, V> b = new Bucket<>(key, value);
        buckets[b.hash] = b;
        bucketsCount++;
        return b.value;
    }

    public void insert(K key, V value) {
        if (items_count == tableSize) {
            System.out.println("Table is full");
            return;
        }
        items_count++;
        int hashVal = hash1(key.hashCode());
        int stepSize = hash2(key.hashCode());

//        while (buckets[hashVal] != null && buckets[hashVal].)

    }

    public Object[] get() {
        return buckets;
    }

    public void asd() {
        System.out.println(Arrays.toString(buckets));
    }

    public void grow() {

    }
}
