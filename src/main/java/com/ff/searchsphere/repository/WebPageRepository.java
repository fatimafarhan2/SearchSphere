package com.ff.searchsphere.repository;

import com.ff.searchsphere.model.WebPage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface WebPageRepository extends MongoRepository<WebPage, String> {

    Optional<WebPage> findByUrl(String url);

    boolean existsByUrl(String url);

}