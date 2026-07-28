package com.ff.searchsphere.indexer;

import org.springframework.stereotype.Component;
import org.tartarus.snowball.ext.PorterStemmer;

import java.util.List;

@Component
public class Stemmer {

    public List<String> stem(List<String> tokens) {

        PorterStemmer stemmer = new PorterStemmer();

        return tokens.stream()
                .map(token -> {
                    stemmer.setCurrent(token);
                    stemmer.stem();
                    return stemmer.getCurrent();
                })
                .toList();
    }
}