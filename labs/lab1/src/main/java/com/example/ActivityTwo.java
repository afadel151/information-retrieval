package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ActivityTwo {

    private static String[] list_of_tokens;

    public static void main(String[] args) {
        tokenizeDocument("corpus/doc1.txt"); 
        displayTokens();
    }

    public static void tokenizeDocument(String filePath) {
        try {
            StringBuilder sb;
            try (
                    InputStream inputStream = ActivityTwo.class.getClassLoader().getResourceAsStream(filePath); BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                if (inputStream == null) {
                    System.err.println("Resource not found: " + filePath);
                    return;
                }
                sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append(" ");
                }
            }

            String delimiters = " ,.’:;!?-_()[]{}\"\\/\n\r\t";

            StringTokenizer tokenizer = new StringTokenizer(sb.toString(), delimiters);

            List<String> tokensList = new ArrayList<>();
            while (tokenizer.hasMoreTokens()) {
                tokensList.add(tokenizer.nextToken());
            }

            list_of_tokens = tokensList.toArray(new String[0]);

        } catch (IOException e) {
        }
    }

    public static void displayTokens() {
        System.out.println("Number of tokens: " + list_of_tokens.length);
        for (String token : list_of_tokens) {
            System.out.println(token);
        }
    }

}
