package com.emp.indexer;


//enable/disable tokenization stopwords  stemming  weighting   
// k1 and b for BM25 and paths to corpus and index folder
public class Config {

    // public static final String CORPUS_PATH = "corpus/";
    public static final String INDEX_PATH = "index_data/";
    public static final String TITLE_PATH = "index_data/title";
    public static final String ABSTRACT_PATH = "index_data/abstract";
    public static final String AUTHORS_PATH = "index_data/authors";
    public static final String CONTENT_PATH = "index_data/content";
    public static final String STOPWORDS_PATH = "stopwords.txt";

    public static final boolean USE_STOPWORDS = true;
    public static final boolean USE_STEMMING = true;
    
    public static final double K1 = 1.2; 
    public static final double B = 0.75;
}
