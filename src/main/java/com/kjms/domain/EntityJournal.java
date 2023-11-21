package com.kjms.domain;

import java.time.Instant;
import java.util.List;
import javax.persistence.*;

/**
 * Entity class representing a journals in the journal management system.
 */
@Entity
@Table(name = "jm_journal")
public class EntityJournal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "title", nullable = false ,length = 100)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "thumbnail", length = 100)
    private String thumbnail;

    @Column(name = "summary", columnDefinition = "TEXT")
    private String summary;

    @Column(name = "published")
    private Boolean published;
    @Column(name = "editor_chief", length = 50)
    private String editorChief;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @Column(name = "online_issn", length = 10)
    private String onlineIssn;

    @Column(name = "print_issn", length = 10)
    private String printIssn;

    @Column(name = "published_at")
    private Instant publishedAt;

    @Column(name = "deleted_remarks", columnDefinition = "TEXT")
    private String deletedRemarks;

    @Column(name = "abbreviation", columnDefinition = "TEXT")
    private String abbreviation;

    // TODO: 13-Nov-23 @varghesh (length missing)
    @Column(name = "initials")
    private String initials;

    @Column(name = "guidelines", columnDefinition = "TEXT")
    private String guidelines;

    @Column(name = "copyright_notice", columnDefinition = "TEXT")
    private String copyrightNotice;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", foreignKey = @ForeignKey(name = "fk_jm_journal_created_by"), updatable = false, nullable = false)
    private EntityUser createdBy;

    @Column(name = "created_at", updatable = false, nullable = false)
    private Instant createdAt;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "last_modified_by", foreignKey = @ForeignKey(name = "fk_jm_journal_last_modified_by"))
    private EntityUser lastModifiedBy;

    @Column(name = "last_modified_at")
    private Instant lastModifiedAt;

    @Column(name = "j_key", length = 10, nullable = false)
    private String key;

    @Column(name = "storage_container_name", updatable = false, nullable = false)
    private String storageContainerName;
    // TODO: 13-Nov-23 @varghesh(check and remove this column)
    @Column(name = "completed_step", length = 2)
    private Integer completedStep = 0;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_id", foreignKey = @ForeignKey(name = "k_jm_journal_ref_file_type"))
    private List<EntityJournalFileType> fileTypes;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_id", foreignKey = @ForeignKey(name = "k_jm_journal_ref_categories"))
    private List<EntityJournalCategory> categories;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_id", foreignKey = @ForeignKey(name = "k_jm_journal_ref_languages"))
    private List<EntityJournalLanguage> languages;
    public String getCopyrightNotice() {
        return copyrightNotice;
    }

    public void setCopyrightNotice(String copyrightNotice) {
        this.copyrightNotice = copyrightNotice;
    }

    public Integer getCompletedStep() {
        return completedStep;
    }

    public void setCompletedStep(Integer completedStep) {
        this.completedStep = completedStep;
    }

    public String getStorageContainerName() {
        return storageContainerName;
    }

    public void setStorageContainerName(String storageContainerName) {
        this.storageContainerName = storageContainerName;
    }

    public String getGuidelines() {
        return guidelines;
    }

    public void setGuidelines(String guidelines) {
        this.guidelines = guidelines;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }


    public String getEditorChief() {
        return editorChief;
    }

    public void setEditorChief(String editorChief) {
        this.editorChief = editorChief;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public EntityUser getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(EntityUser lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(Instant lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getDeletedRemarks() {
        return deletedRemarks;
    }

    public void setDeletedRemarks(String deletedRemarks) {
        this.deletedRemarks = deletedRemarks;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public String getOnlineIssn() {
        return onlineIssn;
    }

    public void setOnlineIssn(String onlineIssn) {
        this.onlineIssn = onlineIssn;
    }

    public String getPrintIssn() {
        return printIssn;
    }

    public void setPrintIssn(String printIssn) {
        this.printIssn = printIssn;
    }

    public Instant getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public List<EntityJournalFileType> getFileTypes() {
        return fileTypes;
    }

    public void setFileTypes(List<EntityJournalFileType> fileTypes) {
        this.fileTypes = fileTypes;
    }

    public List<EntityJournalCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<EntityJournalCategory> categories) {
        this.categories = categories;
    }

    public List<EntityJournalLanguage> getLanguages() {
        return languages;
    }

    public void setLanguages(List<EntityJournalLanguage> languages) {
        this.languages = languages;
    }
}
