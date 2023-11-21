package com.kjms.service.dto;

import com.kjms.domain.ReviewRoundReviewerDiscussionStatus;

import java.time.Instant;
import java.util.List;

public class ReviewRoundReviewerDiscussion {

    private Long id;

    private String topic;

    private String description;

    private Instant createdAt;

    private List<ReviewRoundReviewerDiscussionMessage> discussionMessages;

    private List<ReviewRoundReviewerDiscussionFile> discussionFiles;

    private ReviewRoundReviewerDiscussionStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public List<ReviewRoundReviewerDiscussionMessage> getDiscussionMessages() {
        return discussionMessages;
    }

    public void setDiscussionMessages(List<ReviewRoundReviewerDiscussionMessage> discussionMessages) {
        this.discussionMessages = discussionMessages;
    }

    public List<ReviewRoundReviewerDiscussionFile> getDiscussionFiles() {
        return discussionFiles;
    }

    public void setDiscussionFiles(List<ReviewRoundReviewerDiscussionFile> discussionFiles) {
        this.discussionFiles = discussionFiles;
    }

    public ReviewRoundReviewerDiscussionStatus getStatus() {
        return status;
    }

    public void setStatus(ReviewRoundReviewerDiscussionStatus status) {
        this.status = status;
    }
}
