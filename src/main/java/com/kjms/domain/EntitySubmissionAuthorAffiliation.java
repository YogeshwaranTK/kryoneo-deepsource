package com.kjms.domain;

import javax.persistence.*;

/**
 * Entity class representing an Article Submission file.
 */
@Entity
@Table(name = "jm_submission_article_author_affiliation")
public class EntitySubmissionAuthorAffiliation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;
    // TODO: 24-Oct-23 @varghesh check length
    @Column(name = "affiliation", nullable = false, length = 100)
    private String affiliation;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_article_author_id", foreignKey = @ForeignKey(name = "fk_jm_sub_article_author_affiliation_sub_article_author_id"), updatable = false, nullable = false)
    private EntitySubmissionAuthor submissionArticleAuthor;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_article_id", foreignKey = @ForeignKey(name = "fk_jm_sub_article_author_affiliation_sub_article_id"), updatable = false, nullable = false)
    private EntitySubmission submissionArticle;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_id", foreignKey = @ForeignKey(name = "fk_jm_submission_article_author_affiliation_journal_id"), updatable = false, nullable = false)
    private EntityJournal journal;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;


    public EntitySubmissionAuthor getSubmissionArticleAuthor() {
        return submissionArticleAuthor;
    }

    public void setSubmissionArticleAuthor(EntitySubmissionAuthor submissionArticleAuthor) {
        this.submissionArticleAuthor = submissionArticleAuthor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public EntitySubmission getSubmissionArticle() {
        return submissionArticle;
    }

    public void setSubmissionArticle(EntitySubmission submissionArticle) {
        this.submissionArticle = submissionArticle;
    }

    public EntityJournal getJournal() {
        return journal;
    }

    public void setJournal(EntityJournal journal) {
        this.journal = journal;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
