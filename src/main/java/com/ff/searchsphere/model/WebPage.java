package com.ff.searchsphere.model;

import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "webpages")
public class WebPage {

    @Id
    private String id;

    private String url;

    private String title;

    private String content;
    private int totalWords;

    private LocalDateTime createdAt;

    private LocalDateTime lastCrawledAt;
}