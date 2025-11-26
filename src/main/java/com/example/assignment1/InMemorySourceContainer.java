package com.example.assignment1;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemorySourceContainer implements SourceContainer {
    private final List<Integer> items;
    private final AtomicInteger index = new AtomicInteger(0);

    public InMemorySourceContainer(List<Integer> items) {
        this.items = items;
    }

    @Override
    public Optional<Integer> readItem() {
        int i = index.getAndIncrement();
        if (i < items.size()) {
            return Optional.of(items.get(i));
        }
        return Optional.empty();
    }
}
