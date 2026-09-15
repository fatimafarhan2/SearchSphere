package com.ff.searchsphere.search;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchResult {

    private String title;

    private String url;

    private String snippet;

    private double score;
}