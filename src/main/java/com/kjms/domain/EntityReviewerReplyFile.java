package com.kjms.domain;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "jm_reviewer_reply_file")
public class EntityReviewerReplyFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "review_round_id", foreignKey = @ForeignKey(name = "fk_jm_reviewer_reply_file_review_round_id"),nullable = false)
    private EntityReviewRound reviewRound;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "review_round_reviewer_id", foreignKey = @ForeignKey(name = "fk_jm_reviewer_reply_file_review_round_reviewer_id"),nullable = false)
    private EntityReviewRoundReview reviewRoundReviewer;

    @Column(name = "file", nullable = false, length = 300)
    private String file;

    @Column(name = "file_path", nullable = false, length = 500)
    private String filePath;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
    // TODO: 13-Nov-23 @varghesh (nullable, updatable missing)
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", foreignKey = @ForeignKey(name = "fk_jm_reviewer_reply_file_created_by"))
    private EntityUser createdBy;

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

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
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

    public EntityReviewRoundReview getReviewRoundReviewer() {
        return reviewRoundReviewer;
    }

    public void setReviewRoundReviewer(EntityReviewRoundReview reviewRoundReviewer) {
        this.reviewRoundReviewer = reviewRoundReviewer;
    }
}
