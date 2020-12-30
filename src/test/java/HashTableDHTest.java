import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashTableDHTest {

    HashTableDH<String, Integer> dh = new HashTableDH<>();

    @Test
    void size() {
        assertEquals(8, dh.size());
        dh.put("Q", 1);
        dh.put("QW", 2);
        dh.put("QWE", 3);
        dh.put("E", 4);
        dh.put("EW", 5);
        dh.put("EWQ", 6);
        assertEquals(16, dh.size());
    }

    @Test
    void isEmpty() {
        assertTrue(dh.isEmpty());
        dh.put("Q", 1);
        dh.put("QW", 2);
        dh.put("QWE", 3);
        dh.put("E", 4);
        dh.put("EW", 5);
        assertFalse(dh.isEmpty());
        dh.remove("Q", 1);
        dh.remove("QW", 2);
        dh.remove("QWE", 3);
        dh.remove("E", 4);
        dh.remove("EW", 5);
        assertTrue(dh.isEmpty());
    }

    @Test
    void containsKey() {
        assertFalse(dh.containsKey("Q"));
        assertFalse(dh.containsKey("QWE"));
        dh.put("Q", 1);
        dh.put("QWE", 2);
        assertTrue(dh.containsKey("Q"));
        assertTrue(dh.containsKey("QWE"));
    }

    @Test
    void containsValue() {
        dh.put("E", 4);
        dh.put("EW", 5);
        assertFalse(dh.containsValue(123));
        assertFalse(dh.containsValue(456));
        dh.put("Q", 123);
        dh.put("QWE", 456);
        assertTrue(dh.containsValue(123));
        assertTrue(dh.containsValue(456));
    }

    @Test
    void get() {
        assertNull(dh.get("Q"));
        assertNull(dh.get("QW"));
        assertNull(dh.get("QWE"));
        dh.put("Q", 1);
        dh.put("QW", 2);
        dh.put("QWE", 3);
        assertEquals(1, dh.get("Q"));
        assertEquals(2, dh.get("QW"));
        assertEquals(3, dh.get("QWE"));

    }


    @Test
    void put() {
        assertEquals(0, dh.items_count);
        dh.put("Q", 1);
        dh.put("QW", 2);
        dh.put("QWE", 3);
        dh.put("QWE", 4);
        assertEquals(3, dh.items_count);
    }


    @Test
    void remove() {
        assertEquals(0, dh.items_count);
        dh.put("Q", 1);
        dh.put("QW", 2);
        dh.put("QWE", 3);
        assertEquals(3, dh.items_count);
        dh.remove("QW");
        dh.remove("QWE");
        assertEquals(1, dh.items_count);
        assertEquals(2, dh.deleted_count);
    }

    @Test
    void clear() {
        assertEquals(0, dh.items_count);
        assertEquals(0, dh.deleted_count);
        dh.put("Q", 1);
        dh.put("QW", 2);
        dh.put("QWE", 3);
        dh.put("E", 4);
        dh.put("WE", 5);
        dh.remove("WE");
        dh.remove("QWE");
        assertEquals(3, dh.items_count);
        assertEquals(2, dh.deleted_count);
        dh.clear();
        assertEquals(0, dh.items_count);
        assertEquals(0, dh.deleted_count);
    }
}