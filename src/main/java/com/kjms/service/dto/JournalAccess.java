package com.kjms.service.dto;

public class JournalAccess {

    private final boolean isEditorialUser;
    private final boolean isAuthor;
    private final boolean isReviewer;
    private final EditorialUserRole editorialUserRole;

    public JournalAccess(boolean isEditorialUser, boolean isAuthor, boolean isReviewer, EditorialUserRole editorialUserRole) {

        this.isEditorialUser = isEditorialUser;
        this.isAuthor = isAuthor;
        this.isReviewer = isReviewer;
        this.editorialUserRole = editorialUserRole;
    }

    public boolean isEditorialUser() {
        return isEditorialUser;
    }

    public boolean isAuthor() {
        return isAuthor;
    }

    public boolean isReviewer() {
        return isReviewer;
    }


}
