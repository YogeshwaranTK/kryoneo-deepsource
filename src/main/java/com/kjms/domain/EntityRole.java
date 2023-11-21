package com.kjms.domain;

import java.time.OffsetDateTime;
import javax.persistence.*;

/**
 * Entity class representing a roles in the journal management system.
 */
@Entity
@Table(name = "jm_role")
public class EntityRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;
    // TODO: 13-Nov-23 @varghesh (length missing)
    @Enumerated(EnumType.STRING)
    @Column(name = "default_role_type")
    private DefaultAdministrationRoleType roleType;

    @Column(name = "deleted_remarks", columnDefinition = "TEXT")
    private String deletedRemarks;
    // TODO: 13-Nov-23 @varghesh (nullable missing)
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", foreignKey = @ForeignKey(name = "fk_jm_role_created_by"), updatable = false)
    private EntityUser createdBy;

    @Column(name = "created_at", updatable = false, nullable = false)
    private OffsetDateTime createdAt;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "last_modified_by", foreignKey = @ForeignKey(name = "fk_jm_role_last_modified_by"))
    private EntityUser lastModifiedBy;

    @Column(name = "last_modified_at")
    private OffsetDateTime lastModifiedAt;

    public DefaultAdministrationRoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(DefaultAdministrationRoleType roleType) {
        this.roleType = roleType;
    }

    public EntityUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(EntityUser createdBy) {
        this.createdBy = createdBy;
    }

    public EntityUser getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(EntityUser lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(OffsetDateTime lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
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

    public void setDeletedRemarks(String deletedRemarks) {
        this.deletedRemarks = deletedRemarks;
    }


}
