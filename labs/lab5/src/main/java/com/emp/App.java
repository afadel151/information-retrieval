package com.emp;

import java.io.IOException;
import java.util.ArrayList;

import com.emp.indexer.IndexDiskIO;
import com.emp.indexer.Indexer;
import com.emp.indexer.Stemmer;
import com.emp.indexer.StopWordsFilter;
import com.emp.indexer.Tokenizer;

/**
 * Hello world!
 */
public class App {

    public static ArrayList<arxivPaper> listOfPapers;
    public static Tokenizer tokenizer = new Tokenizer();
    public static Stemmer stemmer = new Stemmer();
    public static StopWordsFilter filter;
    static IndexDiskIO io = new IndexDiskIO();

    public static void main(String[] args) {
        
        Crawler crawler = new Crawler("https://arxiv.org/list/cs/new");
        listOfPapers = crawler.extractAllPapersData();
        PaperProessor.processNPapers(5, listOfPapers);
        try {
            filter = new StopWordsFilter("index_data/stopwords.txt");
        } catch (IOException ex) {
            System.out.println(ex);
        }
        Indexer indexer = new Indexer(tokenizer, filter, stemmer);
        try {
            indexer.buildIndex(listOfPapers);
            io.save_index_to_disk(indexer.getTermToIdMaps(), indexer.getInvertedIndexes(), listOfPapers, "index_data");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }


}
