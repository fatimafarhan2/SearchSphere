package com.ff.searchsphere.search;

import com.ff.searchsphere.indexer.InvertedIndex;
import com.ff.searchsphere.indexer.Posting;
import com.ff.searchsphere.indexer.StopWordService;
import com.ff.searchsphere.indexer.Stemmer;
import com.ff.searchsphere.indexer.Tokenizer;
import com.ff.searchsphere.model.WebPage;
import com.ff.searchsphere.repository.WebPageRepository;
import org.springframework.stereotype.Service;
//import com.ff.searchsphere.SnippetExtractor;
import java.util.*;

@Service
public class SearchService {

    private final InvertedIndex invertedIndex;
    private final WebPageRepository webPageRepository;
    private final Tokenizer tokenizer;
    private final StopWordService stopWordService;
    private final Stemmer stemmer;
    private final SnippetExtractor snippetExtractor;

    public SearchService(InvertedIndex invertedIndex,
                         WebPageRepository webPageRepository,
                         Tokenizer tokenizer,
                         StopWordService stopWordService,
                         Stemmer stemmer,
                         SnippetExtractor snippetExtractor) {
        this.invertedIndex = invertedIndex;
        this.webPageRepository = webPageRepository;
        this.tokenizer = tokenizer;
        this.stopWordService = stopWordService;
        this.stemmer = stemmer;
        this.snippetExtractor = snippetExtractor;
    }

    public List<SearchResult> search(String query, String mode) {

        List<String> tokens = tokenizer.tokenize(query);
        List<String> withoutStopWords = stopWordService.remove(tokens);
        List<String> terms = stemmer.stem(withoutStopWords);

        if (terms.isEmpty()) {
            return List.of();
        }

        boolean useOr = "OR".equalsIgnoreCase(mode);

        // term -> (pageId -> frequency), also gives us df per term
        Map<String, Map<String, Integer>> termPostings = new HashMap<>();

        for (String term : terms) {
            Map<String, Integer> pageFrequencies = new HashMap<>();
            for (Posting posting : invertedIndex.getPostings(term)) {
                pageFrequencies.put(posting.getPageId(), posting.getFrequency());
            }
            termPostings.put(term, pageFrequencies);
        }

        Set<String> candidatePageIds = resolveCandidates(terms, termPostings, useOr);

        if (candidatePageIds.isEmpty()) {
            return List.of();
        }

        long totalPages = webPageRepository.count();

        List<SearchResult> results = new ArrayList<>();

        for (String pageId : candidatePageIds) {

            Optional<WebPage> pageOpt = webPageRepository.findById(pageId);

            if (pageOpt.isEmpty()) {
                continue;
            }

            WebPage page = pageOpt.get();
            double score = calculateScore(terms, termPostings, pageId, page, totalPages);

            String snippet = snippetExtractor.extract(page.getContent(), withoutStopWords);

            results.add(new SearchResult(
                    page.getTitle(),
                    page.getUrl(),
                    snippet,
                    score
            ));
        }

        results.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));

        return results;
    }

    private Set<String> resolveCandidates(List<String> terms,
                                          Map<String, Map<String, Integer>> termPostings,
                                          boolean useOr) {

        Set<String> candidates = null;

        for (String term : terms) {

            Set<String> pageIdsForTerm = termPostings.get(term).keySet();

            if (candidates == null) {
                candidates = new HashSet<>(pageIdsForTerm);
            } else if (useOr) {
                candidates.addAll(pageIdsForTerm);
            } else {
                candidates.retainAll(pageIdsForTerm);
            }
        }

        return candidates == null ? Set.of() : candidates;
    }

    private double calculateScore(List<String> terms,
                                  Map<String, Map<String, Integer>> termPostings,
                                  String pageId,
                                  WebPage page,
                                  long totalPages) {

        double score = 0.0;
        int totalWords = Math.max(page.getTotalWords(), 1);

        for (String term : terms) {

            Map<String, Integer> pageFrequencies = termPostings.get(term);
            Integer frequency = pageFrequencies.get(pageId);

            if (frequency == null) {
                continue;
            }

            int df = pageFrequencies.size();

            double tf = (double) frequency / totalWords;
            double idf = Math.log((double) (totalPages + 1) / (df + 1)) + 1;

            score += tf * idf;
        }

        return score;
    }
}