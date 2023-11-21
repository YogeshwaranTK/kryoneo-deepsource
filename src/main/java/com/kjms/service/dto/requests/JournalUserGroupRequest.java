package com.kjms.service.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Represents a request object for creating a new Journal user group.
 */
@Schema(title = "Journal User Group", description = "Request Body for Journal User Group")
public class JournalUserGroupRequest {


    @Schema(required = true)
    @NotNull
    @NotEmpty
    private List<String> userIds;

    @Schema(required = true)
    @NotNull
    private Long journalGroupId;

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public Long getJournalGroupId() {
        return journalGroupId;
    }

    public void setJournalGroupId(Long journalGroupId) {
        this.journalGroupId = journalGroupId;
    }
}
