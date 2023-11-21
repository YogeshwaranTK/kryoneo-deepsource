package com.kjms.domain;


import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "jm_copy_editing_discussion_message")
public class EntityCopyEditingDiscussionMessage {
    // TODO: 27-Oct-23 @varghesh generation type
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id", foreignKey = @ForeignKey(name = "fk_jm_copy_editing_discussion_message_submission_id"), nullable = false)
    private EntitySubmission submission;
    // TODO: 13-Nov-23 @varghesh (_ missing)
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_id", foreignKey = @ForeignKey(name = "fkjm_copy_editing_discussion_message_journal_id"), nullable = false)
    private EntityJournal journal;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "discussion_id", foreignKey = @ForeignKey(name = "fk_jm_copy_editing_discussion_message_discussion_id"), nullable = false)
    private EntityCopyEditingDiscussion  discussion;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_jm_copy_editing_discussion_message_user_id"), nullable = false)
    private EntityUser user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", foreignKey = @ForeignKey(name = "fk_jm_copy_editing_discussion_message_ref_files"))
    private List<EntityCopyEditingDiscussionMessageFile> files;

    @Column(name = "message",columnDefinition = "TEXT")
    private String message;


    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;


    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", foreignKey = @ForeignKey(name = "fk_jm_copy_editing_discussion_message_created_by"))
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

    public EntityJournal getJournal() {
        return journal;
    }

    public void setJournal(EntityJournal journal) {
        this.journal = journal;
    }

    public EntityCopyEditingDiscussion getDiscussion() {
        return discussion;
    }

    public void setDiscussion(EntityCopyEditingDiscussion discussion) {
        this.discussion = discussion;
    }

    public EntityUser getUser() {
        return user;
    }

    public void setUser(EntityUser user) {
        this.user = user;
    }

    public List<EntityCopyEditingDiscussionMessageFile> getFiles() {
        return files;
    }

    public void setFiles(List<EntityCopyEditingDiscussionMessageFile> files) {
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
}
