package com.example.assignment1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InMemoryDestinationContainer implements DestinationContainer {
    private final List<Integer> consumedItems = Collections.synchronizedList(new ArrayList<>());

    @Override
    public void writeItem(int item) {
        consumedItems.add(item);
    }

    public List<Integer> getConsumedItems() {
        return new ArrayList<>(consumedItems);
    }
}
