package com.kjms.service.dto.requests;

import javax.validation.constraints.NotNull;
import java.util.List;

public class EditorialUserCreateRequest {

    @NotNull
    private List<String> userIds;

    @NotNull
    private Boolean isExistingRole;

    private Long roleId;

    private String roleName;

    private Boolean isSubmissionEnabled;

    private Boolean isReviewEnabled;

    private Boolean isCopyEditingEnabled;

    private Boolean isProductionEnabled;

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public Boolean getExistingRole() {
        return isExistingRole;
    }

    public void setExistingRole(Boolean existingRole) {
        isExistingRole = existingRole;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Boolean getSubmissionEnabled() {
        return isSubmissionEnabled;
    }

    public void setSubmissionEnabled(Boolean submissionEnabled) {
        isSubmissionEnabled = submissionEnabled;
    }

    public Boolean getReviewEnabled() {
        return isReviewEnabled;
    }

    public void setReviewEnabled(Boolean reviewEnabled) {
        isReviewEnabled = reviewEnabled;
    }

    public Boolean getCopyEditingEnabled() {
        return isCopyEditingEnabled;
    }

    public void setCopyEditingEnabled(Boolean copyEditingEnabled) {
        isCopyEditingEnabled = copyEditingEnabled;
    }

    public Boolean getProductionEnabled() {
        return isProductionEnabled;
    }

    public void setProductionEnabled(Boolean productionEnabled) {
        isProductionEnabled = productionEnabled;
    }
}
