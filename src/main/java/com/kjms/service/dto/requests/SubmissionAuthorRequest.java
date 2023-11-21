package com.kjms.service.dto.requests;

import com.kjms.domain.SubmissionArticleContributorRole;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class SubmissionAuthorRequest {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String prefix;

    @Schema(required = true)
    @NotNull
    @NotBlank
    private String firstName;

    @Size(max = 255)
    private String surName;
    private String middleName;

    @NotNull
    @Email
    private String email;
    private List<AuthorAffiliationRequest> affiliations;
    private String orcidId;

    @Schema(required = true)
    @NotNull
    @NotBlank
    private SubmissionArticleContributorRole role;

    @Schema(required = true)
    @NotNull
    private Boolean isPrimary;
    @Schema(required = true)
    @NotNull
    @NotBlank
    private String deletedRemarks;

    @Schema(required = true)
    @NotNull
    private ActionType actionType;

    private Boolean browserList;

    public Boolean getBrowserList() {
        return browserList;
    }

    public void setBrowserList(Boolean browserList) {
        this.browserList = browserList;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    public String getDeletedRemarks() {
        return deletedRemarks;
    }

    public void setDeletedRemarks(String deletedRemarks) {
        this.deletedRemarks = deletedRemarks;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<AuthorAffiliationRequest> getAffiliations() {
        return affiliations;
    }

    public void setAffiliations(List<AuthorAffiliationRequest> affiliations) {
        this.affiliations = affiliations;
    }

    public String getOrcidId() {
        return orcidId;
    }

    public void setOrcidId(String orcidId) {
        this.orcidId = orcidId;
    }

    public SubmissionArticleContributorRole getRole() {
        return role;
    }

    public void setRole(SubmissionArticleContributorRole role) {
        this.role = role;
    }

    public Boolean getPrimary() {
        return isPrimary;
    }

    public void setPrimary(Boolean primary) {
        isPrimary = primary;
    }

}
