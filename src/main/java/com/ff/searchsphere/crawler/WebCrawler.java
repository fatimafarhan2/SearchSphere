package com.ff.searchsphere.crawler;

import java.util.*;
import java.io.IOException;
import java.net.URI;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import com.ff.searchsphere.service.WebPageService;
import com.ff.searchsphere.model.WebPage;


@Component
public class WebCrawler {

    private final WebPageService webPageService;

    private final Queue<CrawlTask> queue = new ArrayDeque<>();
    private final Set<String> visited = new HashSet<>();

    public WebCrawler(WebPageService webPageService) {
        this.webPageService = webPageService;
    }
    public void crawl(String seedUrl, int maxDepth, int maxPages) {

        queue.clear();
        visited.clear();

        int pagesCrawled = 0;

        queue.offer(new CrawlTask(seedUrl, 0));
        visited.add(seedUrl);

        String baseDomain = URI.create(seedUrl).getHost().toLowerCase();

        while (!queue.isEmpty() && pagesCrawled < maxPages) {

            CrawlTask current = queue.poll();

            if (crawlPage(current, baseDomain, maxDepth)) {
                pagesCrawled++;
            }
        }
    }

    private boolean crawlPage(CrawlTask current,
                              String baseDomain,
                              int maxDepth) {

        try {

            Connection.Response response = Jsoup
                    .connect(current.getUrl())
                    .timeout(5000)
                    .userAgent("SearchSphereBot/1.0")
                    .execute();

            Document document = response.parse();

            try {
                Thread.sleep(300);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }

            savePage(current, document);

            processLinks(document, current, baseDomain, maxDepth);

            return true;

        } catch (IOException e) {

            System.out.println("Failed to crawl: " + current.getUrl());

            return false;
        }
    }

    private void savePage(CrawlTask current, Document document) {

        WebPage page = new WebPage();

        page.setUrl(current.getUrl());
        page.setTitle(document.title());
        page.setContent(document.body().text());

        webPageService.saveWebPage(page);
    }

    private void processLinks(Document document,
                              CrawlTask current,
                              String baseDomain,
                              int maxDepth) {

        Elements links = document.select("a[href]");

        for (Element link : links) {

            String url = normalizeUrl(link.absUrl("href"));

            if (url.isBlank()) {
                continue;
            }

            if (url.contains("<") || url.contains(">")) {
                continue;
            }

            String host;

            try {
                host = URI.create(url).getHost();
            } catch (IllegalArgumentException e) {

                System.out.println("Invalid URL: " + url);
                continue;
            }

            if (host == null) {
                continue;
            }

            host = host.toLowerCase();

            if (host.equals(baseDomain) || host.endsWith("." + baseDomain)) {

                if (!visited.contains(url)) {

                    visited.add(url);

                    if (current.getDepth() < maxDepth) {

                        queue.offer(new CrawlTask(url,
                                current.getDepth() + 1));
                    }

                    System.out.println("Queued: " + url);
                }
            }
        }
    }


    private String normalizeUrl(String url) {

        int fragmentIndex = url.indexOf('#');

        if (fragmentIndex != -1) {
            url = url.substring(0, fragmentIndex);
        }

        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }

        return url;
    }
}