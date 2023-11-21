package com.kjms.service.dto;

public class LocalStorageSetting {
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "LocalStorageSetting{" +
            "path='" + path + '\'' +
            '}';
    }
}
