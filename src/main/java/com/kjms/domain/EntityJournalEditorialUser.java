package com.kjms.domain;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "jm_journal_editorial_user")
public class EntityJournalEditorialUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_editorial_role_id", foreignKey = @ForeignKey(name = "fk_jm_journal_editorial_user_journal_editorial_role_id"), updatable = false, nullable = false)
    private EntityJournalEditorialRole entityJournalEditorialRole;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_jm_journal_editorial_user_user_id"), updatable = false, nullable = false)
    private EntityUser user;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", foreignKey = @ForeignKey(name = "fk_jm_journal_editorial_user_created_by"), nullable = false, updatable = false)
    private EntityUser createdBy;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @Column(name = "deleted_remarks", columnDefinition = "TEXT")
    private String deletedRemarks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EntityJournalEditorialRole getEntityJournalEditorialRole() {
        return entityJournalEditorialRole;
    }

    public void setEntityJournalEditorialRole(EntityJournalEditorialRole entityJournalEditorialRole) {
        this.entityJournalEditorialRole = entityJournalEditorialRole;
    }

    public EntityUser getUser() {
        return user;
    }

    public void setUser(EntityUser user) {
        this.user = user;
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

    public String getDeletedRemarks() {
        return deletedRemarks;
    }

    public void setDeletedRemarks(String deletedRemarks) {
        this.deletedRemarks = deletedRemarks;
    }
}
