package com.kjms.domain;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "jm_review_round_reviewer_file")
public class EntityReviewRoundReviewerFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "review_round_reviewer_id", foreignKey = @ForeignKey(name = "fk_jm_review_round_reviewer_file_review_round_reviewer_id"),nullable = false)
    private EntityReviewRoundReview reviewRoundReviewer;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "review_round_file_id", foreignKey = @ForeignKey(name = "fk_jm_review_round_reviewer_file_review_round_id"),nullable = false)
    private EntityReviewRoundFile reviewRoundFile;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
    // TODO: 13-Nov-23 @varghesh (nullable, updated missing)
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", foreignKey = @ForeignKey(name = "fk_jjm_review_round_reviewer_file_created_by"))
    private EntityUser createdBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EntityReviewRoundReview getReviewRoundReviewer() {
        return reviewRoundReviewer;
    }

    public void setReviewRoundReviewer(EntityReviewRoundReview reviewRoundReviewer) {
        this.reviewRoundReviewer = reviewRoundReviewer;
    }

    public EntityReviewRoundFile getReviewRoundFile() {
        return reviewRoundFile;
    }

    public void setReviewRoundFile(EntityReviewRoundFile reviewRoundFile) {
        this.reviewRoundFile = reviewRoundFile;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public EntityUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(EntityUser createdBy) {
        this.createdBy = createdBy;
    }
}
