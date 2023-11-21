package com.kjms.service.mapper;

import com.kjms.domain.*;
import com.kjms.service.WorkflowFileStorageService;
import com.kjms.service.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper class responsible for converting {@link EntitySubmission} to {@link Submission}
 */
@Service
public class SubmissionMapper {

    private final WorkflowFileStorageService fileStorageService;
    private final CommonMapper commonMapper;
    private final JournalLanguageMapper journalLanguageMapper;

    public SubmissionMapper(WorkflowFileStorageService fileStorageService, CommonMapper commonMapper, JournalLanguageMapper journalLanguageMapper) {

        this.fileStorageService = fileStorageService;

        this.commonMapper = commonMapper;
        this.journalLanguageMapper = journalLanguageMapper;
    }

    public Submission entitySubmissionToSubmission(EntitySubmission entitySubmission) {

        Submission submission = new Submission();

        submission.setId(entitySubmission.getId());

        submission.setPrefix(entitySubmission.getLastUpdatedPrefix());

        submission.setDescription(entitySubmission.getLastUpdatedDesc());

        submission.setTitle(entitySubmission.getLastUpdatedTitle());

        submission.setSubTitle(entitySubmission.getLastUpdatedSubTitle());

        submission.setCreatedDate(entitySubmission.getCreatedAt());

        submission.setLastModifiedDate(entitySubmission.getLastModifiedAt());

        submission.setStatus(entitySubmission.getStatus());

        submission.setAgree(entitySubmission.getAgree());

        submission.setCreatedBy(entitySubmission.getCreatedBy().getFullName());

        if (entitySubmission.getJournalLanguage() != null) {

            submission.setSubmissionLanguage(journalLanguageMapper.entityJournalLanguageToJournalLanguage(entitySubmission.getJournalLanguage()));
        }

        return submission;
    }

    public Page<Submission> entitySubmissionToSubmission(Page<EntitySubmission> entitySubmissionArticles) {
        return entitySubmissionArticles.map(this::entitySubmissionToSubmission);
    }


    private String entitySubmissionArticleCategoryToName(EntitySubmissionCategory entitySubmissionCategory) {

        return (entitySubmissionCategory.getJournalCategory() != null) ? entitySubmissionCategory.getJournalCategory().getCategoryId().getName() : null;
    }

    public List<String> getCategoryName(List<EntitySubmissionCategory> entitySubmissionArticleCategories) {

        return entitySubmissionArticleCategories.stream().map(this::entitySubmissionArticleCategoryToName)
            .filter(StringUtils::hasText).collect(Collectors.toList());
    }

    public List<SubmissionCategory> entitySubmissionCategoryToSubmissionCategory(List<EntitySubmissionCategory> submissionCategories) {

        return submissionCategories.stream()
            .map(entitySubmissionArticleCategory -> {

                SubmissionCategory submissionCategory = new SubmissionCategory();

                submissionCategory.setCategoryName(entitySubmissionArticleCategory.getJournalCategory().getCategoryId().getName());
                submissionCategory.setJournalCategoryId(entitySubmissionArticleCategory.getJournalCategory().getId());

                return submissionCategory;

            }).collect(Collectors.toList());
    }

    public Page<SubmissionCategory> entitySubmissionCategoryToSubmissionCategory(Page<EntityJournalCategory> journalCategories) {

        return journalCategories
            .map(entityJournalCategory -> {

                SubmissionCategory submissionCategory = new SubmissionCategory();

                submissionCategory.setCategoryName(entityJournalCategory.getCategoryId().getName());
                submissionCategory.setJournalCategoryId(entityJournalCategory.getId());

                return submissionCategory;

            });
    }


    private SubmissionDiscussionMessageFile entitySubmissionDiscussionMessageFileToSubmissionDiscussionMessageFile(EntitySubmissionDiscussionMessageFile entitySubmissionDiscussionMessageFile, Long journalId) {

        SubmissionDiscussionMessageFile submissionDiscussionMessageFile = new SubmissionDiscussionMessageFile();

        submissionDiscussionMessageFile.setId(entitySubmissionDiscussionMessageFile.getId());

        submissionDiscussionMessageFile.setCreatedAt(entitySubmissionDiscussionMessageFile.getCreatedAt());

        submissionDiscussionMessageFile.setFile(entitySubmissionDiscussionMessageFile.getFile());

        submissionDiscussionMessageFile.setFileType(commonMapper.entityFilesTypeToFileType(entitySubmissionDiscussionMessageFile.getJournalFileType().getFileType()));

        submissionDiscussionMessageFile.setFileEndPoint(fileStorageService.createDownloadLink(entitySubmissionDiscussionMessageFile.getFilePath()));

        return submissionDiscussionMessageFile;
    }

