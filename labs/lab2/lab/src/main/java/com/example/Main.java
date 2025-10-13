package com.example;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        String basePath = "lab";                     // project root
        String corpusPath = basePath + "/corpus/"; // folder of .txt documents
        String stopwordsFile = basePath + "/stopwords.txt";
        String indexPath = basePath;               // where lexicon.txt, postings.txt, documents.txt live

        Tokenizer tokenizer = new Tokenizer();
        StopWordsFilter stopFilter = new StopWordsFilter(stopwordsFile);
        Stemmer stemmer = new Stemmer();
        IndexDiskIO io = new IndexDiskIO();

        Map<String, Integer> lexicon;
        Map<Integer, List<int[]>> postings;
        Map<Integer, DocumentMeta> documents;
        Map<Integer, Integer> termDf = new HashMap<>();

        //Build index if files are empty or missing, else load from disk

        boolean needBuild = Files.size(Paths.get("lab/lexicon.txt")) == 0 ||
                            Files.size(Paths.get("lab/documents.txt")) == 0 ||
                            Files.size(Paths.get("lab/postings.txt")) == 0;

        if (needBuild) {
            System.out.println("No index found — building new index...");

            Indexer indexer = new Indexer(tokenizer, stopFilter, stemmer);
            indexer.buildIndex(corpusPath);

            // load document metadata again for saving
            DocumentReader reader = new DocumentReader();
            Map<Integer, DocumentMeta> docs = reader.loadDocuments(corpusPath);

            // save the index
            io.save_index_to_disk(indexer.getTermToId(), indexer.getInvertedIndex(), docs, indexPath);
            termDf = indexer.getTermDf();

            System.out.println("Index built and saved successfully!");
        }

        IndexData data = io.read_index_from_disk(indexPath);
        lexicon = data.lexicon;
        postings = data.postings;
        documents = data.documents;
        for (var e : postings.entrySet()) {
            termDf.put(e.getKey(), e.getValue().size());
        }
        // loop
        QueryProcessor qp = new QueryProcessor();
        RetrievalEngine engine = new RetrievalEngine();
        Scanner sc = new Scanner(System.in);
        System.out.println("\n=== Information Retrieval System ===");
        System.out.println("Type a query, or 'exit' to quit.");

        while (true) {
            System.out.print("\nQuery > ");
            String query = sc.nextLine().trim();
            if (query.equalsIgnoreCase("exit")) break;
            if (query.isEmpty()) continue;

            List<Integer> queryTerms = qp.processQuery(query, lexicon, tokenizer, stopFilter, stemmer);

            if (queryTerms.isEmpty()) {
                System.out.println("No matching terms found in lexicon.");
                continue;
            }

            Map<Integer, Double> scores = engine.scoreBM25(
                    queryTerms, postings, termDf, documents, documents.size());

            // Sort and display top 10
            scores.entrySet().stream()
                    .sorted(Map.Entry.<Integer, Double>comparingByValue().reversed())
                    .limit(10)
                    .forEach(e -> {
                        DocumentMeta doc = documents.get(e.getKey());
                        System.out.printf("DocID %d | Score: %.4f | Path: %s%n",
                                e.getKey(), e.getValue(), doc.getPath());
                    });
        }
        System.out.println("Goodbye!");
        sc.close();
    }
}
