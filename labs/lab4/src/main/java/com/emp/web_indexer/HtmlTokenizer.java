package com.emp.web_indexer;

import java.util.ArrayList;
import java.util.List;

public class HtmlTokenizer {
    private final String  delimiters = "<>#=,.’:;!?-_()[]{}\"\\/\n\r\t";

    public HtmlTokenizer() {}

     public List<HtmlToken> tokenize(String text) {
        List<HtmlToken> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        String currentTag = null;
        int position = 0;

        boolean insideTag = false;
        StringBuilder tagBuffer = new StringBuilder();

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
                currentTag = tagBuffer.toString().trim();   
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
