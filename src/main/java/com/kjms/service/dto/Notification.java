package com.kjms.service.dto;

import java.io.Serializable;
import java.time.OffsetDateTime;

public class Notification implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String title;
    private String desc;
    private OffsetDateTime createdDate;
    private String formattedCreatedDate;

    public Notification() {
    }

    public String getFormattedCreatedDate() {
        return formattedCreatedDate;
    }

    public void setFormattedCreatedDate(String formattedCreatedDate) {
        this.formattedCreatedDate = formattedCreatedDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OffsetDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(OffsetDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
