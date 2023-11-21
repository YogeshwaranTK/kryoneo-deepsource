package com.kjms.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SubmissionArticleAuthor implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String prefix;
    private String firstName;
    private String surName;
    private String middleName;
    private String email;
    private String affiliation;
    private String orcidId;
    private String role;
    private Boolean isPrimary;
    private Boolean browserList;
    private Boolean ownAuthorDetail;

    private List<SubmissionArticleAuthorAffiliation> affiliations = new ArrayList<>();

    public Boolean getBrowserList() {
        return browserList;
    }

    public void setBrowserList(Boolean browserList) {
        this.browserList = browserList;
    }

    public Boolean getOwnAuthorDetail() {
        return ownAuthorDetail;
    }

    public void setOwnAuthorDetail(Boolean ownAuthorDetail) {
        this.ownAuthorDetail = ownAuthorDetail;
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

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public String getOrcidId() {
        return orcidId;
    }

    public void setOrcidId(String orcidId) {
        this.orcidId = orcidId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getPrimary() {
        return isPrimary;
    }

    public void setPrimary(Boolean primary) {
        isPrimary = primary;
    }

    public List<SubmissionArticleAuthorAffiliation> getAffiliations() {
        return affiliations;
    }

    public void setAffiliations(List<SubmissionArticleAuthorAffiliation> affiliations) {
        this.affiliations = affiliations;
    }
}
