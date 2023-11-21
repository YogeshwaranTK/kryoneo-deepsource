package com.kjms.service.dto;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * A DTO for the {@link EntityGroup} entity
 */
public class Group implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private Long userCount;
    private String description;
    private OffsetDateTime lastModifiedDate;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Long getUserCount() {
        return userCount;
    }

    public void setUserCount(Long userCount) {
        this.userCount = userCount;
    }

    public OffsetDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(OffsetDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
