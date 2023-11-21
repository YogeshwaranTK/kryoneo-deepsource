package com.kjms.service.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Represents a request object for Update Submission article author.
 */
@Schema(description = "Request Body for Update Submission Article Author", title = "Update Submission Article Author")
public class SubmissionAuthorUpdateRequest {

    @Schema(required = true)
    @NotNull
    @NotEmpty
    List<SubmissionAuthorRequest> submissionAuthors;
    @Schema(required = true)
    @NotNull
    private Long submissionId;

    public Long getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(Long submissionId) {
        this.submissionId = submissionId;
    }

    public List<SubmissionAuthorRequest> getSubmissionAuthors() {
        return submissionAuthors;
    }

    public void setSubmissionAuthors(List<SubmissionAuthorRequest> submissionAuthors) {
        this.submissionAuthors = submissionAuthors;
    }
}
