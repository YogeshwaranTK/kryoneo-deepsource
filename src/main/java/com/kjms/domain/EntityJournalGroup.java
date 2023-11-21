package com.kjms.domain;

import java.time.Instant;
import javax.persistence.*;

/**
 * A Journal Group.
 */
@Entity
@Table(name = "jm_journal_group")
public class EntityJournalGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "is_default", nullable = false, updatable = false)
    private Boolean isDefault;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_id", foreignKey = @ForeignKey(name = "fk_jm_journal_group_journal_id"), updatable = false, nullable = false)
    private EntityJournal journalId;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @Column(name = "deleted_remarks", columnDefinition = "TEXT")
    private String deletedRemarks;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", foreignKey = @ForeignKey(name = "fk_jm_journal_group_created_by"), updatable = false)
    private EntityUser createdBy;

    @Column(name = "created_at", updatable = false, nullable = false)
    private Instant createdAt;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "last_modified_by", foreignKey = @ForeignKey(name = "fk_jm_journal_group_last_modified_by"))
    private EntityUser lastModifiedBy;

    @Column(name = "last_modified_at")
    private Instant lastModifiedAt;

    // TODO: 14-10-2023 To  be converted as test to store and retrive in db
    // TODO: 13-Nov-23 @varghesh (add length=20)
    @Column(name = "default_group_type", updatable = false)
    @Enumerated(EnumType.STRING)
    private DefaultJournalGroupType groupType;

    public DefaultJournalGroupType getGroupType() {
        return groupType;
    }

    public void setGroupType(DefaultJournalGroupType groupType) {
        this.groupType = groupType;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public EntityJournal getJournalId() {
        return journalId;
    }

    public void setJournalId(EntityJournal journalId) {
        this.journalId = journalId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public String getDeletedRemarks() {
        return deletedRemarks;
    }

    public void setDeletedRemarks(String deletedRemarks) {
        this.deletedRemarks = deletedRemarks;
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
}
