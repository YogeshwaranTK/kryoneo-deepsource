package com.kjms.service.dto;

import com.kjms.domain.ReviewActionType;
import com.kjms.domain.ReviewStatus;
import com.kjms.domain.SubmissionStatus;
import com.kjms.domain.WorkflowStage;

import java.util.List;

public class ArticlesReport {

    private String title;

    private SubmissionStatus status;

    private String language;

    private List<SubmissionArticleAuthor> submissionArticleAuthors;

    private String submissionAcceptBy;

    private ReviewStatus reviewStatus;

    private ReviewActionType reviewActionType;

    private WorkflowStage workflowStage;


}
