package com.kjms.service.dto;

import com.kjms.domain.EntityJournal;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

/**
 * A DTO for the {@link EntityJournal} entity
 */
public class Journal implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;

    private String title;

    private String description;

    private String thumbnail;

    private Boolean published;

    private String onlineIssn;

    private String printIssn;
    private String summary;

    private Instant publishedDateTime;

    private String abbreviation;

    private String initials;
    private String guidelines;
    private String key;

    private String createdByName;

    private Instant createdDate;

    private String lastModifiedByName;

//    private List<JournalFileFormat> fileFormats;
    private List<JournalLanguage> languages;
    private List<JournalCategory> categories;
    private Integer completedStep;
    private String editorChief;
    private String copyrightNotice;

    public String getCopyrightNotice() {
        return copyrightNotice;
    }

    public void setCopyrightNotice(String copyrightNotice) {
        this.copyrightNotice = copyrightNotice;
    }

    private List<JournalFileType> fileTypes;

    public List<JournalFileType> getFileTypes() {
        return fileTypes;
    }

    public void setFileTypes(List<JournalFileType> fileTypes) {
        this.fileTypes = fileTypes;
    }

    public Integer getCompletedStep() {
        return completedStep;
    }

    public void setCompletedStep(Integer completedStep) {
        this.completedStep = completedStep;
    }

    public String getEditorChief() {
        return editorChief;
    }

    public void setEditorChief(String editorChief) {
        this.editorChief = editorChief;
    }

    public List<JournalLanguage> getLanguages() {
        return languages;
    }

    public void setLanguages(List<JournalLanguage> languages) {
        this.languages = languages;
    }

    public List<JournalCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<JournalCategory> categories) {
        this.categories = categories;
    }

    private Instant lastModifiedDate;

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

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedByName() {
        return lastModifiedByName;
    }

    public void setLastModifiedByName(String lastModifiedByName) {
        this.lastModifiedByName = lastModifiedByName;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
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

    public Instant getPublishedDateTime() {
        return publishedDateTime;
    }

    public void setPublishedDateTime(Instant publishedDateTime) {
        this.publishedDateTime = publishedDateTime;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
