package com.kjms.domain;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "jm_review_round_file")
public class EntityReviewRoundFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "review_round_id", foreignKey = @ForeignKey(name = "fk_jm_review_round_file_review_round_id"),nullable = false)
    private EntityReviewRound reviewRound;

    // TODO: 27-Oct-23 @varghesh check nullable
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "file_type_id", foreignKey = @ForeignKey(name = "fk_jm_review_round_file_file_type_id"),nullable = false)
    private EntityJournalFileType journalFileType;

    @Column(name = "file", nullable = false, length = 300)
    private String file;

    @Column(name = "file_path", nullable = false, length = 500)
    private String filePath;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    // TODO: 13-Nov-23  @varghesh (nullable, updatable missing)
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", foreignKey = @ForeignKey(name = "fk_jm_review_round_file_created_by"))
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
