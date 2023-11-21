package com.kjms.domain;

import javax.persistence.*;

/**
 * Entity class representing a role permissions.
 */
@Entity
@Table(name = "jm_role_permission")
public class EntityRolePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", foreignKey = @ForeignKey(name = "fk_jm_role_permission_role_id"), nullable = false, updatable = false)
    private EntityRole role;
    // TODO: 24-Oct-23 @varghesh check length
    @Column(name = "permission", updatable = false, nullable = false, length = 100)
    private String permission;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EntityRole getRole() {
        return role;
    }

    public void setRole(EntityRole role) {
        this.role = role;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

}
