package de.comparus.opensource.longmap;

import java.lang.reflect.Array;
import java.util.Arrays;

public class LongMapImpl<V> implements LongMap<V> {

    private Node<V>[] buckets;
    private int size;

    private final int RELOAD_FACTOR_AS_PERCENTS;
    private int bucketsFullness;
    private int maxBuckets;

    public LongMapImpl() {
        this(16, 75);
    }

    public LongMapImpl(int maxBuckets) {
        this(maxBuckets, 75);
    }

    public LongMapImpl(int maxBuckets, int reloadFactorAsPercents) {
        this.RELOAD_FACTOR_AS_PERCENTS = reloadFactorAsPercents < 10 || reloadFactorAsPercents > 100
                ? 75 : reloadFactorAsPercents;
        this.maxBuckets = Math.max(maxBuckets, 4);
        this.buckets = new Node[maxBuckets];
    }

    public V put(long key, V value) {
        int currentIndex = (int) Math.abs(key % this.maxBuckets);
        Node<V> current = this.buckets[currentIndex];

        while (current != null) {
            if (current.key == key) {
                current.value = value;
                return value;
            } else if (current.next == null) {
                current.next = new Node<>(key, value);
                this.size++;
                return value;
            }
            current = current.next;
        }

        this.buckets[currentIndex] = new Node<>(key, value);
        this.bucketsFullness++;
        this.size++;

        int bucketFullnessAsPercents = 100 / (this.maxBuckets / this.bucketsFullness);
        if (bucketFullnessAsPercents >= this.RELOAD_FACTOR_AS_PERCENTS) {
            reload();
        }

        return value;
    }

    public V get(long key) {
        int currentIndex = (int) Math.abs(key % this.maxBuckets);
        Node<V> current = this.buckets[currentIndex];

        while (current != null) {
            if (current.key == key) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    public V remove(long key) {
        int currentIndex = (int) Math.abs(key % this.maxBuckets);
        Node<V> current = this.buckets[currentIndex];
        Node<V> prev = this.buckets[currentIndex];


        if (current.key == key) {
            V value = current.value;
            this.buckets[currentIndex] = current.next;
            this.size--;
            return value;
        }

        while (current != null) {
            if (current.key == key) {
                prev.next = current.next;
                V value = current.value;
                this.size--;
                return value;
            }
            prev = current;
            current = current.next;
        }
        return null;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean containsKey(long key) {
        int currentIndex = (int) Math.abs(key % this.maxBuckets);
        Node<V> current = this.buckets[currentIndex];

        while (current != null) {
            if (current.key == key) {
                return true;
            }
            current = current.next;
        }

        return false;
    }

    public boolean containsValue(V value) {
        for (Node<V> current : buckets) {
            while (current != null) {
                if (current.value.equals(value)) {
                    return true;
                }
                current = current.next;
            }
        }

        return false;
    }

    public long[] keys() {
        long[] result = new long[size];
        int i = 0;
        for (Node<V> current : buckets) {
            while (current != null) {
                result[i++] = current.key;
                current = current.next;
            }
        }
        return result;
    }

    public V[] values() {
        V[] arr = null;

        int i = 0;
        for (Node<V> current : buckets) {
            while (current != null) {
                if (arr == null) {
                    arr = (V[]) Array.newInstance(current.value.getClass(), size);
                }
                arr[i++] = current.value;
                current = current.next;
            }
        }

        return arr;
    }

    public long size() {
        return size;
    }

    public void clear() {
        Arrays.fill(this.buckets, null);

        this.size = 0;
        this.maxBuckets = 16;
        this.buckets = new Node[maxBuckets];
    }

    private void reload() {
        Node<V>[] currantBuckets = Arrays.copyOf(this.buckets, this.buckets.length);
        this.maxBuckets *= 2;
        this.size = 0;
        this.buckets = new Node[this.maxBuckets];

        for (Node<V> node : currantBuckets) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private static class Node<V> {
        private long key;
        private V value;
        private Node<V> next;

        public Node(long key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
