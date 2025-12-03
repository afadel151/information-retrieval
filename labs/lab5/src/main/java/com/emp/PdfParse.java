package com.emp;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;

public class PdfParse {

    public static void main(String[] args) {
        // Specify the path to the PDF file
        String filePath = "C:\\rapport.pdf";

        // Tika parser with advanced handling
        try (InputStream input = new FileInputStream(new File(filePath))) {
            // Use a handler for storing content
            BodyContentHandler handler = new BodyContentHandler(-1); // -1 for unlimited text length
            Metadata metadata = new Metadata();

            // Auto-detect parser
            AutoDetectParser parser = new AutoDetectParser();
            ParseContext context = new ParseContext();

            // Parse the file and extract content
            parser.parse(input, handler, metadata, context);

            // Print the extracted content
            System.out.println("Extracted Text:");
            System.out.println(handler.toString());

        } catch (Exception e) {
            System.err.println("Error occurred while extracting text from PDF: " + e.getMessage());
            // e.printStackTrace();
        }
    }
}

