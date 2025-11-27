package  com.emp.indexer;


import java.util.ArrayList;
import java.util.List;

import org.tartarus.snowball.ext.FrenchStemmer;

import com.emp.indexer.snowball.ext.porterStemmer;
import com.emp.language_detector.Detector;

public class Stemmer {

    private final porterStemmer porter = new porterStemmer();
    private final FrenchStemmer frenchStemmer = new FrenchStemmer();
    private final Detector languageDetector = new Detector();
    public String stem(String token) {
        if (token == null || token.isEmpty()) return token;
        String lang = languageDetector.detect(token);
        if ("en".equals(lang) || "x".equals(lang)) {
            porter.setCurrent(token);
            porter.stem();
            return porter.getCurrent();
        }else {
            frenchStemmer.setCurrent(token);
            frenchStemmer.stem();
            return frenchStemmer.getCurrent();
        }
    }

     public List<String> stemAll(List<String> tokens) {
        List<String> stems = new ArrayList<>(tokens.size());
        for (String token : tokens) {
            stems.add(stem(token));
        }
        return stems;
    }
}
