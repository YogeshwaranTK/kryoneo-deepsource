package com.kjms.service.dto;

import com.kjms.domain.ReviewerReviewType;

import java.time.Instant;
import java.util.List;

public class ReviewerReview {

    private Submission submission;

    private ReviewRoundReviewerReview reviewRoundReviewerReview;

    private String guideLines;

    private ReviewerReviewType reviewerReviewType;

    private List<ReviewRoundReviewerFile> reviewRoundReviewerFiles;

    private List<ReviewerReplyFile> reviewerReplyFiles;

    private List<ReviewerFile> reviewerFiles;

    private Instant reviewRequestedDate;

    private List<ReviewRoundDiscussion> reviewRoundDiscussions;

    public Submission getSubmission() {
        return submission;
    }

    public void setSubmission(Submission submission) {
        this.submission = submission;
    }

    public ReviewRoundReviewerReview getReviewRoundReviewerReview() {
        return reviewRoundReviewerReview;
    }

    public void setReviewRoundReviewerReview(ReviewRoundReviewerReview reviewRoundReviewerReview) {
        this.reviewRoundReviewerReview = reviewRoundReviewerReview;
    }

    public String getGuideLines() {
        return guideLines;
    }

    public void setGuideLines(String guideLines) {
        this.guideLines = guideLines;
    }

    public ReviewerReviewType getReviewerReviewType() {
        return reviewerReviewType;
    }

    public void setReviewerReviewType(ReviewerReviewType reviewerReviewType) {
        this.reviewerReviewType = reviewerReviewType;
    }

    public List<ReviewRoundReviewerFile> getReviewRoundReviewerFiles() {
        return reviewRoundReviewerFiles;
    }

    public void setReviewRoundReviewerFiles(List<ReviewRoundReviewerFile> reviewRoundReviewerFiles) {
        this.reviewRoundReviewerFiles = reviewRoundReviewerFiles;
    }

    public List<ReviewerReplyFile> getReviewerReplyFiles() {
        return reviewerReplyFiles;
    }

    public void setReviewerReplyFiles(List<ReviewerReplyFile> reviewerReplyFiles) {
        this.reviewerReplyFiles = reviewerReplyFiles;
    }

    public List<ReviewerFile> getReviewerFiles() {
        return reviewerFiles;
    }

    public void setReviewerFiles(List<ReviewerFile> reviewerFiles) {
        this.reviewerFiles = reviewerFiles;
    }

    public Instant getReviewRequestedDate() {
        return reviewRequestedDate;
    }

    public void setReviewRequestedDate(Instant reviewRequestedDate) {
        this.reviewRequestedDate = reviewRequestedDate;
    }

    public List<ReviewRoundDiscussion> getReviewRoundDiscussions() {
        return reviewRoundDiscussions;
    }

    public void setReviewRoundDiscussions(List<ReviewRoundDiscussion> reviewRoundDiscussions) {
        this.reviewRoundDiscussions = reviewRoundDiscussions;
    }
}
