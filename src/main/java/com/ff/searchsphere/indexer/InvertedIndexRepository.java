package com.ff.searchsphere.indexer;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface InvertedIndexRepository extends MongoRepository<InvertedIndexDocument, String> {
}