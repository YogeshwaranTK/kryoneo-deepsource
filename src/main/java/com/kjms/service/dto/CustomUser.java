package com.kjms.service.dto;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * A DTO representing a {@link EntityJournalUser} journal contributor.
 */
public class CustomUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String userId;
    private String userFullName;
    private String email;
    private OffsetDateTime createdDate;

    public CustomUser() {
        // Empty constructor needed for Jackson.
    }

    public OffsetDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(OffsetDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
