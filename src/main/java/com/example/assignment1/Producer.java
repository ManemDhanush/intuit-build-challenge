package com.example.assignment1;

import com.example.util.SimpleLogger;
import java.util.Optional;

public class Producer implements Runnable {
    private final BoundedBuffer buffer;
    private final SourceContainer source;

    public Producer(BoundedBuffer buffer, SourceContainer source) {
        this.buffer = buffer;
        this.source = source;
    }

    @Override
    public void run() {
        try {
            while (!buffer.isShutdown()) {
                Optional<Integer> itemOpt = source.readItem();
                if (itemOpt.isPresent()) {
                    try {
                        SimpleLogger.log("Producing item: " + itemOpt.get());
                        buffer.put(itemOpt.get());
                        // Simulate work
                        Thread.sleep(10); 
                    } catch (IllegalStateException e) {
                        SimpleLogger.log("Producer stopping: " + e.getMessage());
                        break;
                    }
                } else {
                    SimpleLogger.log("Source exhausted. Producer stopping.");
                    break;
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            SimpleLogger.log("Producer interrupted");
        }
    }
}
