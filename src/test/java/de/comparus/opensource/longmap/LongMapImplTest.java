package de.comparus.opensource.longmap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class LongMapImplTest {

    private LongMapImpl<String> map;

    @BeforeEach
    void initMap() {
        map = new LongMapImpl<>();

        map.put(34534, "test34534");
        map.put(4545, "test4545");
        map.put(6263, "test6263");
        map.put(76445, "test76445");
        map.put(34363, "test34363");
        map.put(23, "test23");
        map.put(1, "test1");
        map.put(0, "test0");
    }

    @Test
    void put() {
        assertEquals(8, map.size());
        String res = map.put(123123, "test123123");
        assertEquals("test123123", res);
        assertEquals(9, map.size());
    }

    @Test
    void get() {
        assertEquals("test34534", map.get(34534));
        assertEquals("test6263", map.get(6263));
        assertEquals("test34363", map.get(34363));
        assertEquals("test0", map.get(0));
    }

    @Test
    void remove() {
        assertEquals("test23", map.remove(23));
        assertEquals("test1", map.remove(1));
        assertEquals("test0", map.remove(0));
        assertEquals(5, map.size());
    }

    @Test
    void containsKey() {
        assertTrue(map.containsKey(23));
        assertTrue(map.containsKey(6263));
        assertFalse(map.containsKey(15));
        assertFalse(map.containsKey(2));
    }

    @Test
    void containsValue() {
        assertTrue(map.containsValue("test4545"));
        assertTrue(map.containsValue("test34363"));
        assertFalse(map.containsValue("asd"));
        assertFalse(map.containsValue("abc"));
    }

    @Test
    void keys() {
        long[] expected = {
                34534,
                0,
                1,
                23,
                34363,
                76445,
                6263,
                4545
        };
        long[] actual = map.keys();

        Arrays.sort(expected);
        Arrays.sort(actual);

        assertArrayEquals(expected, actual);
    }

    @Test
    void values() {
        String[] expected = {
                "test0",
                "test1",
                "test23",
                "test34363",
                "test76445",
                "test6263",
                "test4545",
                "test34534"
        };

        String[] actual = map.values();

        Arrays.sort(expected);
        Arrays.sort(actual);

        assertArrayEquals(expected, actual);
    }

    @Test
    void clear() {
        map.clear();

        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
        assertFalse(map.containsKey(0));
        assertFalse(map.containsValue("test0"));
    }
}