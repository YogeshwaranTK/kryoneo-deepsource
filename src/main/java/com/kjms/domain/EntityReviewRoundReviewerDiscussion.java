package com.kjms.domain;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "jm_review_round_reviewer_discussion")
public class EntityReviewRoundReviewerDiscussion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;
    // TODO: 13-Nov-23 @varghesh (fk naming j missing)
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_m_review_round_reviewer_discussion_user_id"), nullable = false)
    private EntityUser user;

    @Column(name = "topic", length = 500, nullable = false)
    private String topic;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private ReviewRoundReviewerDiscussionStatus status;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "review_round_review_id", foreignKey = @ForeignKey(name = "fk_jm_review_round_reviewer_review_discussion_review_round_id"), nullable = false)
    private EntityReviewRoundReview reviewRoundReview;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "review_round_id", foreignKey = @ForeignKey(name = "fk_jm_review_round_reviewer_discussion_review_round_id"), nullable = false)
    private EntityReviewRound reviewRound;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "discussion_id", foreignKey = @ForeignKey(name = "fk_jm_review_round_reviewer_discussion_discussion_id"))
    private List<EntityReviewRoundReviewerDiscussionMessage> discussionMessages;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "discussion_id", foreignKey = @ForeignKey(name = "fk_jm_review_round_reviewer_discussion_discussion_id"))
    private List<EntityReviewRoundReviewerDiscussionFile> files;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", foreignKey = @ForeignKey(name = "fk_jm_review_round_reviewer_discussion_created_by"), updatable = false, nullable = false)
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

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public ReviewRoundReviewerDiscussionStatus getStatus() {
        return status;
    }

    public void setStatus(ReviewRoundReviewerDiscussionStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EntityReviewRound getReviewRound() {
        return reviewRound;
    }

    public void setReviewRound(EntityReviewRound reviewRound) {
        this.reviewRound = reviewRound;
    }

    public List<EntityReviewRoundReviewerDiscussionMessage> getDiscussionMessages() {
        return discussionMessages;
    }

    public void setDiscussionMessages(List<EntityReviewRoundReviewerDiscussionMessage> discussionMessages) {
        this.discussionMessages = discussionMessages;
    }

    public List<EntityReviewRoundReviewerDiscussionFile> getFiles() {
        return files;
    }

    public void setFiles(List<EntityReviewRoundReviewerDiscussionFile> files) {
        this.files = files;
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

    public EntityReviewRoundReview getReviewRoundReview() {
        return reviewRoundReview;
    }

    public void setReviewRoundReview(EntityReviewRoundReview reviewRoundReview) {
        this.reviewRoundReview = reviewRoundReview;
    }
}
