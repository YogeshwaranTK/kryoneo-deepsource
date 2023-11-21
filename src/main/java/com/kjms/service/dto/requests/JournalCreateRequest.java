package com.kjms.service.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * Represents a request object for creating a new Journal
 */
@Schema(description = "Request Body for Create Journal", title = "Journal Create")
public class JournalCreateRequest {


    private String title;
    private String key;
    private String description;
    private String onlineIssn;
    private String printIssn;
    private String editorChief;
    private List<String> articleSubmissionLanguages;
    private String guidelines;
    private List<String> categories;
    private List<Long> fileTypes;
    private String summary;
    public String getEditorChief() {
        return editorChief;
    }

    public void setEditorChief(String editorChief) {
        this.editorChief = editorChief;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getArticleSubmissionLanguages() {
        return articleSubmissionLanguages;
    }

    public void setArticleSubmissionLanguages(List<String> articleSubmissionLanguages) {
        this.articleSubmissionLanguages = articleSubmissionLanguages;
    }

    public String getGuidelines() {
        return guidelines;
    }

    public void setGuidelines(String guidelines) {
        this.guidelines = guidelines;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<Long> getFileTypes() {
        return fileTypes;
    }

    public void setFileTypes(List<Long> fileTypes) {
        this.fileTypes = fileTypes;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
