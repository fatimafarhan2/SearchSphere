package com.ff.searchsphere.indexer;

import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class StopWordService {

    private Set<String> stopWords;

    @PostConstruct
    public void loadStopWords() throws IOException {

        ClassPathResource resource = new ClassPathResource("stopwords.txt");

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream()))) {

            stopWords = reader.lines()
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .collect(Collectors.toSet());
        }
    }

    public List<String> remove(List<String> tokens) {

        return tokens.stream()
                .filter(token -> !stopWords.contains(token))
                .toList();
    }
}