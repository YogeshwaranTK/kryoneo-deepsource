package com.kjms.service.dto;


import java.util.List;

public class ReviewRound {

    private Long id;

    private Integer round;

    private String name;

    private List<ReviewRoundDiscussion> reviewRoundDiscussions;

    private List<ReviewRoundFile> reviewRoundFiles;

    private List<ReviewRoundContributor> reviewRoundContributors;

    private List<ReviewRoundReviewerReview> reviewRoundReviewerReviews;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRound() {
        return round;
    }

    public void setRound(Integer round) {
        this.round = round;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ReviewRoundDiscussion> getReviewRoundDiscussions() {
        return reviewRoundDiscussions;
    }

    public void setReviewRoundDiscussions(List<ReviewRoundDiscussion> reviewRoundDiscussions) {
        this.reviewRoundDiscussions = reviewRoundDiscussions;
    }

    public List<ReviewRoundFile> getReviewRoundFiles() {
        return reviewRoundFiles;
    }

    public void setReviewRoundFiles(List<ReviewRoundFile> reviewRoundFiles) {
        this.reviewRoundFiles = reviewRoundFiles;
    }

    public List<ReviewRoundContributor> getReviewRoundContributors() {
        return reviewRoundContributors;
    }

    public void setReviewRoundContributors(List<ReviewRoundContributor> reviewRoundContributors) {
        this.reviewRoundContributors = reviewRoundContributors;
    }

    public List<ReviewRoundReviewerReview> getReviewRoundReviewerReviews() {
        return reviewRoundReviewerReviews;
    }

    public void setReviewRoundReviewerReviews(List<ReviewRoundReviewerReview> reviewRoundReviewerReviews) {
        this.reviewRoundReviewerReviews = reviewRoundReviewerReviews;
    }
}
