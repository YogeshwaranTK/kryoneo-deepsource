package com.kjms.service.dto;

import java.time.Instant;

public class JournalsReport {

    private final Long journalId;

    private final String journalTitle;

    private final Boolean published;

    private final Instant publishedAt;

    private final Instant createdAt;

    private final Long submittedCount;

    private final Long InSubmissionCount;

    private final Long InReviewCount;

    private final Long InCopyEditingCount;

    private final Long InProductionCount;

    public JournalsReport(Long journalId, Long submittedCount, Long inSubmissionCount, Long inReviewCount, Long inCopyEditingCount, Long InProductionCount, Boolean published, Instant publishedAt, Instant createdAt, String journalTitle) {
        this.journalId = journalId;
        this.journalTitle = journalTitle;
        this.submittedCount = submittedCount;
        this.InSubmissionCount = inSubmissionCount;
        this.InReviewCount = inReviewCount;
        this.InCopyEditingCount = inCopyEditingCount;
        this.InProductionCount = InProductionCount;
        this.published = published;
        this.publishedAt = publishedAt;
        this.createdAt = createdAt;
    }

    public Long getJournalId() {
        return journalId;
    }

    public Long getSubmittedCount() {
        return submittedCount;
    }

    public Long getInProductionCount() {
        return InProductionCount;
    }

    public Long getInSubmissionCount() {
        return InSubmissionCount;
    }

    public Long getInReviewCount() {
        return InReviewCount;
    }

    public Long getInCopyEditingCount() {
        return InCopyEditingCount;
    }

    public String getJournalTitle() {
        return journalTitle;
    }

    public Boolean getPublished() {
        return published;
    }

    public Instant getPublishedAt() {
        return publishedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
