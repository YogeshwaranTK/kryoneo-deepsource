package com.kjms.service.dto.requests;

public class JournalGroupPermissionRequest {
    private ActionType actionType;
    private String journalPermissionId;

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public String getJournalPermissionId() {
        return journalPermissionId;
    }

    public void setJournalPermissionId(String journalPermissionId) {
        this.journalPermissionId = journalPermissionId;
    }
}
