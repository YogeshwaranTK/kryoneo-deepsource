package com.kjms.domain;


import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "jm_review_round_discussion_message")
public class EntityReviewRoundDiscussionMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "review_round_id",nullable = false, foreignKey = @ForeignKey(name = "fk_jm_review_round_discussion_message_review_round_id"))
    private EntityReviewRound reviewRound;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "discussion_id", nullable = false,foreignKey = @ForeignKey(name = "fk_jm_review_round_discussion_message_discussion_id"))
    private EntityReviewRoundDiscussion discussion;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false, foreignKey = @ForeignKey(name = "fk_jm_review_round_discussion_message_user_id"))
    private EntityUser user;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", foreignKey = @ForeignKey(name = "fk_jm_review_round_discussion_message_message_id"))
    private List<EntityReviewRoundDiscussionMessageFile> files;
    // TODO: 13-Nov-23 @varghesh (check nullable)
    @Column(name = "message",columnDefinition = "TEXT")
    private String message;


    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    // TODO: 13-Nov-23 @varghesh (nullable, updatable missing)
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", foreignKey = @ForeignKey(name = "fk_m_review_round_discussion_message_created_by"))
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

    public EntityReviewRoundDiscussion getDiscussion() {
        return discussion;
    }

    public void setDiscussion(EntityReviewRoundDiscussion discussion) {
        this.discussion = discussion;
    }

    public EntityUser getUser() {
        return user;
    }

    public void setUser(EntityUser user) {
        this.user = user;
    }

    public List<EntityReviewRoundDiscussionMessageFile> getFiles() {
        return files;
    }

    public void setFiles(List<EntityReviewRoundDiscussionMessageFile> files) {
        this.files = files;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
