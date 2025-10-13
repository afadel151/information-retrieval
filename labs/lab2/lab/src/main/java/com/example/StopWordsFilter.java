package com.example;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class StopWordsFilter {
    private final Set<String> stopwords;
    //List<String> filter(List<String>)

    public StopWordsFilter(String stopwordsFile) throws IOException {
        stopwords = new HashSet<>();

        // Read all  and add to the set (lowercased)
        List<String> lines = Files.readAllLines(Paths.get(stopwordsFile));
        for (String line : lines) {
            String word = line.trim().toLowerCase();
            if (!word.isEmpty()) {
                stopwords.add(word);
            }
        }
        System.out.println("Loaded " + stopwords.size() + " stop words from " + stopwordsFile);
    }

    public List<String> filter(List<String> tokens) {
        List<String> filtered = new ArrayList<>();
        for (String token : tokens) {
            if (!stopwords.contains(token)) {
                filtered.add(token);
            }
        }
        return filtered;
    }

}
