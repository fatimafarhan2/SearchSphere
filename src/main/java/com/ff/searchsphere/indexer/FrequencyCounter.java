package com.ff.searchsphere.indexer;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FrequencyCounter {

    /**
     * Counts how many times each processed token appears.
     *
     * @param tokens fully processed tokens (lowercased, stop words removed, stemmed)
     * @return map of token -> frequency
     */
    public Map<String, Integer> count(List<String> tokens) {

        Map<String, Integer> frequencies = new HashMap<>();

        for (String token : tokens) {
            frequencies.merge(token, 1, Integer::sum);
        }

        return frequencies;
    }
}