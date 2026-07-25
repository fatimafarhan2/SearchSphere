package com.ff.searchsphere.controller;

import com.ff.searchsphere.model.WebPage;
import com.ff.searchsphere.service.WebPageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pages")
public class WebPageController {

    private final WebPageService webPageService;

    public WebPageController(WebPageService webPageService) {
        this.webPageService = webPageService;
    }

    @PostMapping
    public ResponseEntity<WebPage> createWebPage(@RequestBody WebPage webPage) {

        WebPage savedPage = webPageService.saveWebPage(webPage);

        return ResponseEntity.ok(savedPage);
    }

    @GetMapping
    public ResponseEntity<List<WebPage>> getAllWebPages() {

        return ResponseEntity.ok(webPageService.getAllWebPages());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WebPage> getWebPageById(@PathVariable String id) {

        return webPageService.getWebPageById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWebPage(@PathVariable String id) {

        webPageService.deleteWebPage(id);

        return ResponseEntity.noContent().build();
    }
}