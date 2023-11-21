package com.kjms.service.dto;

import java.io.Serializable;

/**
 * A DTO for the {@link EntityGroupUser} entity
 */
public class GroupUsers implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String userId;
    private String email;
    private String fullName;
    private boolean activated;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }
}
