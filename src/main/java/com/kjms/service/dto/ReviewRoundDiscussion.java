package com.kjms.service.dto;

import com.kjms.domain.ReviewRoundDiscussionStatus;

import java.time.Instant;
import java.util.List;

public class ReviewRoundDiscussion {

    private Long id;

    private String topic;

    private String description;

    private Instant createdAt;

    private List<ReviewRoundDiscussionMessage> discussionMessages;
    private List<ReviewRoundDiscussionFile> discussionFiles;
    private ReviewRoundDiscussionStatus status;

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

    public List<ReviewRoundDiscussionMessage> getDiscussionMessages() {
        return discussionMessages;
    }

    public void setDiscussionMessages(List<ReviewRoundDiscussionMessage> discussionMessages) {
        this.discussionMessages = discussionMessages;
    }

    public List<ReviewRoundDiscussionFile> getDiscussionFiles() {
        return discussionFiles;
    }

    public void setDiscussionFiles(List<ReviewRoundDiscussionFile> discussionFiles) {
        this.discussionFiles = discussionFiles;
    }

    public ReviewRoundDiscussionStatus getStatus() {
        return status;
    }

    public void setStatus(ReviewRoundDiscussionStatus status) {
        this.status = status;
    }
}
