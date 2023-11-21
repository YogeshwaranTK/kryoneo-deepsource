package com.kjms.service.dto.requests;

import com.kjms.domain.WorkflowStage;

import java.util.List;

public class WorkflowTransferRequest {

    WorkflowStage fromWorkflowStage;

    Long reviewRoundId;

    List<Long> fileIds;

    String skipReviewMail;

    public WorkflowStage getFromWorkflowStage() {
        return fromWorkflowStage;
    }

    public void setFromWorkflowStage(WorkflowStage fromWorkflowStage) {
        this.fromWorkflowStage = fromWorkflowStage;
    }

    public Long getReviewRoundId() {
        return reviewRoundId;
    }

    public void setReviewRoundId(Long reviewRoundId) {
        this.reviewRoundId = reviewRoundId;
    }

    public List<Long> getFileIds() {
        return fileIds;
    }

    public void setFileIds(List<Long> fileIds) {
        this.fileIds = fileIds;
    }

    public String getSkipReviewMail() {
        return skipReviewMail;
    }

    public void setSkipReviewMail(String skipReviewMail) {
        this.skipReviewMail = skipReviewMail;
    }
}
