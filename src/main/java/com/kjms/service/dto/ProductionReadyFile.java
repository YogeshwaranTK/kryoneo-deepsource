package com.kjms.service.dto;

import java.time.Instant;


public class ProductionReadyFile {

    private Long id;
    private String file;
    private String fileEndPoint;
    private String submissionId;
    private String createdBy;
    private FileType fileType;
    private Instant createdAt;

    public String getFileEndPoint() {
        return fileEndPoint;
    }

    public void setFileEndPoint(String fileEndPoint) {
        this.fileEndPoint = fileEndPoint;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(String submissionId) {
        this.submissionId = submissionId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
