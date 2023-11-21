package com.kjms.service.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.util.List;

public class JournalGroupPermissionUpdateRequest {

    @Schema(required = true)
    @NotNull
    private List<JournalGroupPermissionRequest> journalGroupPermissions;
    @Schema(required = true)
    @NotNull
    private Long journalGroupId;

    public List<JournalGroupPermissionRequest> getJournalGroupPermissions() {
        return journalGroupPermissions;
    }

    public void setJournalGroupPermissions(List<JournalGroupPermissionRequest> journalGroupPermissions) {
        this.journalGroupPermissions = journalGroupPermissions;
    }

    public Long getJournalGroupId() {
        return journalGroupId;
    }

    public void setJournalGroupId(Long journalGroupId) {
        this.journalGroupId = journalGroupId;
    }
}
