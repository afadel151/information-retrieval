package com.emp.ir;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class AssessmentParser {

    public static void main(String[] args) {
        try {
            String path = "assessments.txt";
            Map<Integer, QueryAssessment> map = parseAssessments(path);
            for (var entry : map.entrySet()) {
                System.out.println(entry.getKey() + " -> " + entry.getValue().relevantDocIds());
            }
        } catch (IOException e) {
            System.err.println(e.toString());
        }
    }

    public AssessmentParser() {

    }

    public static Map<Integer, QueryAssessment> parseAssessments(String filePath) throws IOException {
        Map<Integer, QueryAssessment> queryMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                if (line.startsWith("q")) {
                    String[] parts = line.split("\\s+");
                    // System.out.println(Arrays.toString(parts));
                    String queryLabel = parts[0]; // e.g., "q1"
                    int queryId = Integer.parseInt(queryLabel.substring(1)); 
                    int relevantCount = Integer.parseInt(parts[1]);
                    // System.out.println(queryId + relevantCount);
                    HashSet<Integer> docIds = new HashSet<>();
                    while ((line = reader.readLine()) != null) {
                        line = line.trim();
                        if (line.isEmpty()) {
                            continue;
                        }
                        if (line.startsWith("q")) {
                            // move back one line so we proceed for a new query 
                            reader.reset();
                            break;
                        }
                        if (line.endsWith(".TXT")) {
                            try {
                                int docId = Integer.parseInt(line.replace(".TXT", ""));
                                docIds.add(docId);
                            } catch (NumberFormatException ignored) {
                            } // ignore the doc
                        }
                        reader.mark(1000);
                    }
                    QueryAssessment qa = new QueryAssessment(
                            queryId, // remove 'q'
                            relevantCount,
                            docIds
                    );
                    queryMap.put(queryId, qa);
                }
                reader.mark(1000);
            }
        }
        return queryMap;
    }

    public void store() {

    }

}
