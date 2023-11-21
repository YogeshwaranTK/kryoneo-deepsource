package com.kjms.service.dto;

import java.time.Instant;

public class ProductionDiscussionMessageFile {

    private Long id;
    private FileType fileType;
    private String file;
    private String fileEndPoint;
    private Instant createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFileEndPoint() {
        return fileEndPoint;
    }

    public void setFileEndPoint(String fileEndPoint) {
        this.fileEndPoint = fileEndPoint;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
