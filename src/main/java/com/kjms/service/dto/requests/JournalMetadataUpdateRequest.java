package com.kjms.service.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import javax.validation.constraints.NotNull;

/**
 * Represents a request object for update journal guidelines.
 */
@Schema(description = "Request Body for Update Journal Guidelines", title = "Update Journal Guideline")
public class JournalMetadataUpdateRequest {

    @Schema(required = true)
    @NotNull
    private List<String> submissionLanguages;

    @Schema(required = true)
    @NotNull
    private List<String> categories;

    @Schema(required = true)
    @NotNull
    private List<Long> fileTypes;

    public List<String> getSubmissionLanguages() {
        return submissionLanguages;
    }

    public void setSubmissionLanguages(List<String> submissionLanguages) {
        this.submissionLanguages = submissionLanguages;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<Long> getFileTypes() {
        return fileTypes;
    }

    public void setFileTypes(List<Long> fileTypes) {
        this.fileTypes = fileTypes;
    }
}
