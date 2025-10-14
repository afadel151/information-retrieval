This is a simple yet powerful **information retrieval system** built from scratch in **Java 21**.  
It demonstrates how modern search engines index, weight, and rank documents using **TF-IDF** and **BM25** — the same algorithms behind Lucene and Elasticsearch.

---

## Overview

This program reads a corpus of text documents, builds an **inverted index**, and allows you to run **free-text queries** from the command line.  
It’s designed as a **learning project** for students and developers who want to understand how search engines work internally..

---
## Features

- Full text preprocessing pipeline:
  - Tokenization
  - Stop-word removal
  - Stemming (Porter algorithm)
- Builds a persistent inverted index (saved to disk)
- Supports **TF-IDF** and **BM25** ranking models
- Query interface to search and rank documents
- Built entirely in **pure Java** (no external search libraries)

---

## Project Structure

```
lab/
├── corpus/                 # Folder containing all text documents (your dataset)
│   ├── doc1.txt
│   ├── doc2.txt
│   └── ...
│
├── src/                    # Java source code
│   └── main/
│       └── java/
│           └── com/
│               └── example/
│                   ├── Config.java
│                   ├── DocumentReader.java
│                   ├── Tokenizer.java
│                   ├── StopWordsFilter.java
│                   ├── Stemmer.java
│                   ├── Indexer.java
│                   ├── IndexDiskIO.java
│                   ├── QueryProcessor.java
│                   ├── RetrievalEngine.java
│                   └── Main.java
│                   └── snowball/
│                       ├── Among.java
│                       ├── SnowballProgram.java
│                       ├── SnowballStemmer.java
│                       ├── TestApp.java
│                       └── ext/
│                           └──  porterStemmer.java
│
├── stopwords.txt           # List of common stop words (one per line)
├── lexicon.txt             # Saved lexicon (term -> termID)
├── postings.txt            # Inverted index (termID -> docID:tf)
├── documents.txt           # Document metadata (docID -> path, length)
├── pom.xml                 # Maven build configuration
└── README.md               # Project documentation
```

## Installation

### 1. Clone the repository
```bash
git clone https://github.com/afadel151/document-indexer.git
cd document-indexer
```

### 2. Buid the project with maven
```bash
mvn clean compile
```

### 3. Prepare your corpus
Extract the ```corpus.zip``` file and place it in the root folder

---

### Usage
Run the main program:
```bash
java -cp target/classes com.example.Main
```
The system will:

- Check if an index exists.

- If not, build one from the corpus.

- Enter interactive query mode.

Example session:
```pgsql
=== Information Retrieval System ===
Type a query, or 'exit' to quit.

Query > machine learning
DocID 12 | Score: 7.1423 | Path: corpus/paper1.txt
DocID 87 | Score: 6.9984 | Path: corpus/paper2.txt
```
---
### Algorithms Implemented
| Algorithm | Description |
|--|--|
| TF-IDF | Weights terms by their importance across the corpus using tf × log(N/df) |
| BM25| A probabilistic ranking function that normalizes for document length|

Both are fully implemented from scratch in pure Java for transparency and learning.

---
### Learning Objectives
This project was built as part of an Information Retrieval Lab project to help understand:

- How inverted indexes are built

- How document frequencies (DF) and term frequencies (TF) work

- How ranking algorithms like BM25 balance TF, DF, and document length

- How to structure a scalable search pipeline

### Author
**[Akram Fadel](https://akramfadel.netlify.app)** 



