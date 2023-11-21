package com.kjms.service.dto;

import com.kjms.domain.ProductionDiscussionStatus;

import java.time.Instant;
import java.util.List;

public class ProductionDiscussion {

    private Long id;

    private String topic;

    private String description;

    private Instant createdAt;

    private List<ProductionDiscussionMessage> discussionMessages;
    private List<ProductionDiscussionFile> discussionFiles;
    private ProductionDiscussionStatus status;

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

    public ProductionDiscussionStatus getStatus() {
        return status;
    }

    public void setStatus(ProductionDiscussionStatus status) {
        this.status = status;
    }

    public List<ProductionDiscussionMessage> getDiscussionMessages() {
        return discussionMessages;
    }

    public void setDiscussionMessages(List<ProductionDiscussionMessage> discussionMessages) {
        this.discussionMessages = discussionMessages;
    }

    public List<ProductionDiscussionFile> getDiscussionFiles() {
        return discussionFiles;
    }

    public void setDiscussionFiles(List<ProductionDiscussionFile> discussionFiles) {
        this.discussionFiles = discussionFiles;
    }
}
