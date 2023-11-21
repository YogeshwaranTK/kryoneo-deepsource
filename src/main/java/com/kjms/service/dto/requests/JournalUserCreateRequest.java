package com.kjms.service.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Represents a request object for creating a new journal user.
 */
@Schema(title = "Journal User Create", description = "Request Body for Journal User Create")
public class JournalUserCreateRequest {

    @Schema(required = true)
    @NotNull
    @NotEmpty
    private List<JournalContributorUserRequest> users;

    public List<JournalContributorUserRequest> getUsers() {
        return users;
    }

    public void setUsers(List<JournalContributorUserRequest> users) {
        this.users = users;
    }
}
