package com.kjms.service.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;


/**
 * Represents a request object for creating a new Journal
 */
@Schema(description = "Request Body for Update Journal Summary", title = "Journal Summary")
public class JournalSummaryRequest {

    @Schema(description = "File path of file which is return from thumbnail upload Api", required = true)
    private String thumbnail;

    @Schema(required = true)
    private String summary;

    private Long journalId;

    public Long getJournalId() {
        return journalId;
    }

    public void setJournalId(Long journalId) {
        this.journalId = journalId;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
