package com.kjms.service.dto;

import java.time.Instant;
import java.util.List;

public class ProductionDiscussionMessage {

    private Long id;

    private Long discussionId;

    private String userFullName;

    private String message;

    private List<ProductionDiscussionMessageFile> productionDiscussionMessageFiles;


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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public List<ProductionDiscussionMessageFile> getProductionDiscussionMessageFiles() {
        return productionDiscussionMessageFiles;
    }

    public void setProductionDiscussionMessageFiles(List<ProductionDiscussionMessageFile> productionDiscussionMessageFiles) {
        this.productionDiscussionMessageFiles = productionDiscussionMessageFiles;
    }
}
