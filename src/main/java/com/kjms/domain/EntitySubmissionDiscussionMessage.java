package com.kjms.domain;


import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "jm_submission_discussion_message")
public class EntitySubmissionDiscussionMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id",nullable = false, foreignKey = @ForeignKey(name = "fk_jm_submission_discussion_message_submission_id"))
    private EntitySubmission submission;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "discussion_id", nullable = false,foreignKey = @ForeignKey(name = "fk_jm_submission_discussion_message_discussion_id"))
    private EntitySubmissionDiscussion discussion;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_id",nullable = false, foreignKey = @ForeignKey(name = "fk_jm_submission_discussion_message_journal_id"))
    private EntityJournal journal;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false, foreignKey = @ForeignKey(name = "fk_jm_submission_discussion_message_user_id"))
    private EntityUser user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", foreignKey = @ForeignKey(name = "fk_jm_submission_discussion_message_message_id"))
    private List<EntitySubmissionDiscussionMessageFile> files;

    @Column(name = "message",columnDefinition = "TEXT")
    private String message;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
    // TODO: 13-Nov-23 @varghesh (in fk name j missing)
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", foreignKey = @ForeignKey(name = "fk_m_submission_discussion_message_created_by"))
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

    public EntitySubmissionDiscussion getDiscussion() {
        return discussion;
    }

    public void setDiscussion(EntitySubmissionDiscussion discussion) {
        this.discussion = discussion;
    }

    public EntityUser getUser() {
        return user;
    }

    public void setUser(EntityUser user) {
        this.user = user;
    }

    public List<EntitySubmissionDiscussionMessageFile> getFiles() {
        return files;
    }

    public void setFiles(List<EntitySubmissionDiscussionMessageFile> files) {
        this.files = files;
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

    public EntityJournal getJournal() {
        return journal;
    }

    public void setJournal(EntityJournal journal) {
        this.journal = journal;
    }
}
