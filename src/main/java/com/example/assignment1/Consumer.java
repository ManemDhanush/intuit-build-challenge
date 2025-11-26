package com.example.assignment1;

import com.example.util.SimpleLogger;

public class Consumer implements Runnable {
    private final BoundedBuffer buffer;
    private final DestinationContainer destination;

    public Consumer(BoundedBuffer buffer, DestinationContainer destination) {
        this.buffer = buffer;
        this.destination = destination;
    }

    @Override
    public void run() {
        try {
            while (true) {
                try {
                    int item = buffer.get();
                    SimpleLogger.log("Consuming item: " + item);
                    destination.writeItem(item);
                    // Simulate processing time
                    Thread.sleep(15); 
                } catch (IllegalStateException e) {
                    SimpleLogger.log("Consumer stopping: " + e.getMessage());
                    break;
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            SimpleLogger.log("Consumer interrupted");
        }
    }
}
