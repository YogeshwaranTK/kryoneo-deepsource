package com.kjms.service.dto;

import java.time.Instant;

public class CopyEditingDraftFile {

    private Long id;

    private Long submissionId;

    private String fileEndPoint;

    private FileType fileType;

    private String file;

    private Instant createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(Long submissionId) {
        this.submissionId = submissionId;
    }

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

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
