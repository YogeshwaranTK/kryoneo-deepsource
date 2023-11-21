package com.kjms.service.mapper;

import com.kjms.domain.*;
import com.kjms.service.ReviewRoundReviewerDiscussionMessageFile;
import com.kjms.service.WorkflowFileStorageService;
import com.kjms.service.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewMapper {

    private final CommonMapper commonMapper;

    private final WorkflowFileStorageService fileStorageService;

    public ReviewMapper(CommonMapper commonMapper, WorkflowFileStorageService fileStorageService) {
        this.commonMapper = commonMapper;
        this.fileStorageService = fileStorageService;
    }

    public ReviewRound entityReviewRoundToReviewRound(EntityReviewRound entityReviewRound) {

        ReviewRound reviewRound = new ReviewRound();

        reviewRound.setId(entityReviewRound.getId());

        reviewRound.setRound(entityReviewRound.getRound());

        reviewRound.setName(entityReviewRound.getName());

        return reviewRound;
    }

    public List<ReviewRound> entityReviewRoundsToReviewRounds(List<EntityReviewRound> entityReviewRounds) {

        List<ReviewRound> reviewRounds = new ArrayList<>();

        entityReviewRounds.forEach(entityReviewRound -> reviewRounds.add(entityReviewRoundToReviewRound(entityReviewRound)));

        return reviewRounds;
    }

    public List<ReviewRoundFile> entityReviewRoundFilesToReviewRoundFiles(List<EntityReviewRoundFile> entityReviewRoundFiles) {

        List<ReviewRoundFile> reviewRoundFiles = new ArrayList<>();

        entityReviewRoundFiles.forEach(entityReviewRoundFile -> reviewRoundFiles.add(entityReviewRoundFileToReviewRoundFile(entityReviewRoundFile)));

        return reviewRoundFiles;
    }

    private ReviewRoundFile entityReviewRoundFileToReviewRoundFile(EntityReviewRoundFile entityReviewRoundFile) {

        ReviewRoundFile reviewRoundFile = new ReviewRoundFile();

        reviewRoundFile.setFileType(commonMapper.entityFilesTypeToFileType(entityReviewRoundFile.getJournalFileType().getFileType()));

        reviewRoundFile.setFile(entityReviewRoundFile.getFile());

        reviewRoundFile.setFileEndPoint(fileStorageService.createDownloadLink(entityReviewRoundFile.getFilePath()));

        reviewRoundFile.setCreatedAt(entityReviewRoundFile.getCreatedAt());

        reviewRoundFile.setId(entityReviewRoundFile.getId());

        return reviewRoundFile;
    }

    public ReviewRoundDiscussion mapEntityReviewRoundDiscussionToReviewRoundDiscussion(EntityReviewRoundDiscussion entityReviewRoundDiscussion) {

        ReviewRoundDiscussion reviewRoundDiscussion = new ReviewRoundDiscussion();

        reviewRoundDiscussion.setId(entityReviewRoundDiscussion.getId());

        reviewRoundDiscussion.setStatus(entityReviewRoundDiscussion.getStatus());

        reviewRoundDiscussion.setDescription(entityReviewRoundDiscussion.getDescription());

        reviewRoundDiscussion.setTopic(entityReviewRoundDiscussion.getTopic());

        reviewRoundDiscussion.setCreatedAt(entityReviewRoundDiscussion.getCreatedAt());

        reviewRoundDiscussion.setDiscussionMessages(mapEntityReviewRoundDiscussionMessagesToReviewRoundDiscussionMessages(entityReviewRoundDiscussion.getDiscussionMessages()));

        reviewRoundDiscussion.setDiscussionFiles(entityReviewRoundDiscussionFilesToReviewRoundDiscussionFiles(entityReviewRoundDiscussion.getFiles()));

        return reviewRoundDiscussion;
    }

    private List<ReviewRoundDiscussionFile> entityReviewRoundDiscussionFilesToReviewRoundDiscussionFiles(List<EntityReviewRoundDiscussionFile> entityReviewRoundDiscussionFiles) {

        List<ReviewRoundDiscussionFile> ReviewRoundDiscussionFiles = new ArrayList<>();

        entityReviewRoundDiscussionFiles.forEach(entityReviewRoundDiscussionFile -> {
            ReviewRoundDiscussionFiles.add(entityReviewRoundDiscussionFileToReviewRoundDiscussion(entityReviewRoundDiscussionFile));
        });
        return ReviewRoundDiscussionFiles;
    }

    private ReviewRoundDiscussionFile entityReviewRoundDiscussionFileToReviewRoundDiscussion(EntityReviewRoundDiscussionFile entityReviewRoundDiscussionFile) {

        ReviewRoundDiscussionFile reviewRoundDiscussionFile = new ReviewRoundDiscussionFile();

        reviewRoundDiscussionFile.setId(entityReviewRoundDiscussionFile.getId());

        reviewRoundDiscussionFile.setFilePath(entityReviewRoundDiscussionFile.getFilePath());

        reviewRoundDiscussionFile.setFile(entityReviewRoundDiscussionFile.getFile());

        reviewRoundDiscussionFile.setCreatedAt(entityReviewRoundDiscussionFile.getCreatedAt());

        reviewRoundDiscussionFile.setFileType(commonMapper.entityFilesTypeToFileType(entityReviewRoundDiscussionFile.getJournalFileType().getFileType()));

        reviewRoundDiscussionFile.setFileEndPoint(fileStorageService.createDownloadLink(entityReviewRoundDiscussionFile.getFilePath()));

        return reviewRoundDiscussionFile;
    }

    public List<ReviewRoundDiscussionMessage> mapEntityReviewRoundDiscussionMessagesToReviewRoundDiscussionMessages(List<EntityReviewRoundDiscussionMessage> entityReviewRoundDiscussionMessages) {

        List<ReviewRoundDiscussionMessage> reviewRoundDiscussionMessages = new ArrayList<>();

        entityReviewRoundDiscussionMessages.forEach(entityReviewRoundDiscussionFile -> {

            reviewRoundDiscussionMessages.add(mapEntityReviewRoundDiscussionMessageToReviewRoundDiscussionMessage(entityReviewRoundDiscussionFile));
        });

        return reviewRoundDiscussionMessages;
    }

    public ReviewRoundDiscussionMessage mapEntityReviewRoundDiscussionMessageToReviewRoundDiscussionMessage(EntityReviewRoundDiscussionMessage entityReviewRoundDiscussionFile) {

        ReviewRoundDiscussionMessage reviewRoundDiscussionMessage = new ReviewRoundDiscussionMessage();

        reviewRoundDiscussionMessage.setId(entityReviewRoundDiscussionFile.getId());

        reviewRoundDiscussionMessage.setMessage(entityReviewRoundDiscussionFile.getMessage());

        reviewRoundDiscussionMessage.setUserFullName(entityReviewRoundDiscussionFile.getUser().getFullName());

        reviewRoundDiscussionMessage.setDiscussionId(entityReviewRoundDiscussionFile.getDiscussion().getId());

        reviewRoundDiscussionMessage.setCreatedAt(entityReviewRoundDiscussionFile.getCreatedAt());

        reviewRoundDiscussionMessage.setReviewRoundDiscussionMessageFiles(entityReviewRoundDiscussionMessageFilesToReviewRoundDiscussionMessageFiles(entityReviewRoundDiscussionFile.getFiles()));

        return reviewRoundDiscussionMessage;
    }

    private List<ReviewRoundDiscussionMessageFile> entityReviewRoundDiscussionMessageFilesToReviewRoundDiscussionMessageFiles(List<EntityReviewRoundDiscussionMessageFile> entityReviewRoundDiscussionMessageFiles) {

        List<ReviewRoundDiscussionMessageFile> reviewRoundDiscussionMessageFiles = new ArrayList<>();

        entityReviewRoundDiscussionMessageFiles.forEach(entityReviewRoundDiscussionMessageFile -> {
            reviewRoundDiscussionMessageFiles.add(entityReviewRoundDiscussionMessageFileToReviewRoundDiscussionMessageFile(entityReviewRoundDiscussionMessageFile));
        });
        return reviewRoundDiscussionMessageFiles;
    }

    private ReviewRoundDiscussionMessageFile entityReviewRoundDiscussionMessageFileToReviewRoundDiscussionMessageFile(EntityReviewRoundDiscussionMessageFile entityReviewRoundDiscussionMessageFile) {

        ReviewRoundDiscussionMessageFile reviewRoundDiscussionMessageFile = new ReviewRoundDiscussionMessageFile();

        reviewRoundDiscussionMessageFile.setId(entityReviewRoundDiscussionMessageFile.getId());

        reviewRoundDiscussionMessageFile.setCreatedAt(entityReviewRoundDiscussionMessageFile.getCreatedAt());

        reviewRoundDiscussionMessageFile.setFile(entityReviewRoundDiscussionMessageFile.getFile());

        reviewRoundDiscussionMessageFile.setFileType(commonMapper.entityFilesTypeToFileType(entityReviewRoundDiscussionMessageFile.getJournalFileType().getFileType()));

        reviewRoundDiscussionMessageFile.setFileEndPoint(fileStorageService.createDownloadLink(entityReviewRoundDiscussionMessageFile.getFilePath()));

        return reviewRoundDiscussionMessageFile;
    }

    public List<ReviewRoundDiscussion> entityReviewRoundDiscussionsToReviewRoundDiscussions(List<EntityReviewRoundDiscussion> discussions) {

        List<ReviewRoundDiscussion> reviewRoundDiscussions = new ArrayList<>();

        discussions.forEach(discussion -> reviewRoundDiscussions.add(mapEntityReviewRoundDiscussionToReviewRoundDiscussion(discussion)));

        return reviewRoundDiscussions;
    }

    public List<ReviewRoundContributor> entityReviewRoundContributorsToReviewRoundContributors(List<EntityReviewRoundContributor> entityReviewRoundContributors) {

        List<ReviewRoundContributor> reviewRoundContributors = new ArrayList<>();

        entityReviewRoundContributors.forEach(entityReviewRoundContributor -> reviewRoundContributors.add(entityReviewRoundContributorToReviewRoundContributor(entityReviewRoundContributor)));

        return reviewRoundContributors;
    }

    public ReviewRoundContributor entityReviewRoundContributorToReviewRoundContributor(EntityReviewRoundContributor entityReviewRoundContributor) {

        ReviewRoundContributor reviewRoundContributor = new ReviewRoundContributor();

        reviewRoundContributor.setId(entityReviewRoundContributor.getUser().getId());

        reviewRoundContributor.setFullName(entityReviewRoundContributor.getUser().getFullName());

        return reviewRoundContributor;
    }

    public ReviewRoundReviewerReview mapEntityReviewRoundReviewerToReviewRoundReviewer(EntityReviewRoundReview entityReviewRoundReview) {

        ReviewRoundReviewerReview reviewRoundReviewerReview = new ReviewRoundReviewerReview();

        reviewRoundReviewerReview.setReviewDueDate(entityReviewRoundReview.getReviewDueDate());

        reviewRoundReviewerReview.setReviewRoundId(entityReviewRoundReview.getReviewRound().getId());

        reviewRoundReviewerReview.setReviewerReviewType(entityReviewRoundReview.getReviewerReviewType());

        reviewRoundReviewerReview.setResponseDueDate(entityReviewRoundReview.getResponseDueDate());

        reviewRoundReviewerReview.setId(entityReviewRoundReview.getId());

        reviewRoundReviewerReview.setFullName(entityReviewRoundReview.getJournalReviewer().getUser().getFullName());

        reviewRoundReviewerReview.setReviewerRecommendation(entityReviewRoundReview.getReviewerRecommendation());

        reviewRoundReviewerReview.setEditorComment(entityReviewRoundReview.getEditorComment());

        reviewRoundReviewerReview.setEditorAndAuthorComment(entityReviewRoundReview.getEditorAndAuthorComment());

        reviewRoundReviewerReview.setReviewStatus(entityReviewRoundReview.getReviewStatus());

        reviewRoundReviewerReview.setReviewerFiles(mapEntityReviewerFilesToReviewerFiles(entityReviewRoundReview.getReviewerFiles()));

        reviewRoundReviewerReview.setReviewCompletedAt(entityReviewRoundReview.getReviewCompletedAt());

        reviewRoundReviewerReview.setRating(entityReviewRoundReview.getRating());

        reviewRoundReviewerReview.setSubmissionId(entityReviewRoundReview.getReviewRound().getSubmission().getId());

        reviewRoundReviewerReview.setSubmissionTitle(entityReviewRoundReview.getReviewRound().getSubmission().getTitle());

        reviewRoundReviewerReview.setSubmissionAbstract(entityReviewRoundReview.getReviewRound().getSubmission().getDescription());

        reviewRoundReviewerReview.setReviewerDiscussions(entityReviewRoundReviewerDiscussionsToReviewRoundReviewerDiscussions(entityReviewRoundReview.getReviewRoundReviewerDiscussions()));

        return reviewRoundReviewerReview;
    }

    public ReviewRoundReviewerReview mapEntityReviewRoundReviewerToReviewRoundReviewerForReviewer(EntityReviewRoundReview entityReviewRoundReview) {

        ReviewRoundReviewerReview reviewRoundReviewerReview = new ReviewRoundReviewerReview();

        reviewRoundReviewerReview.setReviewDueDate(entityReviewRoundReview.getReviewDueDate());

        reviewRoundReviewerReview.setReviewRoundId(entityReviewRoundReview.getReviewRound().getId());

        reviewRoundReviewerReview.setReviewerReviewType(entityReviewRoundReview.getReviewerReviewType());

        reviewRoundReviewerReview.setResponseDueDate(entityReviewRoundReview.getResponseDueDate());

        reviewRoundReviewerReview.setId(entityReviewRoundReview.getId());

        reviewRoundReviewerReview.setFullName(entityReviewRoundReview.getJournalReviewer().getUser().getFullName());

        reviewRoundReviewerReview.setReviewerRecommendation(entityReviewRoundReview.getReviewerRecommendation());

        reviewRoundReviewerReview.setEditorComment(entityReviewRoundReview.getEditorComment());

        reviewRoundReviewerReview.setEditorAndAuthorComment(entityReviewRoundReview.getEditorAndAuthorComment());

        reviewRoundReviewerReview.setReviewStatus(entityReviewRoundReview.getReviewStatus());

        reviewRoundReviewerReview.setReviewerFiles(mapEntityReviewerFilesToReviewerFiles(entityReviewRoundReview.getReviewerFiles()));

        reviewRoundReviewerReview.setReviewCompletedAt(entityReviewRoundReview.getReviewCompletedAt());

        return reviewRoundReviewerReview;


    }

    public List<ReviewRoundReviewerReview> mapEntityReviewRoundReviewersToReviewRoundReviewers(List<EntityReviewRoundReview> entityReviewRoundReviews) {

        List<ReviewRoundReviewerReview> reviewRoundReviewerReviews = new ArrayList<>();

        entityReviewRoundReviews.forEach(entityReviewRoundReviewer -> reviewRoundReviewerReviews.add(mapEntityReviewRoundReviewerToReviewRoundReviewer(entityReviewRoundReviewer)));

        return reviewRoundReviewerReviews;
    }

    public List<ReviewerFile> mapEntityReviewerFilesToReviewerFiles(List<EntityReviewerFile> entityReviewerFiles) {

        List<ReviewerFile> reviewerFiles = new ArrayList<>();

        entityReviewerFiles.forEach(entityReviewRoundFile -> reviewerFiles.add(mapEntityReviewerFileToReviewerFile(entityReviewRoundFile)));

        return reviewerFiles;
    }

    private ReviewerFile mapEntityReviewerFileToReviewerFile(EntityReviewerFile entityReviewerFile) {

        ReviewerFile reviewerFile = new ReviewerFile();

        reviewerFile.setFileType(commonMapper.entityFilesTypeToFileType(entityReviewerFile.getJournalFileType().getFileType()));

        reviewerFile.setFile(entityReviewerFile.getFile());

        reviewerFile.setFileEndPoint(fileStorageService.createDownloadLink(entityReviewerFile.getFilePath()));

        reviewerFile.setCreatedAt(entityReviewerFile.getCreatedAt());

        reviewerFile.setId(entityReviewerFile.getId());

        return reviewerFile;
    }

    public List<ReviewRoundReviewerFile> mapEntityReviewRoundReviewerFilesToReviewRoundReviewerFiles(List<EntityReviewRoundReviewerFile> entityReviewRoundReviewerFiles) {

        List<ReviewRoundReviewerFile> reviewRoundReviewerFiles = new ArrayList<>();

        entityReviewRoundReviewerFiles.forEach(entityReviewRoundReviewerFile -> reviewRoundReviewerFiles.add(mapEntityReviewRoundReviewerFileToReviewRoundReviewerFile(entityReviewRoundReviewerFile)));

        return reviewRoundReviewerFiles;
    }

    private ReviewRoundReviewerFile mapEntityReviewRoundReviewerFileToReviewRoundReviewerFile(EntityReviewRoundReviewerFile entityReviewRoundReviewerFile) {

        ReviewRoundReviewerFile reviewRoundReviewerFile = new ReviewRoundReviewerFile();

        reviewRoundReviewerFile.setId(entityReviewRoundReviewerFile.getId());

        reviewRoundReviewerFile.setCreatedAt(entityReviewRoundReviewerFile.getCreatedAt());

        reviewRoundReviewerFile.setFile(entityReviewRoundReviewerFile.getReviewRoundFile().getFile());

        reviewRoundReviewerFile.setFileType(commonMapper.entityFilesTypeToFileType(entityReviewRoundReviewerFile.getReviewRoundFile().getJournalFileType().getFileType()));

        reviewRoundReviewerFile.setFileEndPoint(fileStorageService.createDownloadLink(entityReviewRoundReviewerFile.getReviewRoundFile().getFilePath()));

        return reviewRoundReviewerFile;
    }

    public Page<ReviewRoundReviewerReview> mapEntityReviewRoundReviewersToReviewRoundReviewers(Page<EntityReviewRoundReview> entityReviewRoundReviewers) {
        return entityReviewRoundReviewers.map(this::mapEntityReviewRoundReviewerToReviewRoundReviewer);
    }

    private ReviewerReplyFile mapEntityReviewerReplyFileToReviewerReplyFile(EntityReviewerReplyFile entityReviewerFile) {

        ReviewerReplyFile reviewerFile = new ReviewerReplyFile();

        reviewerFile.setFile(entityReviewerFile.getFile());

        reviewerFile.setFileEndPoint(fileStorageService.createDownloadLink(entityReviewerFile.getFilePath()));

        reviewerFile.setCreatedAt(entityReviewerFile.getCreatedAt());

        reviewerFile.setId(entityReviewerFile.getId());

        return reviewerFile;
    }

    public List<ReviewerReplyFile> mapEntityReviewerReplyFilesToReviewerReplyFiles(List<EntityReviewerReplyFile> entityReviewerFiles) {

        List<ReviewerReplyFile> reviewerReplyFiles = new ArrayList<>();

        entityReviewerFiles.forEach(entityReviewerFile -> reviewerReplyFiles.add(mapEntityReviewerReplyFileToReviewerReplyFile(entityReviewerFile)));

        return reviewerReplyFiles;
    }

    public ReviewRoundReviewerDiscussion mapEntityReviewRoundReviewerDiscussionToReviewRoundReviewerDiscussion(EntityReviewRoundReviewerDiscussion entityReviewRoundDiscussion) {

        ReviewRoundReviewerDiscussion reviewRoundReviewerDiscussion = new ReviewRoundReviewerDiscussion();

        reviewRoundReviewerDiscussion.setId(entityReviewRoundDiscussion.getId());

        reviewRoundReviewerDiscussion.setStatus(entityReviewRoundDiscussion.getStatus());

        reviewRoundReviewerDiscussion.setDescription(entityReviewRoundDiscussion.getDescription());

        reviewRoundReviewerDiscussion.setTopic(entityReviewRoundDiscussion.getTopic());

        reviewRoundReviewerDiscussion.setCreatedAt(entityReviewRoundDiscussion.getCreatedAt());

        reviewRoundReviewerDiscussion.setDiscussionMessages(mapEntityReviewRoundReviewerDiscussionMessagesToReviewRoundReviewerDiscussionMessages(entityReviewRoundDiscussion.getDiscussionMessages()));

        reviewRoundReviewerDiscussion.setDiscussionFiles(entityReviewRoundReviewerDiscussionFilesToReviewRoundReviewerDiscussionFiles(entityReviewRoundDiscussion.getFiles()));

        return reviewRoundReviewerDiscussion;
    }

    private List<ReviewRoundReviewerDiscussionFile> entityReviewRoundReviewerDiscussionFilesToReviewRoundReviewerDiscussionFiles(List<EntityReviewRoundReviewerDiscussionFile> entityReviewRoundReviewerDiscussionFiles) {

        List<ReviewRoundReviewerDiscussionFile> ReviewRoundDiscussionFiles = new ArrayList<>();

        entityReviewRoundReviewerDiscussionFiles.forEach(entityReviewRoundDiscussionFile -> ReviewRoundDiscussionFiles.add(entityReviewRoundReviewerDiscussionFileToReviewRoundReviewerDiscussion(entityReviewRoundDiscussionFile)));

        return ReviewRoundDiscussionFiles;
    }

    private ReviewRoundReviewerDiscussionFile entityReviewRoundReviewerDiscussionFileToReviewRoundReviewerDiscussion(EntityReviewRoundReviewerDiscussionFile entityReviewRoundDiscussionFile) {

        ReviewRoundReviewerDiscussionFile reviewRoundDiscussionFile = new ReviewRoundReviewerDiscussionFile();

        reviewRoundDiscussionFile.setId(entityReviewRoundDiscussionFile.getId());

        reviewRoundDiscussionFile.setFilePath(entityReviewRoundDiscussionFile.getFilePath());

        reviewRoundDiscussionFile.setFile(entityReviewRoundDiscussionFile.getFile());

        reviewRoundDiscussionFile.setCreatedAt(entityReviewRoundDiscussionFile.getCreatedAt());

        reviewRoundDiscussionFile.setFileType(commonMapper.entityFilesTypeToFileType(entityReviewRoundDiscussionFile.getJournalFileType().getFileType()));

        reviewRoundDiscussionFile.setFileEndPoint(fileStorageService.createDownloadLink(entityReviewRoundDiscussionFile.getFilePath()));

        return reviewRoundDiscussionFile;
    }

    public List<ReviewRoundReviewerDiscussionMessage> mapEntityReviewRoundReviewerDiscussionMessagesToReviewRoundReviewerDiscussionMessages(List<EntityReviewRoundReviewerDiscussionMessage> entityReviewRoundReviewerDiscussionMessages) {

        List<ReviewRoundReviewerDiscussionMessage> reviewRoundDiscussionMessages = new ArrayList<>();

        entityReviewRoundReviewerDiscussionMessages.forEach(entityReviewRoundDiscussionFile -> {

            reviewRoundDiscussionMessages.add(mapEntityReviewRoundReviewerDiscussionMessageToReviewRoundReviewerDiscussionMessage(entityReviewRoundDiscussionFile));
        });

        return reviewRoundDiscussionMessages;
    }

    private List<ReviewRoundReviewerDiscussionMessageFile> entityReviewRoundReviewerDiscussionMessageFilesToReviewRoundReviewerDiscussionMessageFiles(List<EntityReviewRoundReviewerDiscussionMessageFile> entityReviewRoundReviewerDiscussionMessageFiles) {

        List<ReviewRoundReviewerDiscussionMessageFile> reviewRoundDiscussionMessageFiles = new ArrayList<>();

        entityReviewRoundReviewerDiscussionMessageFiles.forEach(entityReviewRoundReviewerDiscussionMessageFile -> reviewRoundDiscussionMessageFiles.add(entityReviewRoundReviewerDiscussionMessageFileToReviewRoundReviewerDiscussionMessageFile(entityReviewRoundReviewerDiscussionMessageFile)));

        return reviewRoundDiscussionMessageFiles;
    }

    private ReviewRoundReviewerDiscussionMessageFile entityReviewRoundReviewerDiscussionMessageFileToReviewRoundReviewerDiscussionMessageFile(EntityReviewRoundReviewerDiscussionMessageFile entityReviewRoundReviewerDiscussionMessageFile) {

        ReviewRoundReviewerDiscussionMessageFile reviewRoundReviewerDiscussionMessageFile = new ReviewRoundReviewerDiscussionMessageFile();

        reviewRoundReviewerDiscussionMessageFile.setId(entityReviewRoundReviewerDiscussionMessageFile.getId());

        reviewRoundReviewerDiscussionMessageFile.setCreatedAt(entityReviewRoundReviewerDiscussionMessageFile.getCreatedAt());

        reviewRoundReviewerDiscussionMessageFile.setFile(entityReviewRoundReviewerDiscussionMessageFile.getFile());

        reviewRoundReviewerDiscussionMessageFile.setFileType(commonMapper.entityFilesTypeToFileType(entityReviewRoundReviewerDiscussionMessageFile.getJournalFileType().getFileType()));

        reviewRoundReviewerDiscussionMessageFile.setFileEndPoint(fileStorageService.createDownloadLink(entityReviewRoundReviewerDiscussionMessageFile.getFilePath()));

        return reviewRoundReviewerDiscussionMessageFile;
    }

    public ReviewRoundReviewerDiscussionMessage mapEntityReviewRoundReviewerDiscussionMessageToReviewRoundReviewerDiscussionMessage(EntityReviewRoundReviewerDiscussionMessage entityReviewRoundReviewerDiscussionMessage) {

        ReviewRoundReviewerDiscussionMessage reviewRoundDiscussionMessage = new ReviewRoundReviewerDiscussionMessage();

        reviewRoundDiscussionMessage.setId(entityReviewRoundReviewerDiscussionMessage.getId());

        reviewRoundDiscussionMessage.setMessage(entityReviewRoundReviewerDiscussionMessage.getMessage());

        reviewRoundDiscussionMessage.setUserFullName(entityReviewRoundReviewerDiscussionMessage.getUser().getFullName());

        reviewRoundDiscussionMessage.setDiscussionId(entityReviewRoundReviewerDiscussionMessage.getDiscussion().getId());

        reviewRoundDiscussionMessage.setCreatedAt(entityReviewRoundReviewerDiscussionMessage.getCreatedAt());

        reviewRoundDiscussionMessage.setDiscussionMessageFiles(entityReviewRoundReviewerDiscussionMessageFilesToReviewRoundReviewerDiscussionMessageFiles(entityReviewRoundReviewerDiscussionMessage.getFiles()));

        return reviewRoundDiscussionMessage;
    }

    public List<ReviewRoundReviewerDiscussion> entityReviewRoundReviewerDiscussionsToReviewRoundReviewerDiscussions(List<EntityReviewRoundReviewerDiscussion> discussions) {

        List<ReviewRoundReviewerDiscussion> reviewRoundDiscussions = new ArrayList<>();

        discussions.forEach(discussion -> reviewRoundDiscussions.add(mapEntityReviewRoundReviewerDiscussionToReviewRoundReviewerDiscussion(discussion)));

        return reviewRoundDiscussions;
    }
}

