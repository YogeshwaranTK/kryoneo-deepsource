package com.kjms.domain;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "jm_production_discussion")
public class EntityProductionDiscussion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    // TODO: 13-Nov-23 @varghesh (fk naming wrong)
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id", foreignKey = @ForeignKey(name = "fk_jm_production_discussion_submission_submission_id"), nullable = false)
    private EntitySubmission submission;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_id", foreignKey = @ForeignKey(name = "fk_jm_production_discussion_journal_id"), nullable = false)
    private EntityJournal journal;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_jm_production_discussion_user_id"), nullable = false)
    private EntityUser user;

    @Column(name = "topic", length = 500, nullable = false)
    private String topic;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private ProductionDiscussionStatus status;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "discussion_id", foreignKey = @ForeignKey(name = "fk_jm_production_discussion_discussion_id"))
    private List<EntityProductionDiscussionMessage> discussionMessages;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "discussion_id", foreignKey = @ForeignKey(name = "fk_jm_production_discussion_discussion_id"))
    private List<EntityProductionDiscussionFile> files;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", foreignKey = @ForeignKey(name = "fk_jm_production_discussion_created_by"), updatable = false, nullable = false)
    private EntityUser createdBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EntitySubmission getSubmission() {
        return submission;
    }

    public void setSubmission(EntitySubmission submission) {
        this.submission = submission;
    }

    public EntityUser getUser() {
        return user;
    }

    public void setUser(EntityUser user) {
        this.user = user;
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

    public EntityUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(EntityUser createdBy) {
        this.createdBy = createdBy;
    }

    public EntityJournal getJournal() {
        return journal;
    }

    public void setJournal(EntityJournal journal) {
        this.journal = journal;
    }

    public ProductionDiscussionStatus getStatus() {
        return status;
    }

    public void setStatus(ProductionDiscussionStatus status) {
        this.status = status;
    }

    public List<EntityProductionDiscussionFile> getFiles() {
        return files;
    }

    public void setFiles(List<EntityProductionDiscussionFile> files) {
        this.files = files;
    }

    public List<EntityProductionDiscussionMessage> getDiscussionMessages() {
        return discussionMessages;
    }

    public void setDiscussionMessages(List<EntityProductionDiscussionMessage> discussionMessages) {
        this.discussionMessages = discussionMessages;
    }
}
