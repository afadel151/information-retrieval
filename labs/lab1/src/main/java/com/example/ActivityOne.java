package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ActivityOne {

    public void ReadTextDocument(String filePath) {
        try (
                InputStream inputStream = ActivityOne.class.getClassLoader().getResourceAsStream(filePath); BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            if (inputStream == null) {
                System.err.println("Resource not found: " + filePath);
                return;
            }
            try {
                String line = reader.readLine();
                while (line != null) {
                    System.out.println(line);
                    line = reader.readLine();
                }
            } finally {
                reader.close();
            }

        } catch (IOException e) {
            System.out.println("Error reading resource file: " + e.getMessage());
        }
    }
}
