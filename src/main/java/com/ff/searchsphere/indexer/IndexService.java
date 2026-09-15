package com.ff.searchsphere.indexer;

import com.ff.searchsphere.model.WebPage;
import com.ff.searchsphere.repository.WebPageRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class IndexService {

    private final WebPageRepository webPageRepository;
    private final InvertedIndexRepository invertedIndexRepository;
    private final InvertedIndex invertedIndex;
    private final InvertedIndexBuilder invertedIndexBuilder;
    private final Tokenizer tokenizer;
    private final StopWordService stopWordService;
    private final Stemmer stemmer;
    private final FrequencyCounter frequencyCounter;

    public IndexService(WebPageRepository webPageRepository,
                        InvertedIndexRepository invertedIndexRepository,
                        InvertedIndex invertedIndex,
                        InvertedIndexBuilder invertedIndexBuilder,
                        Tokenizer tokenizer,
                        StopWordService stopWordService,
                        Stemmer stemmer,
                        FrequencyCounter frequencyCounter) {
        this.webPageRepository = webPageRepository;
        this.invertedIndexRepository = invertedIndexRepository;
        this.invertedIndex = invertedIndex;
        this.invertedIndexBuilder = invertedIndexBuilder;
        this.tokenizer = tokenizer;
        this.stopWordService = stopWordService;
        this.stemmer = stemmer;
        this.frequencyCounter = frequencyCounter;
    }

    @PostConstruct
    public void initializeIndex() {

        List<InvertedIndexDocument> persisted = invertedIndexRepository.findAll();

        if (!persisted.isEmpty()) {
            loadIndexFromMongo(persisted);
        } else {
            buildIndexFromPages();
            persistIndex();
        }
    }

    private void loadIndexFromMongo(List<InvertedIndexDocument> persisted) {

        for (InvertedIndexDocument doc : persisted) {
            for (Posting posting : doc.getPostings()) {
                invertedIndex.addPosting(doc.getTerm(), posting);
            }
        }
    }

    private void buildIndexFromPages() {

        List<WebPage> pages = webPageRepository.findAll();

        for (WebPage page : pages) {

            List<String> tokens = tokenizer.tokenize(page.getContent());
            List<String> withoutStopWords = stopWordService.remove(tokens);
            List<String> stemmed = stemmer.stem(withoutStopWords);

            page.setTotalWords(stemmed.size());
            webPageRepository.save(page);

            Map<String, Integer> frequencies = frequencyCounter.count(stemmed);

            invertedIndexBuilder.addToIndex(invertedIndex, page.getId(), frequencies);
        }
    }

    private void persistIndex() {

        List<InvertedIndexDocument> documents = new ArrayList<>();

        for (Map.Entry<String, List<Posting>> entry : invertedIndex.getIndex().entrySet()) {
            documents.add(new InvertedIndexDocument(entry.getKey(), entry.getValue()));
        }

        invertedIndexRepository.saveAll(documents);
    }
}