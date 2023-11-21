package com.kjms.service.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;


public class SubmissionArticleRevisionStatusUpdateRequest {

    @Schema(required = true)
    @NotNull
    private SubmissionArticleRevisionStatusUpdateType statusUpdateType;
    @Schema(required = true)
    @NotNull
    private Long submissionArticleId;
    private String declineRemarks;

    public SubmissionArticleRevisionStatusUpdateType getStatusUpdateType() {
        return statusUpdateType;
    }

    public void setStatusUpdateType(SubmissionArticleRevisionStatusUpdateType statusUpdateType) {
        this.statusUpdateType = statusUpdateType;
    }

    public Long getSubmissionArticleId() {
        return submissionArticleId;
    }

    public void setSubmissionArticleId(Long submissionArticleId) {
        this.submissionArticleId = submissionArticleId;
    }

    public String getDeclineRemarks() {
        return declineRemarks;
    }

    public void setDeclineRemarks(String declineRemarks) {
        this.declineRemarks = declineRemarks;
    }
}
