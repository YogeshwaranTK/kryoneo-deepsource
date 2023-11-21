package com.kjms.service.mapper;

import com.kjms.domain.*;
import com.kjms.service.WorkflowFileStorageService;
import com.kjms.service.dto.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CopyEditingMapper {


    private final CommonMapper commonMapper;

    public CopyEditingMapper(CommonMapper commonMapper, WorkflowFileStorageService fileStorageService) {
        this.commonMapper = commonMapper;
        this.fileStorageService = fileStorageService;
    }

    private final WorkflowFileStorageService fileStorageService;


    public List<CopyEditingDraftFile> entityCopyEditingDraftFilesToCopyEditingDraftFiles(List<EntityCopyEditingDraftFile> entityCopyEditingDraftFiles) {

        List<CopyEditingDraftFile> copyEditingDraftFiles = new ArrayList<>();

        entityCopyEditingDraftFiles.forEach(entityCopyEditingDraftFile -> copyEditingDraftFiles.add(mapEntityCopyEditingDraftFileToCopyEditingDraftFile(entityCopyEditingDraftFile)));

        return copyEditingDraftFiles;
    }

    private CopyEditingDraftFile mapEntityCopyEditingDraftFileToCopyEditingDraftFile(EntityCopyEditingDraftFile entityCopyEditingDraftFile) {

        CopyEditingDraftFile copyEditingDraftFile = new CopyEditingDraftFile();

        copyEditingDraftFile.setFileType(commonMapper.entityFilesTypeToFileType(entityCopyEditingDraftFile.getJournalFileType().getFileType()));

        copyEditingDraftFile.setFile(entityCopyEditingDraftFile.getFile());

        copyEditingDraftFile.setCreatedAt(entityCopyEditingDraftFile.getCreatedAt());

        copyEditingDraftFile.setId(entityCopyEditingDraftFile.getId());

        return copyEditingDraftFile;

    }

    public List<CopyEditedFile> entityCopyEditedFilesToCopyEditedFiles(List<EntityCopyEditedFile> entityCopyEditedFiles) {

        List<CopyEditedFile> copyEditedFiles = new ArrayList<>();

        entityCopyEditedFiles.forEach(entityCopyEditedFile -> copyEditedFiles.add(entityCopyEditedFileToCopyEditedFile(entityCopyEditedFile)));

        return copyEditedFiles;
    }

    private CopyEditedFile entityCopyEditedFileToCopyEditedFile(EntityCopyEditedFile entityCopyEditedFile) {

        CopyEditedFile copyEditingDraftFile = new CopyEditedFile();

        copyEditingDraftFile.setFileType(commonMapper.entityFilesTypeToFileType(entityCopyEditedFile.getJournalFileType().getFileType()));

        copyEditingDraftFile.setFile(entityCopyEditedFile.getFile());

        copyEditingDraftFile.setCreatedAt(entityCopyEditedFile.getCreatedAt());

        copyEditingDraftFile.setId(entityCopyEditedFile.getId());

        return copyEditingDraftFile;
    }

    public CopyEditingDiscussion mapEntityCopyEditingDiscussionToCopyEditingDiscussion(EntityCopyEditingDiscussion entityCopyEditingDiscussion, Long journalId) {

        CopyEditingDiscussion copyEditingDiscussion = new CopyEditingDiscussion();

        copyEditingDiscussion.setId(entityCopyEditingDiscussion.getId());

        copyEditingDiscussion.setStatus(entityCopyEditingDiscussion.getStatus());

        copyEditingDiscussion.setDescription(entityCopyEditingDiscussion.getDescription());

        copyEditingDiscussion.setTopic(entityCopyEditingDiscussion.getTopic());

        copyEditingDiscussion.setCreatedAt(entityCopyEditingDiscussion.getCreatedAt());

        copyEditingDiscussion.setDiscussionFiles(entityCopyEditingDiscussionFilesToCopyEditingDiscussionFiles(entityCopyEditingDiscussion.getFiles(), journalId));

        copyEditingDiscussion.setDiscussionMessages(mapEntityCopyEditingDiscussionMessagesToCopyEditingDiscussionMessages(entityCopyEditingDiscussion.getDiscussionMessages()));

        return copyEditingDiscussion;
    }

    private List<CopyEditingDiscussionFile> entityCopyEditingDiscussionFilesToCopyEditingDiscussionFiles(List<EntityCopyEditingDiscussionFile> entityCopyEditingDiscussionFiles, Long journalId) {

        List<CopyEditingDiscussionFile> copyEditingDiscussionFilesuctionDiscussionFiles = new ArrayList<>();

        entityCopyEditingDiscussionFiles.forEach(entityCopyEditingDiscussionFile -> copyEditingDiscussionFilesuctionDiscussionFiles.add(entityCopyEditingDiscussionFileToCopyEditingDiscussion(entityCopyEditingDiscussionFile, journalId)));

        return copyEditingDiscussionFilesuctionDiscussionFiles;
    }

    private CopyEditingDiscussionFile entityCopyEditingDiscussionFileToCopyEditingDiscussion(EntityCopyEditingDiscussionFile entityCopyEditingDiscussionFile, Long journalId) {

        CopyEditingDiscussionFile copyEditingDiscussionFile = new CopyEditingDiscussionFile();

        copyEditingDiscussionFile.setId(entityCopyEditingDiscussionFile.getId());

        copyEditingDiscussionFile.setFilePath(entityCopyEditingDiscussionFile.getFilePath());

        copyEditingDiscussionFile.setFile(entityCopyEditingDiscussionFile.getFile());

        copyEditingDiscussionFile.setCreatedAt(entityCopyEditingDiscussionFile.getCreatedAt());

        copyEditingDiscussionFile.setFileType(commonMapper.entityFilesTypeToFileType(entityCopyEditingDiscussionFile.getJournalFileType().getFileType()));

        copyEditingDiscussionFile.setFileEndPoint(fileStorageService.createDownloadLink(entityCopyEditingDiscussionFile.getFilePath()));

        return copyEditingDiscussionFile;
    }

    public List<CopyEditingDiscussion> entityCopyEditingDiscussionsToCopyEditingDiscussions(List<EntityCopyEditingDiscussion> entityCopyEditingDiscussions, Long journalId) {

        List<CopyEditingDiscussion> copyEditingDiscussions = new ArrayList<>();
        entityCopyEditingDiscussions.forEach(entityCopyEditingDiscussion -> copyEditingDiscussions.add(entityCopyEditingDiscussionToCopyEditingDiscussion(entityCopyEditingDiscussion, journalId)));
        return copyEditingDiscussions;
    }

    private CopyEditingDiscussion entityCopyEditingDiscussionToCopyEditingDiscussion(EntityCopyEditingDiscussion entityCopyEditingDiscussion, Long journalId) {

        CopyEditingDiscussion copyEditingDiscussion = new CopyEditingDiscussion();

        copyEditingDiscussion.setId(entityCopyEditingDiscussion.getId());

        copyEditingDiscussion.setStatus(entityCopyEditingDiscussion.getStatus());

        copyEditingDiscussion.setDescription(entityCopyEditingDiscussion.getDescription());

        copyEditingDiscussion.setTopic(entityCopyEditingDiscussion.getTopic());

        copyEditingDiscussion.setCreatedAt(entityCopyEditingDiscussion.getCreatedAt());

        copyEditingDiscussion.setDiscussionFiles(entityDiscussionFilesToDiscussionFiles(entityCopyEditingDiscussion.getFiles(), journalId));

        copyEditingDiscussion.setDiscussionMessages(mapEntityCopyEditingDiscussionMessagesToCopyEditingDiscussionMessages(entityCopyEditingDiscussion.getDiscussionMessages()));

        return copyEditingDiscussion;
    }

    private CopyEditingDiscussionFile entityCopyEditingDiscussionFileToCopyEditingDiscussionFile(EntityCopyEditingDiscussionFile entityCopyEditingDiscussionFile, Long journalId) {

        CopyEditingDiscussionFile copyEditingDiscussionFile = new CopyEditingDiscussionFile();

        copyEditingDiscussionFile.setId(entityCopyEditingDiscussionFile.getId());

        copyEditingDiscussionFile.setFilePath(entityCopyEditingDiscussionFile.getFilePath());

        copyEditingDiscussionFile.setFile(entityCopyEditingDiscussionFile.getFile());

        copyEditingDiscussionFile.setCreatedAt(entityCopyEditingDiscussionFile.getCreatedAt());

        copyEditingDiscussionFile.setFileType(commonMapper.entityFilesTypeToFileType(entityCopyEditingDiscussionFile.getJournalFileType().getFileType()));

        copyEditingDiscussionFile.setFileEndPoint(fileStorageService.createDownloadLink(entityCopyEditingDiscussionFile.getFilePath()));

        return copyEditingDiscussionFile;
    }

    private List<CopyEditingDiscussionFile> entityDiscussionFilesToDiscussionFiles(List<EntityCopyEditingDiscussionFile> entityCopyEditingDiscussionFiles, Long journalId) {

        List<CopyEditingDiscussionFile> copyEditingDiscussionFiles = new ArrayList<>();

        entityCopyEditingDiscussionFiles.forEach(entityCopyEditingDiscussionFile -> copyEditingDiscussionFiles.add(entityCopyEditingDiscussionFileToCopyEditingDiscussionFile(entityCopyEditingDiscussionFile, journalId)));

        return copyEditingDiscussionFiles;
    }

    public CopyEditingDiscussionMessage mapEntityCopyEditingDiscussionMessageToCopyEditingDiscussionMessage(EntityCopyEditingDiscussionMessage entityCopyEditingDiscussionMessage) {

        CopyEditingDiscussionMessage copyEditingDiscussionMessage = new CopyEditingDiscussionMessage();

        copyEditingDiscussionMessage.setId(entityCopyEditingDiscussionMessage.getId());

        copyEditingDiscussionMessage.setMessage(entityCopyEditingDiscussionMessage.getMessage());

        copyEditingDiscussionMessage.setUserFullName(entityCopyEditingDiscussionMessage.getUser().getFullName());

        copyEditingDiscussionMessage.setDiscussionId(entityCopyEditingDiscussionMessage.getDiscussion().getId());

        copyEditingDiscussionMessage.setCreatedAt(entityCopyEditingDiscussionMessage.getCreatedAt());

        copyEditingDiscussionMessage.setProductionDiscussionMessageFiles(entityDiscussionMessageFilesToDiscussionMessageFiles(entityCopyEditingDiscussionMessage.getFiles()));

        return copyEditingDiscussionMessage;
    }

    private List<CopyEditingDiscussionMessageFile> entityDiscussionMessageFilesToDiscussionMessageFiles(List<EntityCopyEditingDiscussionMessageFile> entityCopyEditingDiscussionMessageFiles) {

        List<CopyEditingDiscussionMessageFile> copyEditingDiscussionMessageFiles = new ArrayList<>();

        entityCopyEditingDiscussionMessageFiles.forEach(entityProductionDiscussionFile -> {
            copyEditingDiscussionMessageFiles.add(entityCopyEditingDiscussionMessageFileToCopyEditingDiscussionMessageFile(entityProductionDiscussionFile));
        });
        return copyEditingDiscussionMessageFiles;
    }

    private CopyEditingDiscussionMessageFile entityCopyEditingDiscussionMessageFileToCopyEditingDiscussionMessageFile(EntityCopyEditingDiscussionMessageFile entityCopyEditingDiscussionMessageFile) {

        CopyEditingDiscussionMessageFile copyEditingDiscussionMessageFile = new CopyEditingDiscussionMessageFile();

        copyEditingDiscussionMessageFile.setId(entityCopyEditingDiscussionMessageFile.getId());

        copyEditingDiscussionMessageFile.setCreatedAt(entityCopyEditingDiscussionMessageFile.getCreatedAt());

        copyEditingDiscussionMessageFile.setFile(entityCopyEditingDiscussionMessageFile.getFile());

        copyEditingDiscussionMessageFile.setFileType(commonMapper.entityFilesTypeToFileType(entityCopyEditingDiscussionMessageFile.getJournalFileType().getFileType()));

        copyEditingDiscussionMessageFile.setFileEndPoint(fileStorageService.createDownloadLink(entityCopyEditingDiscussionMessageFile.getFilePath()));

        return copyEditingDiscussionMessageFile;
    }


    public List<CopyEditingDiscussionMessage> mapEntityCopyEditingDiscussionMessagesToCopyEditingDiscussionMessages(List<EntityCopyEditingDiscussionMessage> entityCopyEditingDiscussionMessages) {

        List<CopyEditingDiscussionMessage> copyEditingDiscussionMessages = new ArrayList<>();

        entityCopyEditingDiscussionMessages.forEach(entityProductionDiscussionMessage -> {

            copyEditingDiscussionMessages.add(mapEntityCopyEditingDiscussionMessageToCopyEditingDiscussionMessage(entityProductionDiscussionMessage));
        });

        return copyEditingDiscussionMessages;
    }

    public List<CopyEditingContributor> mapEntityCopyEditingContributorsToCopyEditingContributors(List<EntityCopyEditingContributor> entityCopyEditingContributors) {

        List<CopyEditingContributor> copyEditingContributors = new ArrayList<>();

        entityCopyEditingContributors.forEach(entityCopyEditingContributor -> {

            copyEditingContributors.add(entityCopyEditingContributorToCopyEditingContributor(entityCopyEditingContributor));
        });

        return copyEditingContributors;

    }

    public CopyEditingContributor entityCopyEditingContributorToCopyEditingContributor(EntityCopyEditingContributor entityCopyEditingContributor) {

        CopyEditingContributor copyEditingContributor = new CopyEditingContributor();

        copyEditingContributor.setId(entityCopyEditingContributor.getUser().getId());

        copyEditingContributor.setFullName(entityCopyEditingContributor.getUser().getFullName());

        return copyEditingContributor;
    }

    public CopyEditingDraftFile entityCopyEditingDraftFilesToCopyEditingDraftFile(EntityCopyEditingDraftFile entityCopyEditingDraftFile) {

        CopyEditingDraftFile copyEditingDraftFile = new CopyEditingDraftFile();

        copyEditingDraftFile.setFileType(commonMapper.entityFilesTypeToFileType(entityCopyEditingDraftFile.getJournalFileType().getFileType()));
        copyEditingDraftFile.setFile(entityCopyEditingDraftFile.getFile());
        copyEditingDraftFile.setId(entityCopyEditingDraftFile.getId());
        copyEditingDraftFile.setFileEndPoint(fileStorageService.createDownloadLink(entityCopyEditingDraftFile.getFilePath()));
        copyEditingDraftFile.setSubmissionId(entityCopyEditingDraftFile.getSubmission().getId());
        copyEditingDraftFile.setCreatedAt(entityCopyEditingDraftFile.getCreatedAt());

        return copyEditingDraftFile;
    }


    public List<CopyEditingDraftFile> mapEntityCopyEditingDraftFilesToCopyEditingDraftFile(List<EntityCopyEditingDraftFile> entityDraftFiles) {

        List<CopyEditingDraftFile>  copyEditingDraftFiles = new ArrayList<>();

        entityDraftFiles.forEach(file -> copyEditingDraftFiles.add(entityCopyEditingDraftFilesToCopyEditingDraftFile(file)));

        return copyEditingDraftFiles;
    }

    public List<CopyEditedFile> mapEntityCopyEditedFilesToCopyEditedFile(List<EntityCopyEditedFile> entityCopyEditedFiles) {

        List<CopyEditedFile>  copyEditedFiles = new ArrayList<>();

        entityCopyEditedFiles.forEach(file -> copyEditedFiles.add(entityCopyEditedFilesToCopyEditedFile(file)));

        return copyEditedFiles;
    }

    private CopyEditedFile entityCopyEditedFilesToCopyEditedFile(EntityCopyEditedFile entityCopyEditedFile) {

        CopyEditedFile copyEditedFile = new CopyEditedFile();

        copyEditedFile.setFileType(commonMapper.entityFilesTypeToFileType(entityCopyEditedFile.getJournalFileType().getFileType()));
        copyEditedFile.setFile(entityCopyEditedFile.getFile());
        copyEditedFile.setId(entityCopyEditedFile.getId());
        copyEditedFile.setFileEndPoint(fileStorageService.createDownloadLink(entityCopyEditedFile.getFilePath()));
        copyEditedFile.setSubmissionId(entityCopyEditedFile.getSubmission().getId());
        copyEditedFile.setCreatedAt(entityCopyEditedFile.getCreatedAt());

        return copyEditedFile;
    }
}
