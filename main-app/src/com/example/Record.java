
package com.example;

import java.util.Arrays;

public class Record {
    public String name;
    public int[] numbers;
    public double avg;
    public int min;
    public int max;

    public Record(String name, int[] numbers) {
        this.name = name;
        this.numbers = numbers;
        this.avg = Arrays.stream(numbers).average().orElse(0);
        this.min = Arrays.stream(numbers).min().orElse(0);
        this.max = Arrays.stream(numbers).max().orElse(0);
    }

    public String toHTML() {
        return String.format(
            "<p>Name: %s</p><p>Numbers: %s</p><p>Average: %.2f</p><p>Min: %d</p><p>Max: %d</p>",
            name, Arrays.toString(numbers), avg, min, max);
    }
}
