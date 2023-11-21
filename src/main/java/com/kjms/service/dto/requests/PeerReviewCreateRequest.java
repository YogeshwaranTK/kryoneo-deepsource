//package com.kjms.service.dto.requests;
//
//import io.swagger.v3.oas.annotations.media.Schema;
//
//import javax.validation.constraints.NotEmpty;
//import javax.validation.constraints.NotNull;
//import java.time.OffsetDateTime;
//import java.util.Set;
//
//@Schema(title = "Peer Review Create Request")
//public class PeerReviewCreateRequest {
//
//    @Schema(required = true)
//    @NotNull
//    private Long submissionArticleId;
//    @Schema(required = true)
//    @NotNull
//    @NotEmpty
//    private Set<String> users;
//
//    @Schema(required = true)
//    @NotNull
//    private PeerReviewRequestType reviewType;
//
//    @NotNull
//    private Set<Long> files;
//
//    @NotNull
//    private Set<FileRequest> attachments;
//
//    private OffsetDateTime responseDueDate;
//    private OffsetDateTime reviewDueDate;
//
//    private String comment;
//
//    @NotNull
//    private Boolean sendEmail;
//
//    public String getComment() {
//        return comment;
//    }
//
//    public void setComment(String comment) {
//        this.comment = comment;
//    }
//
//    public void setSendEmail(Boolean sendEmail) {
//        this.sendEmail = sendEmail;
//    }
//
//    public Boolean getSendEmail() {
//        return sendEmail;
//    }
//
//    public void setSentEmail(Boolean sentEmail) {
//        this.sendEmail = sentEmail;
//    }
//
//    public Set<FileRequest> getAttachments() {
//        return attachments;
//    }
//
//    public void setAttachments(Set<FileRequest> attachments) {
//        this.attachments = attachments;
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
//
//    public Set<Long> getFiles() {
//        return files;
//    }
//
//    public void setFiles(Set<Long> files) {
//        this.files = files;
//    }
//
//    public Set<String> getUsers() {
//        return users;
//    }
//
//    public void setUsers(Set<String> users) {
//        this.users = users;
//    }
//
//    public PeerReviewRequestType getReviewType() {
//        return reviewType;
//    }
//
//    public void setReviewType(PeerReviewRequestType reviewType) {
//        this.reviewType = reviewType;
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
//}
