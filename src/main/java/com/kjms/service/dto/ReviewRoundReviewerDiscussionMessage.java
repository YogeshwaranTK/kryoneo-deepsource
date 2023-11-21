package com.kjms.service.dto;


import com.kjms.service.ReviewRoundReviewerDiscussionMessageFile;

import java.time.Instant;
import java.util.List;

public class ReviewRoundReviewerDiscussionMessage {

    private Long id;

    private Long discussionId;

    private String userFullName;

    private String message;

    private List<ReviewRoundReviewerDiscussionMessageFile> discussionMessageFiles;


    private Instant createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDiscussionId() {
        return discussionId;
    }

    public void setDiscussionId(Long discussionId) {
        this.discussionId = discussionId;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ReviewRoundReviewerDiscussionMessageFile> getDiscussionMessageFiles() {
        return discussionMessageFiles;
    }

    public void setDiscussionMessageFiles(List<ReviewRoundReviewerDiscussionMessageFile> discussionMessageFiles) {
        this.discussionMessageFiles = discussionMessageFiles;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
