package com.ff.searchsphere.indexer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents one occurrence of a term in a specific document,
 * with how many times it appeared there.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Posting {

    private String pageId;

    private int frequency;
}