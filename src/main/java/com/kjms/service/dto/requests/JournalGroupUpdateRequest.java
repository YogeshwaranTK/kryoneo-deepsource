package com.kjms.service.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Represents a request object for creating a new Journal Group update.
 */
@Schema(description = "Request Body for Update Journal Group", title = "Journal Group Update")
public class JournalGroupUpdateRequest {

    @Schema(required = true)
    @NotNull
    private Long id;

    @Schema(required = true)
    @NotNull
    @NotBlank
    private String name;


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
}
