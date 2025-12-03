package com.emp.indexer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.emp.arxivPaper;

public class Indexer {

    private final Tokenizer tokenizer;
    private final StopWordsFilter stopFilter;
    private final Stemmer stemmer;
    private static final List<String> FIELDS = Arrays.asList("title", "abstract", "authors", "content");
    private final Map<String, Map<Integer, List<int[]>>> invertedIndexes = new HashMap<>();
    private final Map<String, Map<Integer, Integer>> termDfMaps = new HashMap<>();
    private final Map<String, Map<String, Integer>> termToIdMaps = new HashMap<>();
    private final Map<String, Map<Integer, String>> idToTermMaps = new HashMap<>();
    private final Map<String, Integer> nextTermIds = new HashMap<>();

    public Map<String, Map<String, Integer>> getTermToIdMaps() {
        return termToIdMaps;
    }

    public Map<String, Map<Integer, List<int[]>>> getInvertedIndexes() {
        return invertedIndexes;
    }

    public Indexer(Tokenizer tokenizer, StopWordsFilter stopFilter, Stemmer stemmer) {
        this.tokenizer = tokenizer;
        this.stopFilter = stopFilter;
        this.stemmer = stemmer;

        for (String field : FIELDS) {
            invertedIndexes.put(field, new HashMap<>());
            termDfMaps.put(field, new HashMap<>());
            termToIdMaps.put(field, new HashMap<>());
            idToTermMaps.put(field, new HashMap<>());
            nextTermIds.put(field, 0);
        }
    }

    public void buildIndex(ArrayList<arxivPaper> papers) throws IOException {

        for (arxivPaper paper : papers) {
            int docId = paper.getPaperId();

            indexField(docId, "title", paper.getTitle());
            indexField(docId, "abstract", paper.getAbstract());
            indexField(docId, "authors", paper.getAuthors());
            indexField(docId, "content", paper.getContent());
        }

        System.out.println("Index built!");
    }

    private void indexField(int docId, String field, String text) {
        if (text == null || text.isEmpty()) {
            return;
        }

        List<String> tokens = tokenizer.tokenize(text);

        if (Config.USE_STOPWORDS) {
            tokens = stopFilter.filter(tokens);
        }
        if (Config.USE_STEMMING) {
            tokens = stemmer.stemAll(tokens);
        }

        Map<String, Integer> termFreqs = computeTermFrequency(tokens);

        updateInvertedIndex(docId, termFreqs, field);
    }

    private void updateInvertedIndex(int docId, Map<String, Integer> termFreqs, String field) {

        Map<Integer, List<int[]>> invertedIndex = invertedIndexes.get(field);
        Map<Integer, Integer> termDf = termDfMaps.get(field);
        Map<String, Integer> termToId = termToIdMaps.get(field);
        Map<Integer, String> idToTerm = idToTermMaps.get(field);

        int nextId = nextTermIds.get(field);

        for (Map.Entry<String, Integer> entry : termFreqs.entrySet()) {
            String term = entry.getKey();
            int tf = entry.getValue();

            int termId;

            // If term already exists
            if (termToId.containsKey(term)) {
                termId = termToId.get(term);

            } else {
                // Create new term ID
                termId = nextId++;
                termToId.put(term, termId);
                idToTerm.put(termId, term);
            }

            // Add posting
            invertedIndex.computeIfAbsent(termId, k -> new ArrayList<>())
                    .add(new int[]{docId, tf});

            // Increment DF
            termDf.put(termId, termDf.getOrDefault(termId, 0) + 1);
        }

        // Save updated nextId
        nextTermIds.put(field, nextId);
    }

    private Map<String, Integer> computeTermFrequency(List<String> tokens) {
        Map<String, Integer> tf = new HashMap<>();

        for (String token : tokens) {
            if (token == null || token.isEmpty()) {
                continue;
            }
            tf.put(token, tf.getOrDefault(token, 0) + 1);
        }

        return tf;
    }

}
