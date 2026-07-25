package com.ff.searchsphere.service;

import com.ff.searchsphere.model.WebPage;
import com.ff.searchsphere.repository.WebPageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class WebPageService {

    private final WebPageRepository webPageRepository;

    public WebPageService(WebPageRepository webPageRepository) {
        this.webPageRepository = webPageRepository;
    }

    public WebPage saveWebPage(WebPage webPage) {

        Optional<WebPage> existingPage = webPageRepository.findByUrl(webPage.getUrl());

        if (existingPage.isPresent()) {

            WebPage page = existingPage.get();

            page.setTitle(webPage.getTitle());
            page.setContent(webPage.getContent());
            page.setLastCrawledAt(LocalDateTime.now());

            return webPageRepository.save(page);
        }

        webPage.setCreatedAt(LocalDateTime.now());
        webPage.setLastCrawledAt(LocalDateTime.now());

        return webPageRepository.save(webPage);
    }

    public List<WebPage> getAllWebPages() {
        return webPageRepository.findAll();
    }

    public Optional<WebPage> getWebPageById(String id) {
        return webPageRepository.findById(id);
    }

    public void deleteWebPage(String id) {
        webPageRepository.deleteById(id);
    }
}