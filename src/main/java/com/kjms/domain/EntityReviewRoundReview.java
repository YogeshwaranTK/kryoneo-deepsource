package com.kjms.domain;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "jm_review_round_reviewer")
public class EntityReviewRoundReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_reviewer_id", foreignKey = @ForeignKey(name = "fk_jm_review_round_reviewer_journal_reviewer_id"), updatable = false, nullable = false)
    private EntityJournalReviewer journalReviewer;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "review_round_id", foreignKey = @ForeignKey(name = "fk_jm_review_round_reviewer_review_round_id"), nullable = false)
    private EntityReviewRound reviewRound;
    // TODO: 13-Nov-23 @varghesh (length missing)
    @Enumerated(EnumType.STRING)
    @Column(name = "submission_review_type", nullable = false)
    private ReviewerReviewType reviewerReviewType;
    // TODO: 13-Nov-23 @varghesh (length missing)
    @Enumerated(EnumType.STRING)
    @Column(name = "review_current_status", nullable = false)
    private ReviewStatus reviewStatus;
    // TODO: 13-Nov-23 @varghesh (length missing)
    @Enumerated(EnumType.STRING)
    @Column(name = "reviewer_response")
    private ReviewerRecommendation reviewerRecommendation;

    @Column(name = "editor_and_author_comment", columnDefinition = "TEXT")
    private String editorAndAuthorComment;

    @Column(name = "editor_comment", columnDefinition = "TEXT")
    private String editorComment;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "review_round_reviewer_id", foreignKey = @ForeignKey(name = "fk_jm_review_round_reviewer_review_round_reviewer_id"))
    private List<EntityReviewerFile> reviewerFiles;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "review_round_reviewer_id", foreignKey = @ForeignKey(name = "fk_jm_review_round_reviewer_review_round_reviewer_reply_id"))
    private List<EntityReviewerReplyFile> reviewerReplyFiles;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "review_round_review_id", foreignKey = @ForeignKey(name = "fk_jm_review_round_reviewer_discussion_review_round_reviewer_id"))
    private List<EntityReviewRoundReviewerDiscussion> reviewRoundReviewerDiscussions;

    @Column(name = "rating")
    private Short rating;
    // TODO: 13-Nov-23 @varghesh (length missing)
    @Column(name = "reject_reason")
    private String rejectReason;

    @Column(name = "review_completed_at")
    private Instant reviewCompletedAt;

    @Column(name = "review_requested_date", nullable = false)
    private Instant reviewRequestedDate;
    @Column(name = "review_due_date")
    private Instant reviewDueDate;

    @Column(name = "response_due_date")
    private Instant responseDueDate;

    // TODO: 13-Nov-23 @varghesh (fk naming wrong)
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", foreignKey = @ForeignKey(name = "fk_jm_review_round_reviewer_review_round_reviewer_id"), updatable = false, nullable = false)
    private EntityUser createdBy;

    @Column(name = "created_at", updatable = false, nullable = false)
    private Instant createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EntityJournalReviewer getJournalReviewer() {
        return journalReviewer;
    }

    public void setJournalReviewer(EntityJournalReviewer journalReviewer) {
        this.journalReviewer = journalReviewer;
    }

    public EntityReviewRound getReviewRound() {
        return reviewRound;
    }

    public void setReviewRound(EntityReviewRound reviewRound) {
        this.reviewRound = reviewRound;
    }

    public ReviewerReviewType getReviewerReviewType() {
        return reviewerReviewType;
    }

    public void setReviewerReviewType(ReviewerReviewType reviewerReviewType) {
        this.reviewerReviewType = reviewerReviewType;
    }

    public ReviewStatus getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(ReviewStatus reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public ReviewerRecommendation getReviewerRecommendation() {
        return reviewerRecommendation;
    }

    public void setReviewerRecommendation(ReviewerRecommendation reviewerRecommendation) {
        this.reviewerRecommendation = reviewerRecommendation;
    }

    public String getEditorAndAuthorComment() {
        return editorAndAuthorComment;
    }

    public void setEditorAndAuthorComment(String editorAndAuthorComment) {
        this.editorAndAuthorComment = editorAndAuthorComment;
    }

    public String getEditorComment() {
        return editorComment;
    }

    public void setEditorComment(String editorComment) {
        this.editorComment = editorComment;
    }

    public List<EntityReviewerFile> getReviewerFiles() {
        return reviewerFiles;
    }

    public void setReviewerFiles(List<EntityReviewerFile> reviewerFiles) {
        this.reviewerFiles = reviewerFiles;
    }

    public List<EntityReviewerReplyFile> getReviewerReplyFiles() {
        return reviewerReplyFiles;
    }

    public void setReviewerReplyFiles(List<EntityReviewerReplyFile> reviewerReplyFiles) {
        this.reviewerReplyFiles = reviewerReplyFiles;
    }

    public Short getRating() {
        return rating;
    }

    public void setRating(Short rating) {
        this.rating = rating;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public Instant getReviewCompletedAt() {
        return reviewCompletedAt;
    }

    public void setReviewCompletedAt(Instant reviewCompletedAt) {
        this.reviewCompletedAt = reviewCompletedAt;
    }

    public Instant getReviewRequestedDate() {
        return reviewRequestedDate;
    }

    public void setReviewRequestedDate(Instant reviewRequestedDate) {
        this.reviewRequestedDate = reviewRequestedDate;
    }

    public Instant getReviewDueDate() {
        return reviewDueDate;
    }

    public void setReviewDueDate(Instant reviewDueDate) {
        this.reviewDueDate = reviewDueDate;
    }

    public Instant getResponseDueDate() {
        return responseDueDate;
    }

    public void setResponseDueDate(Instant responseDueDate) {
        this.responseDueDate = responseDueDate;
    }

    public EntityUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(EntityUser createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public List<EntityReviewRoundReviewerDiscussion> getReviewRoundReviewerDiscussions() {
        return reviewRoundReviewerDiscussions;
    }

    public void setReviewRoundReviewerDiscussions(List<EntityReviewRoundReviewerDiscussion> reviewRoundReviewerDiscussions) {
        this.reviewRoundReviewerDiscussions = reviewRoundReviewerDiscussions;
    }
}
