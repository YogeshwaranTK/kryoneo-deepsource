package com.kjms.service.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class SubmissionArticleFileChildRequest {

    @Schema(required = true)
    private Long id;

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


    @Schema(required = true)
    @NotNull
    private ActionType actionType;

    @Schema(required = true)
    private String deletedRemarks;

    public String getDeletedRemarks() {
        return deletedRemarks;
    }

    public void setDeletedRemarks(String deletedRemarks) {
        this.deletedRemarks = deletedRemarks;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getJournalSubmissionFileFormatId() {
        return journalSubmissionFileFormatId;
    }

    public void setJournalSubmissionFileFormatId(Long journalSubmissionFileFormatId) {
        this.journalSubmissionFileFormatId = journalSubmissionFileFormatId;
    }

    public Long getSubmissionFileTypeId() {
        return submissionFileTypeId;
    }

    public void setSubmissionFileTypeId(Long submissionFileTypeId) {
        this.submissionFileTypeId = submissionFileTypeId;
    }
}
