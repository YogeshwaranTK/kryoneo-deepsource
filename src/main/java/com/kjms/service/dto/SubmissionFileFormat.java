package com.kjms.service.dto;

import com.kjms.domain.EntityFileFormat;

import java.io.Serializable;

/**
 * A DTO for the {@link EntityFileFormat} entity
 */
public class SubmissionFileFormat implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;

    public SubmissionFileFormat() {}

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
}
