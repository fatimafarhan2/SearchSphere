package com.ff.searchsphere.indexer;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TokenizerTest {

    private final Tokenizer tokenizer = new Tokenizer();

    @Test
    void shouldTokenizeTextCorrectly() {

        List<String> tokens = tokenizer.tokenize(
                "Spring Boot makes Java easy!"
        );

        assertEquals(
                List.of("spring", "boot", "makes", "java", "easy"),
                tokens
        );
    }
    @Test
    void shouldRemoveExtraSpacesAndPunctuation() {

        List<String> tokens = tokenizer.tokenize(
                "Hello,,,     World!!! Java-17"
        );

        assertEquals(
                List.of("hello", "world", "java", "17"),
                tokens
        );
    }
}