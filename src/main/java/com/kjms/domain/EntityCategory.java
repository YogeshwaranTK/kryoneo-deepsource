package com.kjms.domain;

import javax.persistence.*;

/**
 * Entity class representing a category in the journal management system.
 */
@Entity
@Table(name = "jm_category")
public class EntityCategory {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;

    @Column(name = "name", nullable = false, length = 100, unique = true)
    private String name;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", foreignKey = @ForeignKey(name = "fk_jm_category_parent_id"))
    private EntityCategory parentId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EntityCategory getParentId() {
        return parentId;
    }

    public void setParentId(EntityCategory parentId) {
        this.parentId = parentId;
    }
}
