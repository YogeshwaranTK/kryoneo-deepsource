package com.kjms.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link EntityGroup} entity
 */
public class GroupView implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private String description;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
