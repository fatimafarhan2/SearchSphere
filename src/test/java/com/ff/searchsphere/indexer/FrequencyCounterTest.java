package com.ff.searchsphere.indexer;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FrequencyCounterTest {

    private final FrequencyCounter frequencyCounter = new FrequencyCounter();

    @Test
    void shouldCountTokenFrequencies() {

        List<String> tokens = List.of(
                "spring",
                "boot",
                "spring",
                "java",
                "spring",
                "boot"
        );

        Map<String, Integer> frequencies = frequencyCounter.count(tokens);

        assertEquals(3, frequencies.get("spring"));
        assertEquals(2, frequencies.get("boot"));
        assertEquals(1, frequencies.get("java"));
        assertEquals(3, frequencies.size());
    }
}