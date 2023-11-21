package com.kjms.domain;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "jm_review_round_reviewer_discussion_member")
public class EntityReviewRoundReviewerDiscussionMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;
    // TODO: 13-Nov-23 @varghesh (fk name wrong)
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_jm_review_round_reviewer_discussion_user_user_id"), nullable = false)
    private EntityUser user;
    // TODO: 13-Nov-23 @varghesh (fk name wrong)
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "discussion_id", foreignKey = @ForeignKey(name = "fk_jm_review_round_reviewer_discussion_user_discussion_id"), nullable = false)
    private EntityReviewRoundReviewerDiscussion discussion;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
    // TODO: 13-Nov-23 @varghesh (fk name wrong)
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", foreignKey = @ForeignKey(name = "fk_jm_review_round_reviewer_discussion_user_created_by"), updatable = false, nullable = false)
    private EntityUser createdBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EntityUser getUser() {
        return user;
    }

    public void setUser(EntityUser user) {
        this.user = user;
    }

    public EntityReviewRoundReviewerDiscussion getDiscussion() {
        return discussion;
    }

    public void setDiscussion(EntityReviewRoundReviewerDiscussion discussion) {
        this.discussion = discussion;
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
