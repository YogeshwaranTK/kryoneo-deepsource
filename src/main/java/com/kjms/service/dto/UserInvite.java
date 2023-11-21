package com.kjms.service.dto;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * A DTO for the {@link EntityUserInvite} entity
 */
public class UserInvite implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    private String email;
    private OffsetDateTime invitedAt;
    private Boolean isAccepted;
    private Boolean isRejected;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public OffsetDateTime getInvitedAt() {
        return invitedAt;
    }

    public void setInvitedAt(OffsetDateTime invitedAt) {
        this.invitedAt = invitedAt;
    }

    public Boolean getAccepted() {
        return isAccepted;
    }

    public void setAccepted(Boolean accepted) {
        isAccepted = accepted;
    }

    public Boolean getRejected() {
        return isRejected;
    }

    public void setRejected(Boolean rejected) {
        isRejected = rejected;
    }
}
