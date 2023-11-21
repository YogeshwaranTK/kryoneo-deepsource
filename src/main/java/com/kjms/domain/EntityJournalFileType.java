package com.kjms.domain;

import javax.persistence.*;
import java.time.Instant;

/**
 * Entity class representing journal file types.
 */
@Entity
@Table(name = "jm_journal_file_type")
public class EntityJournalFileType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "file_type_id", foreignKey = @ForeignKey(name = "fk_jm_journal_file_type_file_type_id"), updatable = false, nullable = false)
    private EntityFileType fileType;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_id", foreignKey = @ForeignKey(name = "fk_jm_journal_file_type_journal_id"), updatable = false, nullable = false)
    private EntityJournal journal;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", foreignKey = @ForeignKey(name = "fk_jm_journal_file_type_created_by"), updatable = false, nullable = false)
    private EntityUser createdBy;

    @Column(name = "created_at", updatable = false, nullable = false)
    private Instant createdAt;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "deleted_by", foreignKey = @ForeignKey(name = "fk_jm_journal_file_type_deleted_by"))
    private EntityUser deletedBy;
    // TODO: 13-Nov-23 @varghesh(this column seems unused)
    @Column(name = "deleted_at")
    private Instant deletedAt;

    public EntityUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(EntityUser createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }

    public EntityUser getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(EntityUser deletedBy) {
        this.deletedBy = deletedBy;
    }

    public EntityFileType getFileType() {
        return fileType;
    }

    public void setFileType(EntityFileType fileType) {
        this.fileType = fileType;
    }

    public EntityJournal getJournal() {
        return journal;
    }

    public void setJournal(EntityJournal journal) {
        this.journal = journal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

}
