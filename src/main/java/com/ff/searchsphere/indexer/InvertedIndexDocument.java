package com.ff.searchsphere.indexer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Persistent representation of one inverted index entry: a term
 * and every posting (page + frequency) that contains it.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "inverted_index")
public class InvertedIndexDocument {

    @Id
    private String term;

    private List<Posting> postings;
}