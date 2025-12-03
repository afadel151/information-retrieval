package com.emp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

public class arxivPaper {

    private final int paperId;
    private final String paperURL;
    private final String onlinePDFLink;
    private String localPDFpath;
    private final String title;
    private final String authors;
    private final String paperAbstract;
    private String paperContent;

    public arxivPaper(int paperId, String paperURL, String onlinePDFLink, String localPDFpath,
            String title, String authors, String paperAbstract, String paperContent) {
        this.paperId = paperId;
        this.paperURL = paperURL;
        this.onlinePDFLink = onlinePDFLink;
        this.localPDFpath = localPDFpath;
        this.title = title;
        this.authors = authors;
        this.paperAbstract = paperAbstract;
        this.paperContent = paperContent;
    }

    public void downloadPDF() {
        try {
            if (onlinePDFLink == null || onlinePDFLink.isEmpty()) {
                throw new RuntimeException("PDF URL is empty");
            }
            if (localPDFpath == null || localPDFpath.isEmpty()) {
                localPDFpath = "papers/" + paperId + ".pdf";
            }
            File folder = new File("papers");
            if (!folder.exists()) {
                folder.mkdirs();
            }

            URL url = new URL(onlinePDFLink);
            try (InputStream in = url.openStream(); FileOutputStream out = new FileOutputStream(localPDFpath)) {
                byte[] buffer = new byte[4096];
                int n;
                while ((n = in.read(buffer)) != -1) {
                    out.write(buffer, 0, n);
                }
            }
            System.out.println("PDF downloaded: " + localPDFpath);
        } catch (RuntimeException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void extractContentfromPDF() {
        try {
            if (localPDFpath == null || localPDFpath.isEmpty()) {
                throw new RuntimeException("Le chemin local du PDF est vide.");
            }

            File file = new File(localPDFpath);
            if (!file.exists()) {
                throw new RuntimeException("Le fichier PDF n'existe pas : " + localPDFpath);
            }

            Tika tika = new Tika();
            String extractedText = tika.parseToString(file);

            this.paperContent = extractedText;
            System.out.println("Contenu extrait pour le PDF : " + localPDFpath);

        } catch (RuntimeException | IOException | TikaException e) {
            System.out.println(e.getMessage());
        }
    }

    public String getTitle() {
        return this.title;
    }
    public int getPaperId()
    {
        return this.paperId;
    }
    public String getContent()
    {
        return this.paperContent;
    }

    public String getAbstract()
    {
        return this.paperAbstract;
    }
    public String getAuthors()
    {
        return this.authors;
    }
    public String getPaperUrl()
    {
        return this.paperURL;
    }
    public String getLocalPath()
    {
        return this.localPDFpath;
    }
}
