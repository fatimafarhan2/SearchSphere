package com.ff.searchsphere.indexer;

import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Turns a page's token frequencies into Postings and adds them to
 * the in-memory InvertedIndex. Pure logic, no DB or crawling concerns,
 * so it's easy to unit test in isolation.
 */
@Component
public class InvertedIndexBuilder {

    public void addToIndex(InvertedIndex invertedIndex, String pageId, Map<String, Integer> frequencies) {

        for (Map.Entry<String, Integer> entry : frequencies.entrySet()) {

            String term = entry.getKey();
            int frequency = entry.getValue();

            invertedIndex.addPosting(term, new Posting(pageId, frequency));
        }
    }
}