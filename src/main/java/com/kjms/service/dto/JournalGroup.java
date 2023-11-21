package com.kjms.service.dto;

import com.kjms.domain.EntityJournalGroup;
import java.io.Serializable;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * A DTO representing a {@link EntityJournalGroup} journal group.
 */
public class JournalGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String createdByName;
    private Instant createdDate;
    private String lastModifiedByName;
    private Instant lastModifiedDate;
    private Long memberCount;
    private Boolean isDefault;

    private List<String> journalPermissionIds;

    public JournalGroup() {
        // Empty constructor needed for Jackson.
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public List<String> getJournalPermissionIds() {
        return journalPermissionIds;
    }

    public void setJournalPermissionIds(List<String> journalPermissionIds) {
        this.journalPermissionIds = journalPermissionIds;
    }

    public Long getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(Long memberCount) {
        this.memberCount = memberCount;
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

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public String getLastModifiedByName() {
        return lastModifiedByName;
    }

    public void setLastModifiedByName(String lastModifiedByName) {
        this.lastModifiedByName = lastModifiedByName;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
