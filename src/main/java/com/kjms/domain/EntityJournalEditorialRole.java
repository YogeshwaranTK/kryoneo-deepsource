package com.kjms.domain;

import javax.persistence.*;
import java.time.Instant;


@Entity
@Table(name = "jm_journal_editorial_role")
public class EntityJournalEditorialRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "name", nullable = false)
    private String roleName;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_type", nullable = false)
    private EditorialRoleType editorialRoleType;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_id", foreignKey = @ForeignKey(name = "fk_jm_journal_editorial_role_journal_id"), updatable = false, nullable = false)
    private EntityJournal journal;

    @Column(name = "is_submission_enable", nullable = false)
    private Boolean isSubmissionEnabled;

    @Column(name = "is_review_enable", nullable = false)
    private Boolean isReviewEnabled;

    @Column(name = "is_copy_editing_enable", nullable = false)
    private Boolean isCopyEditingEnabled;

    @Column(name = "is_production_enable", nullable = false)
    private Boolean isProductionEnabled;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", foreignKey = @ForeignKey(name = "fk_jm_journal_editorial_role_created_by"), updatable = false)
    private EntityUser createdBy;

    @Column(name = "created_at", updatable = false, nullable = false)
    private Instant createdAt;

    @Column(name = "is_default", nullable = false)
    private Boolean isDefault;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public EntityJournal getJournal() {
        return journal;
    }

    public void setJournal(EntityJournal journal) {
        this.journal = journal;
    }

    public Boolean getSubmissionEnabled() {
        return isSubmissionEnabled;
    }

    public void setSubmissionEnabled(Boolean submissionEnabled) {
        isSubmissionEnabled = submissionEnabled;
    }

    public Boolean getReviewEnabled() {
        return isReviewEnabled;
    }

    public void setReviewEnabled(Boolean reviewEnabled) {
        isReviewEnabled = reviewEnabled;
    }

    public Boolean getCopyEditingEnabled() {
        return isCopyEditingEnabled;
    }

    public void setCopyEditingEnabled(Boolean copyEditingEnabled) {
        isCopyEditingEnabled = copyEditingEnabled;
    }

    public Boolean getProductionEnabled() {
        return isProductionEnabled;
    }

    public void setProductionEnabled(Boolean productionEnabled) {
        isProductionEnabled = productionEnabled;
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

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public EditorialRoleType getEditorialRoleType() {
        return editorialRoleType;
    }

    public void setEditorialRoleType(EditorialRoleType editorialRoleType) {
        this.editorialRoleType = editorialRoleType;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }
}
