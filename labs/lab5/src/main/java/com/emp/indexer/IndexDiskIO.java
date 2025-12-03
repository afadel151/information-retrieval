package com.emp.indexer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.emp.arxivPaper;

public class IndexDiskIO {

  
    public void save_index_to_disk(Map<String, Map<String, Integer>> lexicons,
                                   Map<String, Map<Integer, List<int[]>>> postings,
                                   ArrayList<arxivPaper> papers,
                                   String baseOutputPath) throws IOException {

        Files.createDirectories(Paths.get(baseOutputPath));
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(baseOutputPath, "papers.txt"))) {
            for (arxivPaper p : papers) {
               if (p.getContent() != null) 
                {
                    writer.write(p.getPaperId() + "\t" + p.getLocalPath() + "\t" + p.getPaperUrl() + "\t" + p.getPaperUrl());
                    writer.newLine();
               }
            }
        }


        for (String field : lexicons.keySet()) {
            Path fieldDir = Paths.get(baseOutputPath, field);
            Files.createDirectories(fieldDir);

            // Lexicon
            try (BufferedWriter writer = Files.newBufferedWriter(fieldDir.resolve("lexicon.txt"))) {
                Map<String, Integer> lexicon = lexicons.get(field);
                for (Map.Entry<String, Integer> e : lexicon.entrySet()) {
                    writer.write(e.getKey() + "\t" + e.getValue());
                    writer.newLine();
                }
            }

            // Postings
            try (BufferedWriter writer = Files.newBufferedWriter(fieldDir.resolve("postings.txt"))) {
                Map<Integer, List<int[]>> postingMap = postings.get(field);
                for (Map.Entry<Integer, List<int[]>> e : postingMap.entrySet()) {
                    int termId = e.getKey();
                    List<int[]> plist = e.getValue();
                    StringBuilder line = new StringBuilder();
                    line.append(termId).append("\t");
                    for (int i = 0; i < plist.size(); i++) {
                        int[] pair = plist.get(i);
                        line.append(pair[0]).append(":").append(pair[1]);
                        if (i < plist.size() - 1) line.append(",");
                    }
                    writer.write(line.toString());
                    writer.newLine();
                }
            }

            System.out.println("Saved field '" + field + "' to: " + fieldDir);
        }
    }

    /**
     * Read index for a specific field from disk
     */
    public IndexData read_index_from_disk(String baseIndexPath, String field) throws IOException {
        IndexData data = new IndexData();
        data.lexicon = new HashMap<>();
        data.postings = new HashMap<>();
        data.papers = new HashMap<>();

        Path fieldDir = Paths.get(baseIndexPath, field);

        // Lexicon
        try (BufferedReader reader = Files.newBufferedReader(fieldDir.resolve("lexicon.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length == 2) {
                    data.lexicon.put(parts[0], Integer.parseInt(parts[1]));
                }
            }
        }

        // Postings
        try (BufferedReader reader = Files.newBufferedReader(fieldDir.resolve("postings.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length == 2) {
                    int termId = Integer.parseInt(parts[0]);
                    List<int[]> plist = new ArrayList<>();
                    String[] pairs = parts[1].split(",");
                    for (String p : pairs) {
                        String[] values = p.split(":");
                        if (values.length == 2) {
                            int docId = Integer.parseInt(values[0]);
                            int tf = Integer.parseInt(values[1]);
                            plist.add(new int[]{docId, tf});
                        }
                    }
                    data.postings.put(termId, plist);
                }
            }
        }

        // Papers metadata
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(baseIndexPath, "papers.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length == 4) {
                    int id = Integer.parseInt(parts[0]);
                    String path = parts[1];
                    String url = parts[2];
                    String title = parts[3];
                    data.papers.put(id, new PaperMeta(id, path, url, title));
                }
            }
        }

        System.out.println("Index loaded for field '" + field + "' from: " + fieldDir);
        System.out.println("Terms: " + data.lexicon.size() +
                           ", Papers: " + data.papers.size() +
                           ", Postings: " + data.postings.size());

        return data;
    }
}
