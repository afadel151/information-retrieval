package com.emp.ir;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResultParser {
    
    public ResultParser()
    {

    }
    public static void main(String[] args) throws IOException{
        String resultsPath = "results"; // root containing s1, s2, s3
        Map<String, Map<Integer, QueryResult>> allSystems = parseAllSystems(resultsPath);

        for (var systemEntry : allSystems.entrySet()) {
            System.out.println("System: " + systemEntry.getKey());
            for (var queryEntry : systemEntry.getValue().entrySet()) {
                System.out.println(queryEntry.getKey() + " -> " + queryEntry.getValue().docIds());
            }
        }
    }

    public static Map<String, Map<Integer, QueryResult>> parseAllSystems(String resultsPath) throws IOException {
        Map<String, Map<Integer, QueryResult>> systemsMap = new HashMap<>();
        File root = new File(resultsPath);

        if (!root.exists() || !root.isDirectory()) {
            throw new IOException("Invalid results root path: " + resultsPath);
        }

        File[] systemDirs = root.listFiles(File::isDirectory);
        if (systemDirs == null) return systemsMap;

        for (File systemDir : systemDirs) {
            String systemName = systemDir.getName();
            Map<Integer, QueryResult> queryResults = parseAllResultsInFolder(systemDir.getAbsolutePath());
            systemsMap.put(systemName, queryResults);
        }

        return systemsMap;
    }
    public static Map<Integer, QueryResult> parseAllResultsInFolder(String folderPath) throws IOException{
        Map<Integer, QueryResult> resultMap = new HashMap<>();
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            throw new IOException("invalid path: " + folderPath);
        }
        File[] files = folder.listFiles((dir, name) -> name.matches("q\\d+\\.results"));
        if (files == null || files.length == 0) {
            throw new IOException("no result files found : " + folderPath);
        }
        for (File file : files) {
            QueryResult qr = parseResultFile(file.getAbsolutePath());
            resultMap.put(qr.queryId(), qr);
        }
        return resultMap;
    }
    // QueryResult
    public static QueryResult  parseResultFile(String filePath)  throws IOException{
        ArrayList<Integer> docIds = new ArrayList<>();
        String fileName = new File(filePath).getName();
        int queryId = extractQueryId(fileName);
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (line.endsWith(".TXT")) {
                    try {
                        int docId = Integer.parseInt(line.replace(".TXT", "").replaceFirst("^0+", ""));
                        docIds.add(docId);
                    } catch (NumberFormatException ignored) {}
                }
            }
        }
        return new QueryResult(queryId, docIds);
    }
    private static int extractQueryId(String fileName) {
        try {
            String queryPart = fileName.substring(1, fileName.indexOf('.')); 
            return Integer.parseInt(queryPart);
        } catch (Exception e) {
            return -1; // return back if not found
        }
    }
}
