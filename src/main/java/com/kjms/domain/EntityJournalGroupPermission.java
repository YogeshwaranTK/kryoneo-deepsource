package com.kjms.domain;

import javax.persistence.*;

/**
 * A Journal Group Permissions.
 */
@Entity
@Table(name = "jm_journal_group_permission")
public class EntityJournalGroupPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_group_id", foreignKey = @ForeignKey(name = "fk_jm_journal_group_permission_journal_group_id"), updatable = false, nullable = false)
    private EntityJournalGroup journalGroupId;
    // TODO: 24-Oct-23 @vargesh check length
    @Column(name = "journal_perm_id", nullable = false, updatable = false, length = 200)
    private String journalPermissionId;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_id", foreignKey = @ForeignKey(name = "fk_jm_journal_group_permission_journal_id"), updatable = false, nullable = false)
    private EntityJournal journalId;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EntityJournalGroup getJournalGroupId() {
        return journalGroupId;
    }

    public void setJournalGroupId(EntityJournalGroup journalGroupId) {
        this.journalGroupId = journalGroupId;
    }

    public String getJournalPermissionId() {
        return journalPermissionId;
    }

    public void setJournalPermissionId(String journalPermissionId) {
        this.journalPermissionId = journalPermissionId;
    }

    public EntityJournal getJournalId() {
        return journalId;
    }

    public void setJournalId(EntityJournal journalId) {
        this.journalId = journalId;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
