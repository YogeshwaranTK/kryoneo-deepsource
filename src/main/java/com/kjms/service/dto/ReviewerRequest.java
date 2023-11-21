package com.kjms.service.dto;

import com.kjms.domain.ReviewerReviewType;

import java.time.Instant;
import java.util.List;

public class ReviewerRequest {

    private Long reviewRoundId;

    private String userId;

    private Instant responseDueDate;

    private Instant reviewDueDate;

    private ReviewerReviewType reviewerReviewType;

    private String message;

    private List<Long> reviewRoundFileIds;

    public Long getReviewRoundId() {
        return reviewRoundId;
    }

    public void setReviewRoundId(Long reviewRoundId) {
        this.reviewRoundId = reviewRoundId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Instant getResponseDueDate() {
        return responseDueDate;
    }

    public void setResponseDueDate(Instant responseDueDate) {
        this.responseDueDate = responseDueDate;
    }

    public Instant getReviewDueDate() {
        return reviewDueDate;
    }

    public void setReviewDueDate(Instant reviewDueDate) {
        this.reviewDueDate = reviewDueDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ReviewerReviewType getSubmissionReviewType() {
        return reviewerReviewType;
    }

    public void setSubmissionReviewType(ReviewerReviewType reviewerReviewType) {
        this.reviewerReviewType = reviewerReviewType;
    }

    public List<Long> getReviewRoundFileIds() {
        return reviewRoundFileIds;
    }

    public void setReviewRoundFileIds(List<Long> reviewRoundFileIds) {
        this.reviewRoundFileIds = reviewRoundFileIds;
    }

}
