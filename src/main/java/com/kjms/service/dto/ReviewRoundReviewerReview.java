package com.kjms.service.dto;

import com.kjms.domain.ReviewStatus;
import com.kjms.domain.ReviewerRecommendation;
import com.kjms.domain.ReviewerReviewType;

import java.time.Instant;
import java.util.List;

public class ReviewRoundReviewerReview {

    private Long id;

    private Long reviewRoundId;

    private String fullName;

    private String guideLines;
    private ReviewerReviewType reviewerReviewType;

    private ReviewerRecommendation reviewerRecommendation;

    private ReviewStatus reviewStatus;

    private String editorAndAuthorComment;

    private String editorComment;

    private List<ReviewerFile> reviewerFiles;

    private Short rating;

    private Instant reviewCompletedAt;

    private Instant reviewDueDate;

    private Instant responseDueDate;

    private Long submissionId;

    private String submissionTitle;

    private String submissionAbstract;

    private List<ReviewRoundReviewerDiscussion> reviewerDiscussions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReviewRoundId() {
        return reviewRoundId;
    }

    public void setReviewRoundId(Long reviewRoundId) {
        this.reviewRoundId = reviewRoundId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public ReviewerReviewType getReviewerReviewType() {
        return reviewerReviewType;
    }

    public void setReviewerReviewType(ReviewerReviewType reviewerReviewType) {
        this.reviewerReviewType = reviewerReviewType;
    }

    public ReviewerRecommendation getReviewerRecommendation() {
        return reviewerRecommendation;
    }

    public void setReviewerRecommendation(ReviewerRecommendation reviewerRecommendation) {
        this.reviewerRecommendation = reviewerRecommendation;
    }

    public ReviewStatus getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(ReviewStatus reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public String getEditorAndAuthorComment() {
        return editorAndAuthorComment;
    }

    public void setEditorAndAuthorComment(String editorAndAuthorComment) {
        this.editorAndAuthorComment = editorAndAuthorComment;
    }

    public String getEditorComment() {
        return editorComment;
    }

    public void setEditorComment(String editorComment) {
        this.editorComment = editorComment;
    }

    public List<ReviewerFile> getReviewerFiles() {
        return reviewerFiles;
    }

    public void setReviewerFiles(List<ReviewerFile> reviewerFiles) {
        this.reviewerFiles = reviewerFiles;
    }

    public Short getRating() {
        return rating;
    }

    public void setRating(Short rating) {
        this.rating = rating;
    }

    public Instant getReviewCompletedAt() {
        return reviewCompletedAt;
    }

    public void setReviewCompletedAt(Instant reviewCompletedAt) {
        this.reviewCompletedAt = reviewCompletedAt;
    }

    public Instant getReviewDueDate() {
        return reviewDueDate;
    }

    public void setReviewDueDate(Instant reviewDueDate) {
        this.reviewDueDate = reviewDueDate;
    }

    public Instant getResponseDueDate() {
        return responseDueDate;
    }

    public void setResponseDueDate(Instant responseDueDate) {
        this.responseDueDate = responseDueDate;
    }

    public String getGuideLines() {
        return guideLines;
    }

    public void setGuideLines(String guideLines) {
        this.guideLines = guideLines;
    }

    public Long getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(Long submissionId) {
        this.submissionId = submissionId;
    }

    public String getSubmissionTitle() {
        return submissionTitle;
    }

    public void setSubmissionTitle(String submissionTitle) {
        this.submissionTitle = submissionTitle;
    }

    public String getSubmissionAbstract() {
        return submissionAbstract;
    }

    public void setSubmissionAbstract(String submissionAbstract) {
        this.submissionAbstract = submissionAbstract;
    }

    public List<ReviewRoundReviewerDiscussion> getReviewerDiscussions() {
        return reviewerDiscussions;
    }

    public void setReviewerDiscussions(List<ReviewRoundReviewerDiscussion> reviewerDiscussions) {
        this.reviewerDiscussions = reviewerDiscussions;
    }
}
