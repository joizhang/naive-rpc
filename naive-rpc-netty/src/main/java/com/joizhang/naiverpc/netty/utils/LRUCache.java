package com.joizhang.naiverpc.netty.utils;

import java.util.LinkedHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LRUCache<K, V> extends LinkedHashMap<K, V> {

    private static final long serialVersionUID = -5167631809472116969L;

    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private static final int DEFAULT_MAX_CAPACITY = 1000;
    private final Lock lock = new ReentrantLock();
    private volatile int maxCapacity;

    // as history list
    private final PreCache<K, Boolean> preCache;

    public LRUCache() {
        this(DEFAULT_MAX_CAPACITY);
    }

    public LRUCache(int maxCapacity) {
        super(16, DEFAULT_LOAD_FACTOR, true);
        this.maxCapacity = maxCapacity;
        this.preCache = new PreCache<>(maxCapacity);
    }

    @Override
    protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
        return size() > maxCapacity;
    }

    @Override
    public boolean containsKey(Object key) {
        lock.lock();
        try {
            return super.containsKey(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V get(Object key) {
        lock.lock();
        try {
            return super.get(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V put(K key, V value) {
        lock.lock();
        try {
            if (preCache.containsKey(key)) {
                // add it to cache
                preCache.remove(key);
                return super.put(key, value);
            } else {
                // add it to history list
                preCache.put(key, true);
                return value;
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public V remove(Object key) {
        lock.lock();
        try {
            preCache.remove(key);
            return super.remove(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public int size() {
        lock.lock();
        try {
            return super.size();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void clear() {
        lock.lock();
        try {
            preCache.clear();
            super.clear();
        } finally {
            lock.unlock();
        }
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        preCache.setMaxCapacity(maxCapacity);
        this.maxCapacity = maxCapacity;
    }

    static class PreCache<K, V> extends LinkedHashMap<K, V> {

        private volatile int maxCapacity;

        public PreCache() {
            this(DEFAULT_MAX_CAPACITY);
        }

        public PreCache(int maxCapacity) {
            super(16, DEFAULT_LOAD_FACTOR, true);
            this.maxCapacity = maxCapacity;
        }

        @Override
        protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
            return size() > maxCapacity;
        }

        public void setMaxCapacity(int maxCapacity) {
            this.maxCapacity = maxCapacity;
        }
    }

}

