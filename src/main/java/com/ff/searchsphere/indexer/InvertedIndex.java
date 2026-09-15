package com.ff.searchsphere.indexer;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

@Component
public class InvertedIndex {

    private final Map<String, List<Posting>> index = new HashMap<>();

    public void addPosting(String term, Posting posting) {
        index.computeIfAbsent(term, k -> new ArrayList<>()).add(posting);
    }

    public List<Posting> getPostings(String term) {
        return index.getOrDefault(term, List.of());
    }

    public boolean containsTerm(String term) {
        return index.containsKey(term);
    }

    public Map<String, List<Posting>> getIndex() {
        return index;
    }
}