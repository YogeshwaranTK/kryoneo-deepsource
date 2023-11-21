package com.kjms.domain;

import javax.persistence.*;
import java.time.OffsetDateTime;

/**
 * Entity class representing a submission article categories in the journal management system.
 */
@Entity
@Table(name = "jm_submission_article_category")
public class EntitySubmissionCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_article_id", foreignKey = @ForeignKey(name = "fk_jm_sub_article_category_sub_article_id"), updatable = false, nullable = false)
    private EntitySubmission submission;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_id", foreignKey = @ForeignKey(name = "fk_jm_sub_article_category_journal_id"), updatable = false, nullable = false)
    private EntityJournal journal;
    // TODO: 13-Nov-23 @varghesh (fk naming not correct)
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_category_id", foreignKey = @ForeignKey(name = "fk_jm_sub_article_category_cat_id"), updatable = false, nullable = false)
    private EntityJournalCategory journalCategory;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @Column(name = "created_at", updatable = false, nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    public OffsetDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(OffsetDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

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

    public EntityJournalCategory getJournalCategory() {
        return journalCategory;
    }

    public void setJournalCategory(EntityJournalCategory journalCategory) {
        this.journalCategory = journalCategory;
    }
}
