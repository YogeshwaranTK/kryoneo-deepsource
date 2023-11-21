package com.kjms.service.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

public class SubmissionArticleRevisionRequest {
    @Schema(required = true)
    @NotNull
    List<SubmissionArticleRevisionFileRequest> files;

    @Schema(required = true)
    @NotNull
    private Long submissionArticleId;
    @Schema(required = true)
    @NotNull
    private String description;
    private String submissionTitle;
    private String submissionSubTitle;
    private String submissionDescription;
    @Size(max = 50)
    private String submissionPrefix;
    private Set<SubmissionCategoryRequest> categories;

    private Set<SubmissionKeywordRequest> keywords;

    public String getSubmissionTitle() {
        return submissionTitle;
    }

    public void setSubmissionTitle(String submissionTitle) {
        this.submissionTitle = submissionTitle;
    }

    public String getSubmissionSubTitle() {
        return submissionSubTitle;
    }

    public void setSubmissionSubTitle(String submissionSubTitle) {
        this.submissionSubTitle = submissionSubTitle;
    }

    public String getSubmissionDescription() {
        return submissionDescription;
    }

    public void setSubmissionDescription(String submissionDescription) {
        this.submissionDescription = submissionDescription;
    }

    public String getSubmissionPrefix() {
        return submissionPrefix;
    }

    public void setSubmissionPrefix(String submissionPrefix) {
        this.submissionPrefix = submissionPrefix;
    }

    public Set<SubmissionCategoryRequest> getCategories() {
        return categories;
    }

    public void setCategories(Set<SubmissionCategoryRequest> categories) {
        this.categories = categories;
    }

    public Set<SubmissionKeywordRequest> getKeywords() {
        return keywords;
    }

    public void setKeywords(Set<SubmissionKeywordRequest> keywords) {
        this.keywords = keywords;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<SubmissionArticleRevisionFileRequest> getFiles() {
        return files;
    }

    public void setFiles(List<SubmissionArticleRevisionFileRequest> files) {
        this.files = files;
    }

    public Long getSubmissionArticleId() {
        return submissionArticleId;
    }

    public void setSubmissionArticleId(Long submissionArticleId) {
        this.submissionArticleId = submissionArticleId;
    }
}
