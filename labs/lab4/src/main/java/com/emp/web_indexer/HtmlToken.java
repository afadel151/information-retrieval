package com.emp.web_indexer;

public class HtmlToken {
    public final String term ;
    public final String tag; 
    public final int position; 


    public HtmlToken(int position, String tag, String term) {
        this.position = position;
        this.tag = tag;
        this.term = term;
    }
}