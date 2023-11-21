package com.kjms.domain;


import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "jm_production_discussion_message")
public class EntityProductionDiscussionMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id",nullable = false, foreignKey = @ForeignKey(name = "fk_jm_production_discussion_message_submission_id"))
    private EntitySubmission submission;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_id",nullable = false, foreignKey = @ForeignKey(name = "fk_jm_production_discussion_message_journal_id"))
    private EntityJournal journal;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "discussion_id", nullable = false,foreignKey = @ForeignKey(name = "fk_jm_production_discussion_message_discussion_id"))
    private EntityProductionDiscussion discussion;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false, foreignKey = @ForeignKey(name = "fk_jm_production_discussion_message_user_id"))
    private EntityUser user;
    // TODO: 24-Oct-23 @varghesh check nullable check column name
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", foreignKey = @ForeignKey(name = "fk_jm_production_discussion_message_message_id"))
    private List<EntityProductionDiscussionMessageFile> files;

    public List<EntityProductionDiscussionMessageFile> getFiles() {
        return files;
    }

    public void setFiles(List<EntityProductionDiscussionMessageFile> files) {
        this.files = files;
    }

    // TODO: 13-Nov-23 @varghesh (check nullable)
    @Column(name = "message",columnDefinition = "TEXT")
    private String message;


    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    // TODO: 13-Nov-23 @varghesh (nullable, updatable missing)
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", foreignKey = @ForeignKey(name = "fk_jm_production_discussion_message_created_by"))
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

    public EntityUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(EntityUser createdBy) {
        this.createdBy = createdBy;
    }

    public EntityProductionDiscussion getDiscussion() {
        return discussion;
    }

    public void setDiscussion(EntityProductionDiscussion discussion) {
        this.discussion = discussion;
    }

    public EntityJournal getJournal() {
        return journal;
    }

    public void setJournal(EntityJournal journal) {
        this.journal = journal;
    }



}
