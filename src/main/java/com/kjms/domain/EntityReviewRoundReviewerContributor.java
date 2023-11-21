package com.kjms.domain;


import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "jm_review_round_reviewer_contributor")
public class EntityReviewRoundReviewerContributor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "review_round_id", foreignKey = @ForeignKey(name = "fk_jm_review_round_reviewer_contributor_review_round_id"), nullable = false, insertable = false, updatable = false)
    private EntityReviewRound reviewRound;
    // TODO: 13-Nov-23 @varghesh (fk name contain unwanted _)
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "review_round_id", foreignKey = @ForeignKey(name = "fk_jm_review_round__reviewer_contributor_review_round_id"), nullable = false)
    private EntityReviewRoundReview reviewRoundReview;
    // TODO: 13-Nov-23 @varghesh (fk name contain unwanted _)
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_jm_review_round__reviewer_contributor_user_id"), nullable = false)
    private EntityUser user;
    // TODO: 13-Nov-23 @varghesh (fk name wrong)
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", foreignKey = @ForeignKey(name = "fk_jm_review_round_contributor_created_by"), updatable = false)
    private EntityUser createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EntityReviewRound getReviewRound() {
        return reviewRound;
    }

    public void setReviewRound(EntityReviewRound reviewRound) {
        this.reviewRound = reviewRound;
    }

    public EntityUser getUser() {
        return user;
    }

    public void setUser(EntityUser user) {
        this.user = user;
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

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public EntityReviewRoundReview getReviewRoundReview() {
        return reviewRoundReview;
    }

    public void setReviewRoundReview(EntityReviewRoundReview reviewRoundReview) {
        this.reviewRoundReview = reviewRoundReview;
    }
}
