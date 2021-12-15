package com.lingmiao.distribution.bean;

public class Filepath {
    private String filePath;
    private String id;

    public Filepath(String filePath, String id) {
        super();
        this.filePath = filePath;
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
