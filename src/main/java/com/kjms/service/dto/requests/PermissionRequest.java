package com.kjms.service.dto.requests;

public class PermissionRequest {
    private ActionType actionType;
    private String orgPermissionId;

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public String getOrgPermissionId() {
        return orgPermissionId;
    }

    public void setOrgPermissionId(String orgPermissionId) {
        this.orgPermissionId = orgPermissionId;
    }
}