    private List<SubmissionDiscussionMessageFile> entityDiscussionMessageFilesToDiscussionMessageFiles(List<EntitySubmissionDiscussionMessageFile> entitySubmissionDiscussionMessageFiles, Long journalId) {

        List<SubmissionDiscussionMessageFile> submissionDiscussionMessageFiles = new ArrayList<>();

        entitySubmissionDiscussionMessageFiles.forEach(entitySubmissionDiscussionMessageFile -> submissionDiscussionMessageFiles.add(entitySubmissionDiscussionMessageFileToSubmissionDiscussionMessageFile(entitySubmissionDiscussionMessageFile, journalId)));

        return submissionDiscussionMessageFiles;
    }

    public SubmissionDiscussionMessage mapEntitySubmissionDiscussionMessageToSubmissionDiscussionMessage(EntitySubmissionDiscussionMessage entitySubmissionDiscussionMessage, Long journalId) {

        SubmissionDiscussionMessage submissionDiscussionMessage = new SubmissionDiscussionMessage();

        submissionDiscussionMessage.setId(entitySubmissionDiscussionMessage.getId());

        submissionDiscussionMessage.setMessage(entitySubmissionDiscussionMessage.getMessage());

        submissionDiscussionMessage.setUserFullName(entitySubmissionDiscussionMessage.getUser().getFullName());

        submissionDiscussionMessage.setDiscussionId(entitySubmissionDiscussionMessage.getDiscussion().getId());

        submissionDiscussionMessage.setCreatedAt(entitySubmissionDiscussionMessage.getCreatedAt());

        submissionDiscussionMessage.setSubmissionDiscussionMessageFiles(entityDiscussionMessageFilesToDiscussionMessageFiles(entitySubmissionDiscussionMessage.getFiles(), journalId));

        return submissionDiscussionMessage;
    }

    public List<SubmissionDiscussionMessage> mapEntitySubmissionDiscussionMessagesToSubmissionDiscussionMessages(List<EntitySubmissionDiscussionMessage> entitySubmissionDiscussionMessages, Long journalId) {

        List<SubmissionDiscussionMessage> submissionDiscussionMessages = new ArrayList<>();

        entitySubmissionDiscussionMessages.forEach(entitySubmissionDiscussionMessage -> {

            submissionDiscussionMessages.add(mapEntitySubmissionDiscussionMessageToSubmissionDiscussionMessage(entitySubmissionDiscussionMessage, journalId));
        });

        return submissionDiscussionMessages;
    }

    private SubmissionDiscussionFile entitySubmissionDiscussionFileToSubmissionDiscussion(EntitySubmissionDiscussionFile entitySubmissionDiscussionFile, Long journalId) {

        SubmissionDiscussionFile submissionDiscussionFile = new SubmissionDiscussionFile();

        submissionDiscussionFile.setId(entitySubmissionDiscussionFile.getId());

        submissionDiscussionFile.setFilePath(entitySubmissionDiscussionFile.getFilePath());

        submissionDiscussionFile.setFile(entitySubmissionDiscussionFile.getFile());

        submissionDiscussionFile.setCreatedAt(entitySubmissionDiscussionFile.getCreatedAt());

        submissionDiscussionFile.setFileType(commonMapper.entityFilesTypeToFileType(entitySubmissionDiscussionFile.getJournalFileType().getFileType()));

        submissionDiscussionFile.setFileEndPoint(fileStorageService.createDownloadLink(entitySubmissionDiscussionFile.getFilePath()));

        return submissionDiscussionFile;
    }

    private List<SubmissionDiscussionFile> entityDiscussionFilesToDiscussionFiles(List<EntitySubmissionDiscussionFile> entitySubmissionDiscussionFiles, Long journalId) {

        List<SubmissionDiscussionFile> submissionDiscussionFiles = new ArrayList<>();

        entitySubmissionDiscussionFiles.forEach(entitySubmissionDiscussionFile -> submissionDiscussionFiles.add(entitySubmissionDiscussionFileToSubmissionDiscussion(entitySubmissionDiscussionFile, journalId)));

        return submissionDiscussionFiles;

    }

