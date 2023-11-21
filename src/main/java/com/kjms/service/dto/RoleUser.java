package com.kjms.service.dto;

public class RoleUser {

    private RootRoleType rootRoleType;
    private String userId;
    private String fullName;

    public RootRoleType getRootRoleType() {
        return rootRoleType;
    }

    public void setRootRoleType(RootRoleType rootRoleType) {
        this.rootRoleType = rootRoleType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
