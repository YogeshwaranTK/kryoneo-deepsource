package com.kjms.service.dto;

import java.io.Serializable;
import java.time.OffsetDateTime;

public class PeerReviewComment implements Serializable {
    private static final long serialVersionUID = 1L;
    private OffsetDateTime createdDate;
    private String createdBy;

    private String comment;

    public OffsetDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(OffsetDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
