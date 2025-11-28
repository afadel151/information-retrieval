package com.emp.web_indexer;

import com.emp.indexer.DocumentMeta;
import com.emp.web_indexer.models.PositionInfo;
import com.emp.web_indexer.models.Posting;
import com.emp.web_indexer.models.WebIndex;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class WebIndexDiskService {

    private static final String LEXICON_FILE = "index_data/lexicon.txt";
    private static final String DOCS_FILE = "index_data/docs.txt";
    private static final String POSTINGS_FILE = "index_data/postings.txt";

    // -------------------------------------------------------------------------
    // SAVE TO DISK
    // -------------------------------------------------------------------------
    public void save_index_to_disk(Map<String, Integer> lexicon,
                                   Map<Integer, List<Posting>> postings,
                                   Map<Integer, DocumentMeta> documents,
                                   String outputPath) throws IOException {

        Files.createDirectories(Paths.get(outputPath, "index_data"));

        // Save lexicon
        try (BufferedWriter writer =
                     Files.newBufferedWriter(Paths.get(outputPath, LEXICON_FILE))) {
            for (var e : lexicon.entrySet()) {
                writer.write(e.getKey() + "\t" + e.getValue());
                writer.newLine();
            }
        }

        // Save documents
        try (BufferedWriter writer =
                     Files.newBufferedWriter(Paths.get(outputPath, DOCS_FILE))) {
            for (var e : documents.entrySet()) {
                writer.write(e.getKey() + "\t" +
                        e.getValue().getPath() + "\t" +
                        e.getValue().getLength());
                writer.newLine();
            }
        }

        // Save postings (HTML-aware)
        try (BufferedWriter writer =
                     Files.newBufferedWriter(Paths.get(outputPath, POSTINGS_FILE))) {

            for (var entry : postings.entrySet()) {
                int termId = entry.getKey();
                List<Posting> plist = entry.getValue();

                StringBuilder sb = new StringBuilder();
                sb.append(termId).append("\t");

                for (int i = 0; i < plist.size(); i++) {
                    Posting posting = plist.get(i);

                    sb.append(posting.getDocId())
                            .append("|")
                            .append(posting.getTermFrequency())
                            .append("|");

                    List<PositionInfo> positions = posting.getPositions();
                    for (int j = 0; j < positions.size(); j++) {
                        PositionInfo pos = positions.get(j);
                        sb.append(pos.getPosition())
                                .append(":")
                                .append(pos.getTag())
                                .append(":")
                                .append(pos.getTagWeight());

                        if (j < positions.size() - 1) sb.append(",");
                    }

                    if (i < plist.size() - 1) sb.append(";");
                }

                writer.write(sb.toString());
                writer.newLine();
            }
        }

        System.out.println("Index saved to: " + outputPath);
    }

    // -------------------------------------------------------------------------
    // LOAD FROM DISK
    // -------------------------------------------------------------------------
    public WebIndex read_index_from_disk(String indexPath) throws IOException {

        WebIndex index = new WebIndex();
        index.lexicon = new HashMap<>();
        index.postings = new HashMap<>();
        index.documents = new HashMap<>();

        // Load lexicon
        try (BufferedReader reader =
                     Files.newBufferedReader(Paths.get(indexPath, LEXICON_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] p = line.split("\t");
                index.lexicon.put(p[0], Integer.parseInt(p[1]));
            }
        }

        // Load documents
        try (BufferedReader reader =
                     Files.newBufferedReader(Paths.get(indexPath, DOCS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] p = line.split("\t");
                int docId = Integer.parseInt(p[0]);
                index.documents.put(docId, new DocumentMeta(p[1], Integer.parseInt(p[2])));
            }
        }

        // Load postings
        try (BufferedReader reader =
                     Files.newBufferedReader(Paths.get(indexPath, POSTINGS_FILE))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] p = line.split("\t");
                int termId = Integer.parseInt(p[0]);

                List<Posting> plist = new ArrayList<>();

                String[] docs = p[1].split(";");
                for (String docEntry : docs) {

                    String[] d = docEntry.split("\\|");
                    int docId = Integer.parseInt(d[0]);
                    int tf = Integer.parseInt(d[1]);

                    List<PositionInfo> positions = new ArrayList<>();
                    if (d.length > 2) {
                        String[] posArr = d[2].split(",");
                        for (String posStr : posArr) {
                            String[] details = posStr.split(":");
                            int pos = Integer.parseInt(details[0]);
                            String tag = details[1];
                            double weight = Double.parseDouble(details[2]);
                            positions.add(new PositionInfo(pos, tag, weight));
                        }
                    }

                    plist.add(new Posting(docId, tf, positions));
                }

                index.postings.put(termId, plist);
            }
        }

        System.out.println("Index loaded from: " + indexPath);
        return index;
    }
}
