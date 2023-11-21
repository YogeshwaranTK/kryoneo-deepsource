package com.kjms.service.dto.requests;

public class SubmissionLanguageRequest {
    private ActionType actionType;
    private Long submissionLanguageId;

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public Long getSubmissionLanguageId() {
        return submissionLanguageId;
    }

    public void setSubmissionLanguageId(Long submissionLanguageId) {
        this.submissionLanguageId = submissionLanguageId;
    }
}
