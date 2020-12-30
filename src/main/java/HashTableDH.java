import java.util.*;

public class HashTableDH<K, V> implements Map<K, V> {
    private float loadFactor;
    public int items_count = 0;
    public int deleted_count = 0;
    public Bucket[] buckets;
    private int size;
    private int threshold;

    public HashTableDH(int size, float loadFactor) {
        this.buckets = new Bucket[size];
        this.loadFactor = loadFactor;
        this.size = size;
        threshold = (int) (size * loadFactor);
    }

    public HashTableDH() {
        this(8, 0.75F);
    }

    private class Bucket<K, V> implements Map.Entry<K, V> {
        private boolean deleted = false;
        private final K key;
        private V value;

        public Bucket(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public final boolean isDeleted() {
            return deleted;
        }

        public final K getKey() {
            return key;
        }

        public final V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            return this.value = value;
        }

        public final String toString() {
            return key + "=" + value;
        }

        public final boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o instanceof Bucket) {
                Bucket<?, ?> b = (Bucket<?, ?>) o;
                if (Objects.equals(key, b.getKey()) &&
                        Objects.equals(value, getValue())) {
                    return true;
                }
            }
            return false;
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return items_count == 0;
    }

    public boolean containsKey(Object key) {
        V value = get(key);
        return value != null;
    }

    public boolean containsValue(Object value) {
        if (value == null) {
            throw new NullPointerException();
        }
        for (Bucket bucket : buckets) {
            if (bucket != null && bucket.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }

    private int hash1(Object key, int capacity) {
        int hashVal = key.hashCode();
        hashVal %= capacity;
        if (hashVal < 0) {
            hashVal += capacity;
        }
        return hashVal;
    }

    private int hash2(Object key, int capacity) {
        int hashVal = key.hashCode();
        hashVal %= capacity - 1;
        if (hashVal < 0) {
            hashVal += capacity;
        }
        if (hashVal % 2 == 0) {
            hashVal++;
        }
        return 1 + hashVal;
    }

    //Трудоемкость O(N) - в худшем,O(1) в лучшем
    //Ресурсоемоксть O(1)
    public V get(Object key) {
        int hashVal = hash1(key, size);
        int stepSize = hash2(key, size);
        int i = 0;
        while (buckets[hashVal] != null && i < size) {
            if (buckets[hashVal].getKey().equals(key) && !buckets[hashVal].isDeleted()) {
                return (V) buckets[hashVal].getValue();
            }
            hashVal += stepSize;
            hashVal %= size;
            i++;
        }
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        HashTableDH entry = (HashTableDH) m;
        for (Bucket<K, V> bucket : entry.buckets) {
            if (bucket != null && !bucket.isDeleted()) {
                put(bucket.getKey(), bucket.getValue());
            }
        }
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> entries = new HashSet<>();
        Bucket[] buckets1 = buckets;
        for (Bucket bucket : buckets1) {
            if (bucket != null && !bucket.isDeleted()) {
                entries.add(bucket);
            }
        }
        return entries;
    }

    //Трудоемкость O(N) - в худшем,O(1) в лучшем
    //Ресурсоемкость O(m) - где m - размер таблицы
    public V put(K key, V value) {
        if (this.items_count + 1 >= threshold) {
            increase();
        } else if (this.deleted_count > items_count * 2) {
            rehash();
        }
        int hashVal = hash1(key, size);
        int stepSize = hash2(key, size);
        int i = 0;
        while (buckets[hashVal] != null && !buckets[hashVal].isDeleted() && i < size) {
            if (buckets[hashVal].getKey().equals(key)) {// такой элемент уже существует
                return (V) buckets[hashVal].getKey();
            }
            hashVal += stepSize;
            hashVal %= size;
            i++;
        }
        buckets[hashVal] = new Bucket(key, value);
        items_count++;
        return (V) buckets[hashVal].getKey();
    }

    private void increase() {
        int newSize = size * 2;
        Bucket[] newBuckets = new Bucket[newSize];
        for (int i = 0; i < size; i++) {
            if (buckets[i] != null && !buckets[i].isDeleted()) {
                int hashVal = hash1(buckets[i].getKey(), newSize);
                int stepSize = hash2(buckets[i].getKey(), newSize);
                int j = 0;
                while (newBuckets[hashVal] != null && j < size) {
                    hashVal += stepSize;
                    hashVal %= newSize;
                    j++;
                }
                newBuckets[hashVal] = new Bucket(buckets[i].getKey(), buckets[i].getValue());
            }
        }
        this.buckets = newBuckets;
        this.size = newSize;
        this.threshold = (int) (size * loadFactor);
    }

    private void rehash() {
        Bucket[] newBuckets = new Bucket[size];
        for (int i = 0; i < size; i++) {
            if (buckets[i] != null && !buckets[i].isDeleted()) {
                System.out.println(buckets[i].key + " in REHASH");
                int hashVal = hash1(buckets[i].getKey(), size);
                int stepSize = hash2(buckets[i].getKey(), size);
                while (newBuckets[hashVal] != null) {
                    hashVal += stepSize;
                    hashVal %= size;
                }
                newBuckets[hashVal] = new Bucket(buckets[i].getKey(), buckets[i].getValue());
            }
        }
        buckets = newBuckets;
        deleted_count = 0;
    }


    //Трудоемкость O(N) - в худшем,O(1) в лучшем
    //Ресурсоемоксть O(1)
    public V remove(Object key) {
        int hashVal = hash1(key, size);
        int stepSize = hash2(key, size);
        int i = 0;
        while (buckets[hashVal] != null && i < size) {
            if (buckets[hashVal].getKey().equals(key) && !buckets[hashVal].isDeleted()) {
                buckets[hashVal].deleted = true;
                items_count--;
                deleted_count++;
                return (V) buckets[hashVal].getValue();
            }
            hashVal += stepSize;
            hashVal %= size;
            i++;
        }
        return null;// no key in map
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            buckets[i] = null;
        }
        items_count = 0;
        deleted_count = 0;
    }

    public Set<K> keySet() {
        Set<K> keySet = new HashSet<>();
        for (int i = 0; i < size; i++) {
            if (buckets[i] != null && !buckets[i].isDeleted()) {
                keySet.add((K) buckets[i].getKey());
            }
        }
        return keySet;
    }

    public Collection<V> values() {
        Collection<V> vs = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (buckets[i] != null && !buckets[i].isDeleted()) {
                vs.add((V) buckets[i].getValue());
            }
        }
        return vs;
    }

}
