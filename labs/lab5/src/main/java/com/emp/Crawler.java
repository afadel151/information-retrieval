package com.emp;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {

    private final String papersURL;
    private final ArrayList<arxivPaper> listofArxivPapers = new ArrayList<>();
    public Crawler(String url) {
        this.papersURL = url;
    }

    public ArrayList<arxivPaper> extractAllPapersData() {
        
        try {
            Document doc = Jsoup.connect(papersURL).get();
            Elements dtList = doc.select("dl > dt");
            Elements ddList = doc.select("dl > dd");
            for (int i = 0; i < dtList.size(); i++) {
                Element dt = dtList.get(i);
                Element dd = ddList.get(i);
                String idText = dt.select("a[href^=/abs/]").text().replace("arXiv:", "");
                int paperId = i + 1;
                String paperURL = "https://arxiv.org/abs/" + idText;
                String pdfURL = "https://arxiv.org/pdf/" + idText;
                String title = dd.select("div.list-title").text().replace("Title: ", "").trim();
                String authors = dd.select("div.list-authors").text()
                                   .replace("Authors:", "")
                                   .trim();
                String paperAbstract = dd.select("p.mathjax").text();
                arxivPaper p = new arxivPaper(
                        paperId,
                        paperURL,
                        pdfURL,
                        null,           // localPDFpath
                        title,
                        authors,
                        paperAbstract,
                        null            // paperContent
                );
                listofArxivPapers.add(p);
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        for(arxivPaper paper : listofArxivPapers){
            System.out.println(paper.getTitle());
        }
        return listofArxivPapers;
    }

}
