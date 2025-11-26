package com.example.assignment1;

import com.example.util.SimpleLogger;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Assignment1Main {
    public static void main(String[] args) {
        SimpleLogger.log("Starting Producer-Consumer Simulation...");
        
        BoundedBuffer buffer = new BoundedBuffer(5);
        
        // Setup Source
        List<Integer> data = IntStream.range(0, 20).boxed().collect(Collectors.toList());
        SourceContainer source = new InMemorySourceContainer(data);
        
        // Setup Destination
        InMemoryDestinationContainer destination = new InMemoryDestinationContainer();

        Producer producer = new Producer(buffer, source);
        Consumer consumer = new Consumer(buffer, destination);

        Thread producerThread = new Thread(producer, "Producer");
        Thread consumerThread = new Thread(consumer, "Consumer");

        producerThread.start();
        consumerThread.start();

        try {
            producerThread.join();
            while (buffer.size() > 0) {
                Thread.sleep(100);
            }
            SimpleLogger.log("Shutting down buffer...");
            buffer.shutdown();
            consumerThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            SimpleLogger.log("Main thread interrupted");
        }

        SimpleLogger.log("Simulation finished.");
        SimpleLogger.log("Consumed items count: " + destination.getConsumedItems().size());
    }
}
