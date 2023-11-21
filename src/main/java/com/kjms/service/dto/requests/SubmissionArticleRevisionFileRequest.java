package com.kjms.service.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class SubmissionArticleRevisionFileRequest {

    @Schema(required = true)
    @NotNull
    private Long journalSubmissionFileFormatId;

    @Schema(required = true)
    @NotNull
    @NotBlank
    private String filePath;

    @Schema(required = true)
    @NotNull
    @NotBlank
    private String fileName;


    @Schema(required = true)
    @NotNull
    private Long submissionFileTypeId;

    public Long getSubmissionFileTypeId() {
        return submissionFileTypeId;
    }

    public void setSubmissionFileTypeId(Long submissionFileTypeId) {
        this.submissionFileTypeId = submissionFileTypeId;
    }

    public Long getJournalSubmissionFileFormatId() {
        return journalSubmissionFileFormatId;
    }

    public void setJournalSubmissionFileFormatId(Long journalSubmissionFileFormatId) {
        this.journalSubmissionFileFormatId = journalSubmissionFileFormatId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
