package com.kjms.service.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Represents a request object for create Submission article.
 */
@Schema(description = "Request Body for Create Submission Article", title = "Create Submission Article")
public class SubmissionCreateRequest {

    @Size(max = 50)
    private String prefix;

    @Schema(required = true)
    @NotNull
    @NotBlank
    @Size(min = 2)
    private String title;

    private String subTitle;
    private String description;
    private Long journalLanguageId;

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getJournalLanguageId() {
        return journalLanguageId;
    }

    public void setJournalLanguageId(Long journalLanguageId) {
        this.journalLanguageId = journalLanguageId;
    }
}
