package com.kjms.service.dto;

import java.util.List;

public class JournalRole {

    private final boolean isAuthor;

    private final boolean isReviewer;

    private final boolean isEditorialUser;

    private final boolean isSubmissionEnabled;

    private final boolean isReviewEnabled;

    private final boolean isCopyEditingEnabled;

    private final boolean isProductionEnabled;

    private final List<String> submissionAccess;

    private final List<String> reviewAccess;

    private final List<String> copyEditingAccess;

    private final List<String> productionAccess;

    public JournalRole(boolean isAuthor, boolean isReviewer, boolean isEditorialUser, boolean isSubmissionEnabled, boolean isReviewEnabled, boolean isCopyEditingEnabled, boolean isProductionEnabled, List<String> submissionAccess, List<String> reviewAccess, List<String> copyEditingAccess, List<String> productionAccess) {
        this.isAuthor = isAuthor;
        this.isReviewer = isReviewer;
        this.isEditorialUser = isEditorialUser;
        this.isSubmissionEnabled = isSubmissionEnabled;
        this.isReviewEnabled = isReviewEnabled;
        this.isCopyEditingEnabled = isCopyEditingEnabled;
        this.isProductionEnabled = isProductionEnabled;
        this.submissionAccess = submissionAccess;
        this.reviewAccess = reviewAccess;
        this.copyEditingAccess = copyEditingAccess;
        this.productionAccess = productionAccess;
    }

    public boolean isAuthor() {
        return isAuthor;
    }

    public boolean isReviewer() {
        return isReviewer;
    }

    public boolean isEditorialUser() {
        return isEditorialUser;
    }

    public boolean isSubmissionEnabled() {
        return isSubmissionEnabled;
    }

    public boolean isReviewEnabled() {
        return isReviewEnabled;
    }

    public boolean isCopyEditingEnabled() {
        return isCopyEditingEnabled;
    }

    public boolean isProductionEnabled() {
        return isProductionEnabled;
    }

    public List<String> getSubmissionAccess() {
        return submissionAccess;
    }

    public List<String> getReviewAccess() {
        return reviewAccess;
    }

    public List<String> getCopyEditingAccess() {
        return copyEditingAccess;
    }

    public List<String> getProductionAccess() {
        return productionAccess;
    }
}
