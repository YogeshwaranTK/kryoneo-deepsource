package com.kjms.service.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Represents a request object for creating a new Journal Group create.
 */
@Schema(description = "Request Body for Create Journal Group", title = "Journal Group Create")
public class JournalGroupCreateRequest {

    @Schema(required = true)
    @NotNull
    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
