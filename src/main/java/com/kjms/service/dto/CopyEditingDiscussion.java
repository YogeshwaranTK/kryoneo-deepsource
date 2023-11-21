package com.kjms.service.dto;

import com.kjms.domain.CopyEditingDiscussionStatus;

import java.time.Instant;
import java.util.List;

public class CopyEditingDiscussion {

    private Long id;

    private String topic;

    private String description;

    private Instant createdAt;

    private List<CopyEditingDiscussionMessage> discussionMessages;

    private List<CopyEditingDiscussionFile> discussionFiles;

    private CopyEditingDiscussionStatus status;

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

    public List<CopyEditingDiscussionFile> getDiscussionFiles() {
        return discussionFiles;
    }

    public void setDiscussionFiles(List<CopyEditingDiscussionFile> discussionFiles) {
        this.discussionFiles = discussionFiles;
    }

    public CopyEditingDiscussionStatus getStatus() {
        return status;
    }

    public void setStatus(CopyEditingDiscussionStatus status) {
        this.status = status;
    }

    public List<CopyEditingDiscussionMessage> getDiscussionMessages() {
        return discussionMessages;
    }

    public void setDiscussionMessages(List<CopyEditingDiscussionMessage> discussionMessages) {
        this.discussionMessages = discussionMessages;
    }
}
