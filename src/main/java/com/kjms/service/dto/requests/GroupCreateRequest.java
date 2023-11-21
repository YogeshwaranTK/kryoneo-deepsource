package com.kjms.service.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Represents a request object for create group.
 */
@Schema(description = "Request Body for Create Group", title = "Group Create")
public class GroupCreateRequest {

    @Schema(required = true, defaultValue = "New Group")
    @NotNull
    @NotBlank
    private String name;

    @Schema(required = true, defaultValue = "This is test description")
    @NotNull
    @NotBlank
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
