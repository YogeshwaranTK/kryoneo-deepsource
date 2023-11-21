//package com.kjms.service.dto.requests;
//
//import io.swagger.v3.oas.annotations.media.Schema;
//
//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.NotEmpty;
//import javax.validation.constraints.NotNull;
//import java.time.OffsetDateTime;
//import java.util.List;
//import java.util.Set;
//
///**
// * Represents a request object for create submission article review.
// */
//@Schema(description = "Request Body for Create Submission Article Review", title = "Submission Create Review")
//public class RevisionCreateRequest {
//
//    @Schema(required = true)
//    @NotNull
//    @NotEmpty
//    private Set<Long> files;
//
//    @Schema(required = true)
//    @NotNull
//    @NotEmpty
//    List<SubmissionArticleRevisionAttachmentRequest> attachments;
//
//    @Schema(required = true)
//    @NotNull
//    @NotBlank
//    private String description;
//    private OffsetDateTime responseDueDate;
//    private OffsetDateTime reviewDueDate;
//
//    @Schema(required = true)
//    @NotNull
//    private Long submissionArticleId;
//
//    public List<SubmissionArticleRevisionAttachmentRequest> getAttachments() {
//        return attachments;
//    }
//
//    public void setAttachments(List<SubmissionArticleRevisionAttachmentRequest> attachments) {
//        this.attachments = attachments;
//    }
//
//    public Long getSubmissionArticleId() {
//        return submissionArticleId;
//    }
//
//    public void setSubmissionArticleId(Long submissionArticleId) {
//        this.submissionArticleId = submissionArticleId;
//    }
//
//    public Set<Long> getFiles() {
//        return files;
//    }
//
//    public void setFiles(Set<Long> files) {
//        this.files = files;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public OffsetDateTime getResponseDueDate() {
//        return responseDueDate;
//    }
//
//    public void setResponseDueDate(OffsetDateTime responseDueDate) {
//        this.responseDueDate = responseDueDate;
//    }
//
//    public OffsetDateTime getReviewDueDate() {
//        return reviewDueDate;
//    }
//
//    public void setReviewDueDate(OffsetDateTime reviewDueDate) {
//        this.reviewDueDate = reviewDueDate;
//    }
//}
