package  com.emp.indexer;

// iterate over corpus folder

import java.util.*;
import java.util.stream.Stream;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
// map each file with docID, calculate doc length and store docURL
// output : Map<Integer, DocumentMeta (String path, int length)>, 
public class DocumentReader {

    public Map<Integer, DocumentMeta> loadDocuments(String corpusPath){
        Map<Integer, DocumentMeta> documents = new HashMap<>();
        try (Stream<Path> paths = Files.walk(Paths.get(corpusPath))){
            int docId = 0;
            for (Path file : (Iterable<Path>) paths.filter(Files::isRegularFile)::iterator) {
                //  only text files :
                String content = readDocument(file.toString());

                //  estimate length before tokenization
                int length = content.split("\\s+").length;

                documents.put(docId, new DocumentMeta(file.toString(), length));
                docId++;
            }
        }catch (IOException e) {
            System.err.println(e);;
        }
         System.out.println("Loaded " + documents.size() + " documents from " + corpusPath);
        return documents;
    }

    public String readDocument(String filePath) throws IOException{
        return Files.readString(Paths.get(filePath), StandardCharsets.UTF_8);
    }
}