    public SubmissionDiscussion mapEntityDiscussionToDiscussion(EntitySubmissionDiscussion entitySubmissionDiscussion, Long journalId) {

        SubmissionDiscussion submissionDiscussion = new SubmissionDiscussion();

        submissionDiscussion.setId(entitySubmissionDiscussion.getId());

        submissionDiscussion.setStatus(entitySubmissionDiscussion.getStatus());

        submissionDiscussion.setDescription(entitySubmissionDiscussion.getDescription());

        submissionDiscussion.setTopic(entitySubmissionDiscussion.getTopic());

        submissionDiscussion.setCreatedAt(entitySubmissionDiscussion.getCreatedAt());

        submissionDiscussion.setDiscussionMessages(mapEntitySubmissionDiscussionMessagesToSubmissionDiscussionMessages(entitySubmissionDiscussion.getDiscussionMessages(), journalId));

        submissionDiscussion.setDiscussionFiles(entityDiscussionFilesToDiscussionFiles(entitySubmissionDiscussion.getFiles(), journalId));

        return submissionDiscussion;
    }

    public List<SubmissionDiscussion> mapEntitySubmissionDiscussionsToSubmissionDiscussions(List<EntitySubmissionDiscussion> discussions, Long journalId) {

        List<SubmissionDiscussion> submissionDiscussions = new ArrayList<>();

        discussions.forEach(value -> submissionDiscussions.add(mapEntityDiscussionToDiscussion(value, journalId)));

        return submissionDiscussions;
    }

    public SubmissionContributor mapEntitySubmissionContributorToSubmissionContributor(EntitySubmissionContributor entitySubmissionContributor) {

        SubmissionContributor submissionContributor = new SubmissionContributor();

        submissionContributor.setId(entitySubmissionContributor.getUser().getId());

        submissionContributor.setFullName(entitySubmissionContributor.getUser().getFullName());

        return submissionContributor;
    }

    public List<SubmissionContributor> mapEntitySubmissionContributorsToSubmissionContributors(List<EntitySubmissionContributor> entitySubmissionContributors) {

        List<SubmissionContributor> submissionContributors = new ArrayList<>();

        entitySubmissionContributors.forEach(entitySubmissionContributor -> submissionContributors.add(mapEntitySubmissionContributorToSubmissionContributor(entitySubmissionContributor)));

        return submissionContributors;
    }

    SubmissionAuthor mapEntitySubmissionAuthorToEntitySubmissionAuthor(EntitySubmissionAuthor entitySubmissionAuthor) {

        SubmissionAuthor submissionAuthor = new SubmissionAuthor();

        submissionAuthor.setId(entitySubmissionAuthor.getId());

        submissionAuthor.setPrefix(entitySubmissionAuthor.getPrefix());

        submissionAuthor.setFirstName(entitySubmissionAuthor.getFirstName());

        submissionAuthor.setSurName(entitySubmissionAuthor.getSurName());

        submissionAuthor.setMiddleName(entitySubmissionAuthor.getMiddleName());

        submissionAuthor.setEmail(entitySubmissionAuthor.getEmail());

        submissionAuthor.setOrcidId(entitySubmissionAuthor.getOrcidId());

        submissionAuthor.setPrimary(entitySubmissionAuthor.getPrimary());


        return submissionAuthor;
    }

    public List<SubmissionAuthor> mapEntitySubmissionAuthorsToEntitySubmissionAuthors(List<EntitySubmissionAuthor> entitySubmissionAuthors) {

        return entitySubmissionAuthors.stream().map(this::mapEntitySubmissionAuthorToEntitySubmissionAuthor).collect(Collectors.toList());
    }

    public SubmissionFile entitySubmissionFileToSubmissionFile(EntitySubmissionFile entityReviewRoundFile) {

        SubmissionFile reviewRoundFile = new SubmissionFile();

        reviewRoundFile.setFileType(commonMapper.entityFilesTypeToFileType(entityReviewRoundFile.getJournalFileType().getFileType()));

        reviewRoundFile.setFile(entityReviewRoundFile.getFile());

        reviewRoundFile.setFileEndPoint(fileStorageService.createDownloadLink(entityReviewRoundFile.getFilePath()));

        reviewRoundFile.setCreatedAt(entityReviewRoundFile.getCreatedAt());

        reviewRoundFile.setId(entityReviewRoundFile.getId());

        return reviewRoundFile;
    }

    public List<SubmissionFile> entitySubmissionFilesToSubmissionFiles(List<EntitySubmissionFile> entitySubmissionFiles) {

        List<SubmissionFile> reviewRoundFiles = new ArrayList<>();

        entitySubmissionFiles.forEach(entityReviewRoundFile -> reviewRoundFiles.add(entitySubmissionFileToSubmissionFile(entityReviewRoundFile)));

        return reviewRoundFiles;
    }
}
