package com.kjms.domain;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "jm_review_round_reviewer_discussion_file")
public class EntityReviewRoundReviewerDiscussionFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "discussion_id", foreignKey = @ForeignKey(name = "fk_jm_review_round_reviewer_discussion_file_discussion_id"),nullable = false)
    private EntityReviewRoundReviewerDiscussion reviewRoundDiscussion;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "file_type", foreignKey = @ForeignKey(name = "fk_jm_review_round_reviewer_discussion_file_file_type_id"),nullable = false)
    private EntityJournalFileType journalFileType;

    @Column(name = "file", nullable = false, length = 300)
    private String file;

    @Column(name = "file_path", nullable = false, length = 500)
    private String filePath;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", foreignKey = @ForeignKey(name = "fk_jm_review_round_reviewer_discussion_file_created_by"), nullable = false, updatable = false)
    private EntityUser createdBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EntityReviewRoundReviewerDiscussion getReviewRoundDiscussion() {
        return reviewRoundDiscussion;
    }

    public void setReviewRoundDiscussion(EntityReviewRoundReviewerDiscussion reviewRoundDiscussion) {
        this.reviewRoundDiscussion = reviewRoundDiscussion;
    }

    public EntityJournalFileType getJournalFileType() {
        return journalFileType;
    }

    public void setJournalFileType(EntityJournalFileType journalFileType) {
        this.journalFileType = journalFileType;
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
}
