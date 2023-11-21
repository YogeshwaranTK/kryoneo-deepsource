package com.kjms.service.dto.requests;

public class OrgRolePermissionUpdateRequest {
    private String id;
    private ActionType actionType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }
}
