package com.ff.searchsphere.crawler;

public class CrawlTask {

    private final String url;
    private final int depth;

    public CrawlTask(String url, int depth) {
        this.url = url;
        this.depth = depth;
    }

    public String getUrl() {
        return url;
    }

    public int getDepth() {
        return depth;
    }
}