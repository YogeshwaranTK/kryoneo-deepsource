package com.kjms.service.dto;

public class SubmissionCategory {
    private Long journalCategoryId;
    private String categoryName;

    public Long getJournalCategoryId() {
        return journalCategoryId;
    }

    public void setJournalCategoryId(Long journalCategoryId) {
        this.journalCategoryId = journalCategoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
