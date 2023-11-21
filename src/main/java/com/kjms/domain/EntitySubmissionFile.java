package com.kjms.domain;

import javax.persistence.*;
import java.time.Instant;

/**
 * Entity class representing an Article Submission file.
 */
@Entity
@Table(name = "jm_submission_file")
public class EntitySubmissionFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id", foreignKey = @ForeignKey(name = "fk_jm_submission_file_submission_id"), nullable = false)
    private EntitySubmission submission;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_id", foreignKey = @ForeignKey(name = "fk_jm_submission_file_journal_id"), nullable = false)
    private EntityJournal journal;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "file_type", foreignKey = @ForeignKey(name = "fk_jm_submission_file_file_type_id"), nullable = false)
    private EntityJournalFileType journalFileType;

    @Column(name = "file", nullable = false, length = 300)
    private String file;

    @Column(name = "file_path", nullable = false, length = 500)
    private String filePath;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", foreignKey = @ForeignKey(name = "fk_jm_submission_file_created_by"), updatable = false, nullable = false)
    private EntityUser createdBy;

    @Column(name = "is_revised")
    private Boolean isRevised;
    @Column(name = "revised_file_id")
    private Long revisedFileId;
    @Column(name = "revised_at", updatable = false)
    private Instant revisedAt;
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

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

    public EntityJournalFileType getJournalFileType() {
        return journalFileType;
    }

    public void setJournalFileType(EntityJournalFileType journalFileType) {
        this.journalFileType = journalFileType;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
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

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }


    public Boolean getRevised() {
        return isRevised;
    }

    public void setRevised(Boolean revised) {
        isRevised = revised;
    }

    public Long getRevisedFileId() {
        return revisedFileId;
    }

    public void setRevisedFileId(Long revisedFileId) {
        this.revisedFileId = revisedFileId;
    }

    public Instant getRevisedAt() {
        return revisedAt;
    }

    public void setRevisedAt(Instant revisedAt) {
        this.revisedAt = revisedAt;
    }
}
