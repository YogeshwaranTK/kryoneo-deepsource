//package com.kjms.service.dto.requests;
//
//import io.swagger.v3.oas.annotations.media.Schema;
//
//import javax.validation.constraints.NotNull;
//
//@Schema(title = "Accept or Reject Assigned Peer Review By Reviewer")
//public class AcceptOrRejectPeerReviewRequest {
//    @Schema(required = true)
//    @NotNull
//    private Long id;
//    @Schema(required = true)
//    @NotNull
//    private Long submissionArticleId;
//
//    @Schema(required = true)
//    @NotNull
//    private DecisionType decision;
//
//    @Schema(required = true)
//    @NotNull
//    private String declineRemarks;
//
//    public String getDeclineRemarks() {
//        return declineRemarks;
//    }
//
//    public void setDeclineRemarks(String declineRemarks) {
//        this.declineRemarks = declineRemarks;
//    }
//
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public DecisionType getDecision() {
//        return decision;
//    }
//
//    public void setDecision(DecisionType decision) {
//        this.decision = decision;
//    }
//
//    public Long getSubmissionArticleId() {
//        return submissionArticleId;
//    }
//
//    public void setSubmissionArticleId(Long submissionArticleId) {
//        this.submissionArticleId = submissionArticleId;
//    }
//}
