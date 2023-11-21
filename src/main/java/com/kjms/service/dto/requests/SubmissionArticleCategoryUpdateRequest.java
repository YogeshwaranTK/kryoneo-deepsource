package com.kjms.service.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;
import javax.validation.constraints.NotNull;

/**
 * Represents a request object for update Journal category.
 */
@Schema(description = "Request Body for Update Submission Article Category", title = "Submission Article Category Update")
public class SubmissionArticleCategoryUpdateRequest {

    @Schema(required = true)
    @NotNull
    private Long submissionArticleId;

    @Schema(required = true)
    @NotNull
    private Set<SubmissionCategoryRequest> categories;
    @Schema(required = true)
    @NotNull
    private Set<SubmissionKeywordRequest> keywords;

    public Set<SubmissionKeywordRequest> getKeywords() {
        return keywords;
    }

    public void setKeywords(Set<SubmissionKeywordRequest> keywords) {
        this.keywords = keywords;
    }

    public Set<SubmissionCategoryRequest> getCategories() {
        return categories;
    }

    public void setCategories(Set<SubmissionCategoryRequest> categories) {
        this.categories = categories;
    }

    public Long getSubmissionArticleId() {
        return submissionArticleId;
    }

    public void setSubmissionArticleId(Long submissionArticleId) {
        this.submissionArticleId = submissionArticleId;
    }
}
