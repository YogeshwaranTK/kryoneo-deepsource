package com.kjms.service.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class JournalContributorUserRequest {

    @Schema(
        description = "There is two type of user to contributor ,One is Existing User and another user is add by invite",
        required = true
    )
    @NotNull
    private ContributorUserAddTypes type;

    @NotNull
    @Email
    private String email;

    public ContributorUserAddTypes getType() {
        return type;
    }

    public void setType(ContributorUserAddTypes type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
