package com.kjms.service.dto.requests;

public class SubmissionFormatRequest {
    private ActionType actionType;
    private Long submissionFormatId;

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public Long getSubmissionFormatId() {
        return submissionFormatId;
    }

    public void setSubmissionFormatId(Long submissionFormatId) {
        this.submissionFormatId = submissionFormatId;
    }
}
