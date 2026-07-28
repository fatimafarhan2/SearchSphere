package com.ff.searchsphere.indexer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StopWordServiceTest {

    private StopWordService stopWordService;

    @BeforeEach
    void setUp() throws Exception {
        stopWordService = new StopWordService();
        stopWordService.loadStopWords();
    }

    @Test
    void shouldRemoveStopWords() {

        List<String> tokens = List.of(
                "spring",
                "boot",
                "makes",
                "the",
                "java",
                "and",
                "easy"
        );

        List<String> result = stopWordService.remove(tokens);

        assertEquals(
                List.of(
                        "spring",
                        "boot",
                        "java",
                        "easy"
                ),
                result
        );
    }
}