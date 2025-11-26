# Assignment 1: Producer-Consumer Simulation

## Overview
This assignment implements a robust, thread-safe Producer-Consumer pattern using Java's native concurrency primitives. The goal was to demonstrate safe inter-thread communication and resource management without using high-level concurrency libraries (like `java.util.concurrent`), relying instead on `wait()` and `notifyAll()`.

## Concepts Used
1.  **Thread Synchronization**: Used `synchronized` blocks to ensure that only one thread accesses the shared buffer at a time.
2.  **Inter-Thread Communication**: Used `wait()` to pause threads when the buffer is full (Producer) or empty (Consumer), and `notifyAll()` to wake them up when the state changes.
3.  **Graceful Shutdown**: Implemented a "Poison Pill" strategy where a shutdown flag signals threads to stop processing and exit loops cleanly.
4.  **Interface Segregation**: Decoupled the data source and destination from the threading logic using `SourceContainer` and `DestinationContainer` interfaces.

## Implementation Approach
1.  **BoundedBuffer**: The core component. It manages a fixed-size queue.
    *   **Blocking Logic**: `put()` blocks if full; `get()` blocks if empty.
    *   **Safety**: All state mutations happen within `synchronized(this)` blocks.
    *   **Shutdown**: A `shutdown()` method sets a flag and wakes up all waiting threads so they can check the flag and exit (throwing `IllegalStateException` to signal the end).
2.  **Producer & Consumer**: Runnable tasks that rely on the `BoundedBuffer`. They handle the `IllegalStateException` as a signal to terminate their run loops.
3.  **Logging**: A custom `SimpleLogger` was introduced to print thread-safe, timestamped logs, making it easier to trace the interleaved execution of threads.

## Testing Strategy (`BoundedBufferTest`)
We implemented comprehensive unit tests to verify correctness under concurrency:
*   **Basic Functionality**: Verifies that items put into the buffer are retrieved in the same order (FIFO).
*   **Blocking Behavior**: Tests that a Producer waits when the buffer is full and a Consumer waits when it is empty.
*   **Multiple Threads**: A stress test with 5 Producers and 3 Consumers ensures no data is lost or corrupted under high load.
*   **Graceful Shutdown**: Verifies that calling `shutdown()` allows waiting threads to exit and prevents new operations.
*   **Edge Cases**:
    *   **Invalid Capacity**: Ensures buffer cannot be created with size <= 0.
    *   **Immediate Shutdown**: Ensures the system handles shutdown requests even before processing starts.
