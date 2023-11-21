package com.kjms.domain;

import java.time.OffsetDateTime;
import javax.persistence.*;

/**
 * Entity class representing a journal submission languages in the journal management system.
 */
@Entity
@Table(name = "jm_journal_submission_lang")
public class EntityJournalLanguage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_id", foreignKey = @ForeignKey(name = "fk_jm_journal_submission_lang_journal_id"), updatable = false, nullable = false)
    private EntityJournal journal;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "lang_key", foreignKey = @ForeignKey(name = "fk_jm_journal_submission_lang_lang_key"), updatable = false, nullable = false)
    private EntityLanguage language;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", foreignKey = @ForeignKey(name = "fk_jm_journal_submission_lang_created_by"), updatable = false, nullable = false)
    private EntityUser createdBy;

    @Column(name = "created_at", updatable = false, nullable = false)
    private OffsetDateTime createdAt;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "last_modified_by", foreignKey = @ForeignKey(name = "fk_jm_journal_submission_lang_last_modified_by"))
    private EntityUser lastModifiedBy;

    @Column(name = "last_modified_at")
    private OffsetDateTime lastModifiedAt;

    @Column(name = "deleted_remarks", columnDefinition = "TEXT")
    private String deletedRemarks;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    public String getDeletedRemarks() {
        return deletedRemarks;
    }

    public void setDeletedRemarks(String deletedRemarks) {
        this.deletedRemarks = deletedRemarks;
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

    public EntityJournal getJournal() {
        return journal;
    }

    public void setJournal(EntityJournal journal) {
        this.journal = journal;
    }

    public EntityLanguage getLanguage() {
        return language;
    }

    public void setLanguage(EntityLanguage language) {
        this.language = language;
    }

    public EntityUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(EntityUser createdBy) {
        this.createdBy = createdBy;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public EntityUser getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(EntityUser lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public OffsetDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(OffsetDateTime lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

}
