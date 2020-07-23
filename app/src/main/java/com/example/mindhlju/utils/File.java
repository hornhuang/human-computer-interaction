package com.example.mindhlju.utils;

public class File {

    private String path;

    private String fileName;

    private String changeDate;

    public File() {

    }

    public File(String path, String fileName, String changeDate) {
        this.path = path;
        this.fileName = fileName;
        this.changeDate = changeDate;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(String changeDate) {
        this.changeDate = changeDate;
    }
}
