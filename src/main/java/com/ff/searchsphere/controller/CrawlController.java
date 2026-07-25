package com.ff.searchsphere.controller;

import com.ff.searchsphere.crawler.WebCrawler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CrawlController {

    private final WebCrawler webCrawler;

    public CrawlController(WebCrawler webCrawler) {
        this.webCrawler = webCrawler;
    }

    @GetMapping("/crawl")
    public String crawl() {

        webCrawler.crawl("https://spring.io", 1, 5);

        return "Crawler finished.";
    }
}