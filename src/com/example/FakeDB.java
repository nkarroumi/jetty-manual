
package com.example;

import java.util.*;

public class FakeDB {
    private final List<Record> records = new ArrayList<>();

    public void save(Record r) {
        records.add(r);
    }

    public Record findByName(String name) {
        return records.stream()
            .filter(r -> r.name.equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
    }
}
