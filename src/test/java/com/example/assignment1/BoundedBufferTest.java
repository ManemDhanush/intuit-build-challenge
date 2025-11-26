package com.example.assignment1;

import com.example.util.SimpleLogger;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

class BoundedBufferTest {

    @Test
    void testPutAndGet() throws InterruptedException {
        BoundedBuffer buffer = new BoundedBuffer(2);
        buffer.put(1);
        assertEquals(1, buffer.size());
        int item = buffer.get();
        assertEquals(1, item);
        assertEquals(0, buffer.size());
    }

    @Test
    void testBufferCapacity() {
        assertThrows(IllegalArgumentException.class, () -> new BoundedBuffer(0));
        assertThrows(IllegalArgumentException.class, () -> new BoundedBuffer(-1));
    }

    @Test
    void testBlockingBehavior() throws InterruptedException {
        BoundedBuffer buffer = new BoundedBuffer(1);
        CountDownLatch producerBlocked = new CountDownLatch(1);
        CountDownLatch consumerReady = new CountDownLatch(1);

        Thread producer = new Thread(() -> {
            try {
                buffer.put(1);
                consumerReady.countDown(); // Signal that first item is in
                buffer.put(2); // Should block here
                producerBlocked.countDown(); // Should not reach here immediately
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        
        assertTrue(consumerReady.await(1, TimeUnit.SECONDS));
        // Give a small window to ensure producer tries to put 2nd item and blocks
        // We can't deterministicly prove it BLOCKED without internal hooks, 
        // but we can prove it didn't finish yet.
        assertFalse(producerBlocked.await(100, TimeUnit.MILLISECONDS), "Producer should be blocked");
        
        buffer.get(); // Consume one, unblocking producer
        
        assertTrue(producerBlocked.await(1, TimeUnit.SECONDS), "Producer should unblock and finish");
    }

    @Test
    void testShutdown() throws InterruptedException {
        BoundedBuffer buffer = new BoundedBuffer(5);
        buffer.put(1);
        buffer.shutdown();
        
        assertTrue(buffer.isShutdown());
        
        // Should still be able to get existing items
        assertEquals(1, buffer.get());
        
        // Should throw exception when empty and shutdown
        assertThrows(IllegalStateException.class, buffer::get);
        
        // Should throw exception when putting into shutdown buffer
        assertThrows(IllegalStateException.class, () -> buffer.put(2));
    }

    @Test
    void testMultipleProducersConsumers() throws InterruptedException {
        BoundedBuffer buffer = new BoundedBuffer(10);
        int producerCount = 5;
        int itemsPerProducer = 20;
        int totalItems = producerCount * itemsPerProducer;
        
        CountDownLatch doneLatch = new CountDownLatch(totalItems);
        AtomicInteger consumedCount = new AtomicInteger(0);

        // Start Consumers
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                try {
                    while (true) {
                        try {
                            buffer.get();
                            consumedCount.incrementAndGet();
                            doneLatch.countDown();
                        } catch (IllegalStateException e) {
                            break; // Buffer shutdown
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }

        // Start Producers
        for (int i = 0; i < producerCount; i++) {
            new Thread(() -> {
                try {
                    for (int j = 0; j < itemsPerProducer; j++) {
                        buffer.put(1);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }

        // Wait for all items to be consumed
        assertTrue(doneLatch.await(5, TimeUnit.SECONDS), "All items should be consumed");
        assertEquals(totalItems, consumedCount.get());
        
        buffer.shutdown();
    }

    @Test
    void testInterruptWaitingProducer() throws InterruptedException {
        BoundedBuffer buffer = new BoundedBuffer(1);
        buffer.put(1); // Fill buffer

        Thread producer = new Thread(() -> {
            try {
                buffer.put(2); // Should block
                fail("Should have thrown InterruptedException");
            } catch (InterruptedException e) {
                // Expected
            }
        });

        producer.start();
        Thread.sleep(100); // Wait for producer to block
        producer.interrupt();
        producer.join(1000);
        
        assertFalse(producer.isAlive(), "Producer thread should have finished after interrupt");
    }

    @Test
    void testInterruptWaitingConsumer() throws InterruptedException {
        BoundedBuffer buffer = new BoundedBuffer(1);

        Thread consumer = new Thread(() -> {
            try {
                buffer.get(); // Should block
                fail("Should have thrown InterruptedException");
            } catch (InterruptedException e) {
                // Expected
            }
        });

        consumer.start();
        Thread.sleep(100); // Wait for consumer to block
        consumer.interrupt();
        consumer.join(1000);
        
        assertFalse(consumer.isAlive(), "Consumer thread should have finished after interrupt");
    }

    @Test
    void testInvalidCapacity() {
        assertThrows(IllegalArgumentException.class, () -> new BoundedBuffer(0));
        assertThrows(IllegalArgumentException.class, () -> new BoundedBuffer(-1));
    }

    @Test
    void testImmediateShutdown() throws InterruptedException {
        BoundedBuffer buffer = new BoundedBuffer(5);
        buffer.shutdown();
        assertTrue(buffer.isShutdown());
        assertThrows(IllegalStateException.class, buffer::get);
    }
}
