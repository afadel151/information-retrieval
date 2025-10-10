package com.example;

import java.util.ArrayList;
import java.util.List;

public class ActivityFour {

    public static void main(String[] args) {
        String[] list_of_tokens_without_stopwords = {"the", "quick", "brown", "fox", "jumps", "over", "lazy", "dog"};
        int truncationSize = 3; 
        displayStems(list_of_tokens_without_stopwords, truncationSize);
    }
   

    public static String[] stemming(String[] list_of_tokens_without_stopwords, int truncationSize) {
         String[] list_of_stems;
        List<String> stemsList = new ArrayList<>();

        for (String token : list_of_tokens_without_stopwords) {
            if (token.length() <= truncationSize) {
                stemsList.add(token);
            } else {
                stemsList.add(token.substring(0, truncationSize));
            }
        }

        list_of_stems = stemsList.toArray(new String[0]);

        return list_of_stems;
    }

    public static void displayStems(String[] list_of_tokens_without_stopwords, int truncationSize) {
        String[] list_of_stems = stemming(list_of_tokens_without_stopwords, truncationSize);
        System.out.println("List of stems (" + list_of_stems.length + "):");
        for (String stem : list_of_stems) {
            System.out.println(stem);
        }
    }
}
