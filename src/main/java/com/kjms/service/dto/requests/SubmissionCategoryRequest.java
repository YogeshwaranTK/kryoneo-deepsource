package com.kjms.service.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class SubmissionCategoryRequest {
    @Schema(required = true)
    @NotNull
    @NotBlank
    private Long journalCategoryId;

    @Schema(required = true)
    @NotNull
    private ActionType actionType;

    public Long getJournalCategoryId() {
        return journalCategoryId;
    }

    public void setJournalCategoryId(Long journalCategoryId) {
        this.journalCategoryId = journalCategoryId;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }
}
