package com.kjms.domain;

import java.time.Instant;
import javax.persistence.*;

/**
 * Entity class representing an article Submission in the journal management system.
 */
@Entity
@Table(name = "jm_submission_article")
public class EntitySubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;
    // TODO: 13-Nov-23 @varghesh (length missing)
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SubmissionStatus status;

    @Column(name = "prefix", length = 50)
    private String prefix;
    @Column(name = "last_updated_prefix", length = 50)
    private String lastUpdatedPrefix;
    @Column(name = "title", nullable = false, length = 100)
    private String title;
    @Column(name = "last_updated_title", length = 100)
    private String lastUpdatedTitle;

    @Column(name = "sub_title", length = 100)
    private String subTitle;

    @Column(name = "last_updated_sub_title", length = 100)
    private String lastUpdatedSubTitle;

    //// TODO: 14-11-2023 To be Abstract
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "last_updated_desc", columnDefinition = "TEXT")
    private String lastUpdatedDesc;

    @Column(name = "decline_remarks", columnDefinition = "TEXT")
    private String declineRemarks;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "decline_by", foreignKey = @ForeignKey(name = "fk_jm_submission_article_decline_by"))
    private EntityUser declineBy;

    @Column(name = "decline_date")
    private Instant declineDate;

    @Column(name = "submitted_date")
    private Instant submittedDate;

    @Column(name = "submitted_accept_date")
    private Instant submittedAcceptDate;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "submitted_accept_by", foreignKey = @ForeignKey(name = "fk_jm_submission_article_submitted_accept_by"))
    private EntityUser submittedAcceptBy;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_id", foreignKey = @ForeignKey(name = "fk_jm_submission_article_journal_id"), updatable = false, nullable = false)
    private EntityJournal journal;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_lang_id", foreignKey = @ForeignKey(name = "fk_jm_submission_article_journal_lang_id"))
    private EntityJournalLanguage journalLanguage;


    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", foreignKey = @ForeignKey(name = "fk_jm_submission_article_author_id"), updatable = false, nullable = false)
    private EntityUser author;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", foreignKey = @ForeignKey(name = "fk_jm_submission_article_created_by"), updatable = false, nullable = false)
    private EntityUser createdBy;

    @Column(name = "created_at", updatable = false, nullable = false)
    private Instant createdAt;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "last_modified_by", foreignKey = @ForeignKey(name = "fk_jm_submission_article_last_modified_by"))
    private EntityUser lastModifiedBy;

    @Column(name = "last_modified_at")
    private Instant lastModifiedAt;

    @Column(name = "deleted_remarks", columnDefinition = "TEXT")
    private String deletedRemarks;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @Column(name = "agree", nullable = false)
    private Boolean agree = false;
    // TODO: 13-Nov-23 @varghesh (length missing)
    @Enumerated(EnumType.STRING)
    @Column(name = "workflow_stage", nullable = false)
    private WorkflowStage workflowStage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SubmissionStatus getStatus() {
        return status;
    }

    public void setStatus(SubmissionStatus status) {
        this.status = status;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getLastUpdatedPrefix() {
        return lastUpdatedPrefix;
    }

    public void setLastUpdatedPrefix(String lastUpdatedPrefix) {
        this.lastUpdatedPrefix = lastUpdatedPrefix;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLastUpdatedTitle() {
        return lastUpdatedTitle;
    }

    public void setLastUpdatedTitle(String lastUpdatedTitle) {
        this.lastUpdatedTitle = lastUpdatedTitle;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getLastUpdatedSubTitle() {
        return lastUpdatedSubTitle;
    }

    public void setLastUpdatedSubTitle(String lastUpdatedSubTitle) {
        this.lastUpdatedSubTitle = lastUpdatedSubTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLastUpdatedDesc() {
        return lastUpdatedDesc;
    }

    public void setLastUpdatedDesc(String lastUpdatedDesc) {
        this.lastUpdatedDesc = lastUpdatedDesc;
    }

    public String getDeclineRemarks() {
        return declineRemarks;
    }

    public void setDeclineRemarks(String declineRemarks) {
        this.declineRemarks = declineRemarks;
    }

    public EntityUser getDeclineBy() {
        return declineBy;
    }

    public void setDeclineBy(EntityUser declineBy) {
        this.declineBy = declineBy;
    }

    public Instant getDeclineDate() {
        return declineDate;
    }

    public void setDeclineDate(Instant declineDate) {
        this.declineDate = declineDate;
    }

    public Instant getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(Instant submittedDate) {
        this.submittedDate = submittedDate;
    }

    public Instant getSubmittedAcceptDate() {
        return submittedAcceptDate;
    }

    public void setSubmittedAcceptDate(Instant submittedAcceptDate) {
        this.submittedAcceptDate = submittedAcceptDate;
    }

    public EntityUser getSubmittedAcceptBy() {
        return submittedAcceptBy;
    }

    public void setSubmittedAcceptBy(EntityUser submittedAcceptBy) {
        this.submittedAcceptBy = submittedAcceptBy;
    }

    public EntityJournal getJournal() {
        return journal;
    }

    public void setJournal(EntityJournal journal) {
        this.journal = journal;
    }

    public EntityJournalLanguage getJournalLanguage() {
        return journalLanguage;
    }

    public void setJournalLanguage(EntityJournalLanguage journalLanguage) {
        this.journalLanguage = journalLanguage;
    }

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

    public EntityUser getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(EntityUser lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(Instant lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

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

    public Boolean getAgree() {
        return agree;
    }

    public void setAgree(Boolean agree) {
        this.agree = agree;
    }

    public WorkflowStage getWorkflowStage() {
        return workflowStage;
    }

    public void setWorkflowStage(WorkflowStage workflowStage) {
        this.workflowStage = workflowStage;
    }

    public EntityUser getAuthor() {
        return author;
    }

    public void setAuthor(EntityUser author) {
        this.author = author;
    }
}
