package com.kjms.service.dto;

import java.util.List;

public class CopyEditing {

    List<CopyEditingContributor> copyEditingContributors;
    List<CopyEditingDiscussion> copyEditingDiscussions;

    List<CopyEditingDraftFile> copyEditingDraftFiles;

    List<CopyEditedFile> copyEditedFiles;

    public List<CopyEditingContributor> getCopyEditingContributors() {
        return copyEditingContributors;
    }

    public void setCopyEditingContributors(List<CopyEditingContributor> copyEditingContributors) {
        this.copyEditingContributors = copyEditingContributors;
    }

    public List<CopyEditingDiscussion> getCopyEditingDiscussions() {
        return copyEditingDiscussions;
    }

    public void setCopyEditingDiscussions(List<CopyEditingDiscussion> copyEditingDiscussions) {
        this.copyEditingDiscussions = copyEditingDiscussions;
    }

    public List<CopyEditingDraftFile> getCopyEditingDraftFiles() {
        return copyEditingDraftFiles;
    }

    public void setCopyEditingDraftFiles(List<CopyEditingDraftFile> copyEditingDraftFiles) {
        this.copyEditingDraftFiles = copyEditingDraftFiles;
    }

    public List<CopyEditedFile> getCopyEditedFiles() {
        return copyEditedFiles;
    }

    public void setCopyEditedFiles(List<CopyEditedFile> copyEditedFiles) {
        this.copyEditedFiles = copyEditedFiles;
    }
}
