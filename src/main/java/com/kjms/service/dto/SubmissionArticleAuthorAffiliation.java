package com.kjms.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class SubmissionArticleAuthorAffiliation {
    private Long id;
    private String affiliation;

    @JsonIgnore
    private Long submissionAuthorId;

    public Long getSubmissionAuthorId() {
        return submissionAuthorId;
    }

    public String getActionType() {
        return "DEFAULT";
    }

    public void setSubmissionAuthorId(Long submissionAuthorId) {
        this.submissionAuthorId = submissionAuthorId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }
}
