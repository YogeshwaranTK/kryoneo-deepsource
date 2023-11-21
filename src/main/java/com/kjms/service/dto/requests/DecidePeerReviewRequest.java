package com.kjms.service.dto.requests;

public class DecidePeerReviewRequest {
    private DecidePeerReviewType peerReviewType;
    private Long submissionArticleId;

    public DecidePeerReviewType getPeerReviewType() {
        return peerReviewType;
    }

    public void setPeerReviewType(DecidePeerReviewType peerReviewType) {
        this.peerReviewType = peerReviewType;
    }

    public Long getSubmissionArticleId() {
        return submissionArticleId;
    }

    public void setSubmissionArticleId(Long submissionArticleId) {
        this.submissionArticleId = submissionArticleId;
    }
}
