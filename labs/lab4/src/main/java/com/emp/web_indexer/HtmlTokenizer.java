package com.emp.web_indexer;

import java.util.ArrayList;
import java.util.List;

import com.emp.web_indexer.models.HtmlToken;

public class HtmlTokenizer {

    private final String delimiters = " <>#=,.’:;!?-_()[]{}\"\\/\n\r\t";

    public HtmlTokenizer() {
    }

    public List<HtmlToken> tokenize(String text) {
        boolean insideTag = false;
        boolean ignoreContent = false; 
        
        String currentTag = null;
        StringBuilder tagBuffer = new StringBuilder();




        List<HtmlToken> tokens = new ArrayList<>();
        
        StringBuilder current = new StringBuilder();

        int position = 0;


        text = text.replaceAll("(?is)<script.*?>.*?</script>", " ");
        text = text.replaceAll("(?is)<style.*?>.*?</style>", " ");


        for (char c : text.toCharArray()) {

            if (c == '<') {
                if (current.length() > 0) {
                    tokens.add(new HtmlToken(position++, currentTag, current.toString()));
                    current.setLength(0);
                }
                insideTag = true;
                tagBuffer.setLength(0);
                continue;
            }

            if (c == '>') {
                insideTag = false;
                String raw = tagBuffer.toString().trim();
                int space = raw.indexOf(' ');
                if (space > 0) {
                    raw = raw.substring(0, space);
                }
                if (raw.startsWith("/")) {
                    // remove / from end tag
                    raw = raw.substring(1);
                }
                currentTag = raw.toLowerCase();
                continue;
            }

            if (insideTag) {
                tagBuffer.append(c);
                continue;
            }
            if (isDelimiter(c)) {
                if (current.length() > 0) {
                    tokens.add(new HtmlToken(position++, currentTag, current.toString()));
                    current.setLength(0);
                }
            } else {
                current.append(c);
            }
        }
        if (current.length() > 0) {
            tokens.add(new HtmlToken(position, currentTag, current.toString()));
        }
        return tokens;
    }

    private boolean isDelimiter(char c) {
        return delimiters.indexOf(c) != -1;
    }
}
