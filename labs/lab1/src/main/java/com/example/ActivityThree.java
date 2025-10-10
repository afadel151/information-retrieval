package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class ActivityThree {

    private static String[] list_of_tokens; 
    private static String[] list_of_tokens_without_stopwords;

    public static void main(String[] args) {
        list_of_tokens = loadTokensFromDoc("corpus/doc1.txt");

        stopwords_removal("corpus/stopwords.txt");

        displayTokensWithoutStopwords();
    }

    private static String[] loadTokensFromDoc(String filePath) {
        try (InputStream inputStream = ActivityThree.class.getClassLoader().getResourceAsStream(filePath);
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            if (inputStream == null) {
                System.err.println("Resource not found: " + filePath);
                return new String[0];
            }

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append(" ");
            }

            String delimiters = " ,.’:;!?-_()[]{}\"\\/\n\r\t";
            StringTokenizer tokenizer = new StringTokenizer(sb.toString(), delimiters);

            List<String> tokensList = new ArrayList<>();
            while (tokenizer.hasMoreTokens()) {
                tokensList.add(tokenizer.nextToken().toLowerCase());
            }

            return tokensList.toArray(new String[0]);
        } catch (IOException e) {
            e.printStackTrace();
            return new String[0];
        }
    }

    public static void stopwords_removal(String stopwordsFile) {
        try (InputStream inputStream = ActivityThree.class.getClassLoader().getResourceAsStream(stopwordsFile);
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            if (inputStream == null) {
                System.err.println("Stopwords file not found: " + stopwordsFile);
                list_of_tokens_without_stopwords = list_of_tokens;
                return;
            }

            Set<String> stopwords = new HashSet<>();
            String line;
            while ((line = br.readLine()) != null) {
                stopwords.add(line.trim().toLowerCase());
            }

            List<String> filteredTokens = new ArrayList<>();
            for (String token : list_of_tokens) {
                if (!stopwords.contains(token.toLowerCase())) {
                    filteredTokens.add(token);
                }
            }

            list_of_tokens_without_stopwords = filteredTokens.toArray(new String[0]);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void displayTokensWithoutStopwords() {
        System.out.println("Tokens without stopwords (" + list_of_tokens_without_stopwords.length + "):");
        for (String token : list_of_tokens_without_stopwords) {
            System.out.println(token);
        }
    }
}
