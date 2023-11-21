package com.kjms.service.dto;

public class EditorialUserRole {

    private final String userId;

    private final Long roleId;

    private final String roleName;

    private final Boolean isSubmissionEnabled;

    private final Boolean isReviewEnabled;

    private final Boolean isCopyEditingEnabled;

    private final Boolean isProductionEnabled;


    public EditorialUserRole(String userId, Long roleId, String roleName, Boolean isSubmissionEnabled,
                             Boolean isReviewEnabled, Boolean isCopyEditingEnabled, Boolean isProductionEnabled) {
        this.userId = userId;
        this.roleId = roleId;
        this.roleName = roleName;
        this.isSubmissionEnabled = isSubmissionEnabled;
        this.isReviewEnabled = isReviewEnabled;
        this.isCopyEditingEnabled = isCopyEditingEnabled;
        this.isProductionEnabled = isProductionEnabled;
    }

    public String getUserId() {
        return userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public Boolean getSubmissionEnabled() {
        return isSubmissionEnabled;
    }

    public Boolean getReviewEnabled() {
        return isReviewEnabled;
    }

    public Boolean getCopyEditingEnabled() {
        return isCopyEditingEnabled;
    }

    public Boolean getProductionEnabled() {
        return isProductionEnabled;
    }
}
