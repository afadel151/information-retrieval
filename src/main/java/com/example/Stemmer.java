package com.example;


import java.util.ArrayList;
import java.util.List;
import com.example.snowball.ext.porterStemmer;;

public class Stemmer {

    private final porterStemmer porter = new porterStemmer();

    public String stem(String token) {
        if (token == null || token.isEmpty()) return token;
        porter.setCurrent(token);
        porter.stem();
        return porter.getCurrent();
    }

     public List<String> stemAll(List<String> tokens) {
        List<String> stems = new ArrayList<>(tokens.size());
        for (String token : tokens) {
            stems.add(stem(token));
        }
        return stems;
    }
}
