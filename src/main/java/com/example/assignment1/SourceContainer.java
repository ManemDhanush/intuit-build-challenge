package com.example.assignment1;

import java.util.Optional;

/**
 * Interface representing a source of data items.
 */
public interface SourceContainer {
    /**
     * Reads the next item from the source.
     *
     * @return an Optional containing the item, or Optional.empty() if the source is exhausted.
     */
    Optional<Integer> readItem();
}
