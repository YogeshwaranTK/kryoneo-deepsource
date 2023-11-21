package com.kjms.service.dto;

import com.kjms.domain.WorkflowStage;

import java.util.List;

public class SubmissionWorkFlowFile {
    private WorkflowStage workflowStage;
    private List<SubmissionFile> submissionFiles;
    private List<SubmissionDiscussion> submissionDiscussions;

    public WorkflowStage getWorkflowStage() {
        return workflowStage;
    }

    public void setWorkflowStage(WorkflowStage workflowStage) {
        this.workflowStage = workflowStage;
    }

    public List<SubmissionFile> getSubmissionFiles() {
        return submissionFiles;
    }

    public void setSubmissionFiles(List<SubmissionFile> submissionFiles) {
        this.submissionFiles = submissionFiles;
    }

    public List<SubmissionDiscussion> getSubmissionDiscussions() {
        return submissionDiscussions;
    }

    public void setSubmissionDiscussions(List<SubmissionDiscussion> submissionDiscussions) {
        this.submissionDiscussions = submissionDiscussions;
    }
}
