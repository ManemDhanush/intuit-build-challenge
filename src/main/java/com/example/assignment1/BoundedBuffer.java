package com.example.assignment1;

import com.example.util.SimpleLogger;
import java.util.LinkedList;
import java.util.Queue;

/**
 * A thread-safe bounded buffer implementation using wait/notify mechanism.
 */
public class BoundedBuffer {
    private final Queue<Integer> queue;
    private final int capacity;
    private boolean isShutdown = false;

    public BoundedBuffer(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.capacity = capacity;
        this.queue = new LinkedList<>();
        this.isShutdown = false;
    }

    /**
     * Puts an item into the buffer. Blocks if the buffer is full.
     *
     * @param item the item to add
     * @throws InterruptedException if the thread is interrupted while waiting
     * @throws IllegalStateException if the buffer is shutdown
     */
    public void put(int item) throws InterruptedException {
        synchronized (this) {
            while (queue.size() == capacity && !isShutdown) {
                SimpleLogger.log("Buffer is full. Waiting to put " + item + "...");
                wait();
            }
            if (isShutdown) {
                throw new IllegalStateException("Buffer is shutdown");
            }
            queue.add(item);
            notifyAll();
        }
        // Logging outside synchronized block
        SimpleLogger.log("Buffer received: " + item + " | Size: " + size());
    }

    /**
     * Takes an item from the buffer. Blocks if the buffer is empty.
     *
     * @return the item removed from the buffer
     * @throws InterruptedException if the thread is interrupted while waiting
     * @throws IllegalStateException if the buffer is shutdown and empty
     */
    public int get() throws InterruptedException {
        int item;
        synchronized (this) {
            while (queue.isEmpty() && !isShutdown) {
                SimpleLogger.log("Buffer is empty. Waiting to consume...");
                wait();
            }
            if (queue.isEmpty() && isShutdown) {
                throw new IllegalStateException("Buffer is shutdown and empty");
            }
            item = queue.remove();
            notifyAll();
        }
        // Logging outside synchronized block
        SimpleLogger.log("Buffer served:   " + item + " | Size: " + size());
        return item;
    }
    
    public synchronized void shutdown() {
        isShutdown = true;
        notifyAll();
    }
    
    public synchronized boolean isShutdown() {
        return isShutdown;
    }

    public synchronized int size() {
        return queue.size();
    }
}
