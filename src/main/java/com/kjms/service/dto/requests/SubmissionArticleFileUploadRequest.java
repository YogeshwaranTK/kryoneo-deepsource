package com.kjms.service.dto.requests;


import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Represents a request object for create Upload Submission Article File.
 */
@Schema(description = "Request Body for Upload Submission Article File", title = "Create Upload Submission Article File")
public class SubmissionArticleFileUploadRequest {
    List<SubmissionArticleFileChildRequest> submissionArticleFiles;
    private Long submissionArticleId;

    public Long getSubmissionArticleId() {
        return submissionArticleId;
    }

    public void setSubmissionArticleId(Long submissionArticleId) {
        this.submissionArticleId = submissionArticleId;
    }

    public List<SubmissionArticleFileChildRequest> getSubmissionArticleFiles() {
        return submissionArticleFiles;
    }

    public void setSubmissionArticleFiles(List<SubmissionArticleFileChildRequest> submissionArticleFiles) {
        this.submissionArticleFiles = submissionArticleFiles;
    }
}
