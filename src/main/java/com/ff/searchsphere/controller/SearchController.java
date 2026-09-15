package com.ff.searchsphere.controller;

import com.ff.searchsphere.search.SearchResult;
import com.ff.searchsphere.search.SearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/search")
    public List<SearchResult> search(@RequestParam("q") String query,
                                     @RequestParam(value = "mode", defaultValue = "AND") String mode) {
        return searchService.search(query, mode);
    }
}