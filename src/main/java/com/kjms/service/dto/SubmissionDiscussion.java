package com.kjms.service.dto;

import com.kjms.domain.SubmissionDiscussionStatus;

import java.time.Instant;
import java.util.List;

public class SubmissionDiscussion {

    private Long id;

    private String topic;

    private String description;

    private Instant createdAt;

    private List<SubmissionDiscussionMessage> discussionMessages;
    private List<SubmissionDiscussionFile> discussionFiles;
    private SubmissionDiscussionStatus status;

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

    public List<SubmissionDiscussionMessage> getDiscussionMessages() {
        return discussionMessages;
    }

    public void setDiscussionMessages(List<SubmissionDiscussionMessage> discussionMessages) {
        this.discussionMessages = discussionMessages;
    }

    public List<SubmissionDiscussionFile> getDiscussionFiles() {
        return discussionFiles;
    }

    public void setDiscussionFiles(List<SubmissionDiscussionFile> discussionFiles) {
        this.discussionFiles = discussionFiles;
    }

    public SubmissionDiscussionStatus getStatus() {
        return status;
    }

    public void setStatus(SubmissionDiscussionStatus status) {
        this.status = status;
    }
}
