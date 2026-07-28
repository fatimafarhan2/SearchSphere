package com.ff.searchsphere.indexer;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class Tokenizer {

    public List<String> tokenize(String text) {

        if (text == null || text.isBlank()) {
            return List.of();
        }

        String normalizedText = text
                .toLowerCase()
                .replaceAll("[^a-z0-9\\s]", " ");

        return Arrays.stream(normalizedText.trim().split("\\s+"))
                .filter(token -> !token.isBlank())
                .toList();
    }
}