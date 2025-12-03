package com.emp;

import java.util.ArrayList;

public class PaperProessor {
    public PaperProessor(){}


    public static void processNPapers(int N,ArrayList<arxivPaper> papers)
    {
        if (N > papers.size()) {
            System.out.println("N est superieur au nombre des papiers");
            return;
        }
        for (int idx = 0; idx < N; idx++) {
            
            arxivPaper paper = papers.get(idx);
            paper.downloadPDF();
            paper.extractContentfromPDF();
        }
    }
}
