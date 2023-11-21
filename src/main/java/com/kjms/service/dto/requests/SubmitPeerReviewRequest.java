package com.kjms.service.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Schema(title = "Submit Peer Review By Peer Reviewer")
public class SubmitPeerReviewRequest {

    @NotNull
    private Set<FileRequest> files;
    private String comment;

    private Long id;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<FileRequest> getFiles() {
        return files;
    }

    public void setFiles(Set<FileRequest> files) {
        this.files = files;
    }

}
