package com.kjms.service.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class JournalGroupUserRemoveRequest {
    @Schema(required = true)
    @NotNull
    private Long id;
    @Schema(required = true)
    @NotNull
    @NotBlank
    private String deletedRemarks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeletedRemarks() {
        return deletedRemarks;
    }

    public void setDeletedRemarks(String deletedRemarks) {
        this.deletedRemarks = deletedRemarks;
    }
}
