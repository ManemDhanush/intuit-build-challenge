package com.example.assignment1;

/**
 * Interface representing a destination for data items.
 */
public interface DestinationContainer {
    /**
     * Writes an item to the destination.
     *
     * @param item the item to write
     */
    void writeItem(int item);
}
