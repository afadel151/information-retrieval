package com.emp.indexer;

public class PaperMeta {
    String path;
    String url;
    String title;
    int id;

    public PaperMeta(int id,String path,String url,String title) {
        this.id = id;
        this.path = path;
        this.url = url;
        this.title = title;
    }

    public String getPath() {
        return path;
    }
    public String getUrl() {
        return url;
    }
    public String getTitle() {
        return title;
    }
    public int getId()
    {
        return id;
    }

}
