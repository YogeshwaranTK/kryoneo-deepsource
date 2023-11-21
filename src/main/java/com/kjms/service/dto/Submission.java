package com.kjms.service.dto;

import com.kjms.domain.EntitySubmission;
import com.kjms.domain.SubmissionStatus;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

/**
 * A DTO for the {@link EntitySubmission} entity
 */
public class Submission implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String prefix;
    private String title;
    private String subTitle;
    private String description;
    private Instant createdDate;
    private String createdBy;
    private Instant lastModifiedDate;
    private JournalLanguage submissionLanguage;
    private SubmissionStatus status;
    private List<SubmissionCategory> categories;
    private List<SubmissionFile> submissionFiles;
    private List<SubmissionArticleAuthor> authors;
    private Boolean isOwnSubmission;
    private List<String> keywords;
    private String journalTitle;
    private String allFilePath;
    private Boolean agree;

    List<SubmissionContributor> submissionContributors;

    List<SubmissionDiscussion> submissionDiscussions;

    public Boolean getAgree() {
        return agree;
    }

    public void setAgree(Boolean agree) {
        this.agree = agree;
    }

    public String getAllFilePath() {
        return allFilePath;
    }

    public void setAllFilePath(String allFilePath) {
        this.allFilePath = allFilePath;
    }

    public String getJournalTitle() {
        return journalTitle;
    }

    public void setJournalTitle(String journalTitle) {
        this.journalTitle = journalTitle;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public Boolean getOwnSubmission() {
        return isOwnSubmission;
    }

    public void setOwnSubmission(Boolean ownSubmission) {
        isOwnSubmission = ownSubmission;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public SubmissionStatus getStatus() {
        return status;
    }

    public void setStatus(SubmissionStatus status) {
        this.status = status;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public List<SubmissionFile> getSubmissionFiles() {
        return submissionFiles;
    }

    public void setSubmissionFiles(List<SubmissionFile> submissionFiles) {
        this.submissionFiles = submissionFiles;
    }

    public List<SubmissionArticleAuthor> getAuthors() {
        return authors;
    }

    public void setAuthors(List<SubmissionArticleAuthor> authors) {
        this.authors = authors;
    }

    public List<SubmissionCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<SubmissionCategory> categories) {
        this.categories = categories;
    }

    public Submission() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public JournalLanguage getSubmissionLanguage() {
        return submissionLanguage;
    }

    public void setSubmissionLanguage(JournalLanguage submissionLanguage) {
        this.submissionLanguage = submissionLanguage;
    }

    public List<SubmissionContributor> getSubmissionContributors() {
        return submissionContributors;
    }

    public void setSubmissionContributors(List<SubmissionContributor> submissionContributors) {
        this.submissionContributors = submissionContributors;
    }

    public List<SubmissionDiscussion> getSubmissionDiscussions() {
        return submissionDiscussions;
    }

    public void setSubmissionDiscussions(List<SubmissionDiscussion> submissionDiscussions) {
        this.submissionDiscussions = submissionDiscussions;
    }
}
