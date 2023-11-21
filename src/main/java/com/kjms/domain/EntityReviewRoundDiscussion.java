package com.kjms.domain;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "jm_review_round_discussion")
public class EntityReviewRoundDiscussion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "review_round_id", foreignKey = @ForeignKey(name = "fk_jm_review_round_discussion_review_round_id"), nullable = false)
    private EntityReviewRound reviewRound;
    // TODO: 13-Nov-23 @varghesh (In fk name j missing)
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_m_review_round_discussion_user_id"), nullable = false)
    private EntityUser user;

    @Column(name = "topic", length = 500, nullable = false)
    private String topic;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private ReviewRoundDiscussionStatus status;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "discussion_id", foreignKey = @ForeignKey(name = "fk_jm_review_round_discussion_discussion_id"))
    private List<EntityReviewRoundDiscussionMessage> discussionMessages;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "discussion_id", foreignKey = @ForeignKey(name = "fk_jm_review_round_discussion_discussion_id"))
    private List<EntityReviewRoundDiscussionFile> files;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", foreignKey = @ForeignKey(name = "fk_jm_review_round_discussion_created_by"), updatable = false, nullable = false)
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

    public ReviewRoundDiscussionStatus getStatus() {
        return status;
    }

    public void setStatus(ReviewRoundDiscussionStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<EntityReviewRoundDiscussionMessage> getDiscussionMessages() {
        return discussionMessages;
    }

    public void setDiscussionMessages(List<EntityReviewRoundDiscussionMessage> discussionMessages) {
        this.discussionMessages = discussionMessages;
    }

    public List<EntityReviewRoundDiscussionFile> getFiles() {
        return files;
    }

    public void setFiles(List<EntityReviewRoundDiscussionFile> files) {
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
}
