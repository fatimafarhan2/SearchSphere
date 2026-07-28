package com.ff.searchsphere.indexer;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StemmerTest {

    private final Stemmer stemmer = new Stemmer();

    @Test
    void shouldStemWords() {

        List<String> result = stemmer.stem(
                List.of(
                        "running",
                        "runs",
                        "runner",
                        "cats",
                        "studies",
                        "studying"
                )
        );

        assertEquals(
                List.of(
                        "run",
                        "run",
                        "runner",
                        "cat",
                        "studi",
                        "studi"
                ),
                result
        );
    }
}