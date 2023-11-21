package com.kjms.service.mapper;

import com.kjms.domain.*;
import com.kjms.service.WorkflowFileStorageService;
import com.kjms.service.dto.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductionMapper {

    private final WorkflowFileStorageService fileStorageService;


    private final CommonMapper commonMapper;

    public ProductionMapper(WorkflowFileStorageService fileStorageService, CommonMapper commonMapper) {
        this.fileStorageService = fileStorageService;
        this.commonMapper = commonMapper;
    }


    public ProductionDiscussion mapEntityDiscussionToDiscussion(EntityProductionDiscussion entityProductionDiscussion, Long journalId) {

        ProductionDiscussion productionDiscussion = new ProductionDiscussion();

        productionDiscussion.setId(entityProductionDiscussion.getId());

        productionDiscussion.setStatus(entityProductionDiscussion.getStatus());

        productionDiscussion.setDescription(entityProductionDiscussion.getDescription());

        productionDiscussion.setTopic(entityProductionDiscussion.getTopic());

        productionDiscussion.setCreatedAt(entityProductionDiscussion.getCreatedAt());

        productionDiscussion.setDiscussionMessages(mapEntityProductionDiscussionMessagesToProductionDiscussionMessages(entityProductionDiscussion.getDiscussionMessages(), journalId));

        productionDiscussion.setDiscussionFiles(entityDiscussionFilesToDiscussionFiles(entityProductionDiscussion.getFiles(), journalId));

        return productionDiscussion;
    }

    private List<ProductionDiscussionFile> entityDiscussionFilesToDiscussionFiles(List<EntityProductionDiscussionFile> entityProductionDiscussionFiles, Long journalId) {

        List<ProductionDiscussionFile> productionDiscussionFiles = new ArrayList<>();

        entityProductionDiscussionFiles.forEach(entityProductionDiscussionFile -> {
            productionDiscussionFiles.add(entityProductionDiscussionFileToProductionDiscussion(entityProductionDiscussionFile, journalId));
        });
        return productionDiscussionFiles;
    }

    private List<ProductionDiscussionMessageFile> entityDiscussionMessageFilesToDiscussionMessageFiles(List<EntityProductionDiscussionMessageFile> entityProductionDiscussionMessageFiles, Long journalId) {

        List<ProductionDiscussionMessageFile> productionDiscussionFiles = new ArrayList<>();

        entityProductionDiscussionMessageFiles.forEach(entityProductionDiscussionFile -> {
            productionDiscussionFiles.add(entityProductionDiscussionMessageFileToProductionDiscussionMessageFile(entityProductionDiscussionFile, journalId));
        });
        return productionDiscussionFiles;
    }

    private ProductionDiscussionMessageFile entityProductionDiscussionMessageFileToProductionDiscussionMessageFile(EntityProductionDiscussionMessageFile entityProductionDiscussionFile, Long journalId) {


        ProductionDiscussionMessageFile productionReadyFile = new ProductionDiscussionMessageFile();

        productionReadyFile.setId(entityProductionDiscussionFile.getId());

        productionReadyFile.setCreatedAt(entityProductionDiscussionFile.getCreatedAt());

        productionReadyFile.setFile(entityProductionDiscussionFile.getFile());

        productionReadyFile.setFileType(commonMapper.entityFilesTypeToFileType(entityProductionDiscussionFile.getJournalFileType().getFileType()));

        productionReadyFile.setFileEndPoint(fileStorageService.createDownloadLink(entityProductionDiscussionFile.getFilePath()));

        return productionReadyFile;
    }

    private ProductionDiscussionFile entityProductionDiscussionFileToProductionDiscussion(EntityProductionDiscussionFile entityProductionDiscussionFile, Long journalId) {

        ProductionDiscussionFile productionDiscussionFile = new ProductionDiscussionFile();

        productionDiscussionFile.setId(entityProductionDiscussionFile.getId());

        productionDiscussionFile.setFilePath(entityProductionDiscussionFile.getFilePath());

        productionDiscussionFile.setFile(entityProductionDiscussionFile.getFile());

        productionDiscussionFile.setCreatedAt(entityProductionDiscussionFile.getCreatedAt());

        productionDiscussionFile.setFileType(commonMapper.entityFilesTypeToFileType(entityProductionDiscussionFile.getJournalFileType().getFileType()));

        productionDiscussionFile.setFileEndPoint(fileStorageService.createDownloadLink(entityProductionDiscussionFile.getFilePath()));

        return productionDiscussionFile;
    }

    public List<ProductionDiscussion> entityProductionDiscussionsToProductionDiscussions(List<EntityProductionDiscussion> discussions, Long journalId) {

        List<ProductionDiscussion> productionDiscussions = new ArrayList<>();

        discussions.forEach(value -> productionDiscussions.add(mapEntityDiscussionToDiscussion(value, journalId)));

        return productionDiscussions;
    }

    public ProductionReadyFile entityProductionReadyFileToProductionReadyFile(EntityProductionReadyFile entityProductionReadyFile, Long journalId) {

        ProductionReadyFile productionReadyFile = new ProductionReadyFile();

        productionReadyFile.setId(entityProductionReadyFile.getId());

        productionReadyFile.setCreatedBy(entityProductionReadyFile.getCreatedBy().getFullName());

        productionReadyFile.setCreatedAt(entityProductionReadyFile.getCreatedAt());

        productionReadyFile.setFile(entityProductionReadyFile.getFile());

        productionReadyFile.setFileType(commonMapper.entityFilesTypeToFileType(entityProductionReadyFile.getJournalFileType().getFileType()));

        productionReadyFile.setFileEndPoint(fileStorageService.createDownloadLink(entityProductionReadyFile.getFilePath()));

        return productionReadyFile;
    }

    public List<ProductionReadyFile> entityProductionReadyFilesToProductionReadyFiles(List<EntityProductionReadyFile> entityProductionReadyFiles, Long journalId) {

        List<ProductionReadyFile> productionReadyFiles = new ArrayList<>();

        entityProductionReadyFiles.forEach(file -> productionReadyFiles.add(entityProductionReadyFileToProductionReadyFile(file, journalId)));

        return productionReadyFiles;

    }


    public ProductionCompletedFile entityProductionCompletedFileToProductionCompletedFile(EntityProductionCompletedFile entityProductionCompletedFile, Long journalId) {

        ProductionCompletedFile productionCompletedFile = new ProductionCompletedFile();

        productionCompletedFile.setFile(entityProductionCompletedFile.getFile());

        productionCompletedFile.setFileType(commonMapper.entityFilesTypeToFileType(entityProductionCompletedFile.getJournalFileType().getFileType()));

        productionCompletedFile.setCreatedBy(entityProductionCompletedFile.getCreatedBy().getFullName());

        productionCompletedFile.setFile(entityProductionCompletedFile.getFile());

        productionCompletedFile.setFileEndPoint(fileStorageService.createDownloadLink(entityProductionCompletedFile.getFilePath()));

        return productionCompletedFile;
    }

    public List<ProductionCompletedFile> entityProductionCompletedFilesToProductionCompletedFiles(List<EntityProductionCompletedFile> entityProductionCompletedFiles, Long journalId) {

        List<ProductionCompletedFile> productionCompletedFiles = new ArrayList<>();

        entityProductionCompletedFiles.forEach(entityProductionCompletedFile ->

            productionCompletedFiles.add(entityProductionCompletedFileToProductionCompletedFile(entityProductionCompletedFile, journalId))
        );

        return productionCompletedFiles;
    }

    public ProductionDiscussionMessage mapEntityProductionDiscussionMessageToProductionDiscussionMessage(EntityProductionDiscussionMessage entityProductionDiscussionMessage, Long journalId) {

        ProductionDiscussionMessage productionDiscussionMessage = new ProductionDiscussionMessage();

        productionDiscussionMessage.setId(entityProductionDiscussionMessage.getId());

        productionDiscussionMessage.setMessage(entityProductionDiscussionMessage.getMessage());

        productionDiscussionMessage.setUserFullName(entityProductionDiscussionMessage.getUser().getFullName());

        productionDiscussionMessage.setDiscussionId(entityProductionDiscussionMessage.getDiscussion().getId());

        productionDiscussionMessage.setCreatedAt(entityProductionDiscussionMessage.getCreatedAt());

        productionDiscussionMessage.setProductionDiscussionMessageFiles(entityDiscussionMessageFilesToDiscussionMessageFiles(entityProductionDiscussionMessage.getFiles(), journalId));

        return productionDiscussionMessage;
    }

    public List<ProductionDiscussionMessage> mapEntityProductionDiscussionMessagesToProductionDiscussionMessages(List<EntityProductionDiscussionMessage> entityProductionDiscussionMessages, Long journalId) {

        List<ProductionDiscussionMessage> productionDiscussionMessages = new ArrayList<>();

        entityProductionDiscussionMessages.forEach(entityProductionDiscussionMessage -> {

            productionDiscussionMessages.add(mapEntityProductionDiscussionMessageToProductionDiscussionMessage(entityProductionDiscussionMessage, journalId));
        });

        return productionDiscussionMessages;
    }


    public List<ProductionContributor> entityProductionContributorsToProductionContributors(

        List<EntityProductionContributor> entityProductionContributors) {

        List<ProductionContributor> productionContributors = new ArrayList<>();

        entityProductionContributors.forEach(entityProductionContributor -> productionContributors.add(entityProductionContributorToProductionContributor(entityProductionContributor)));

        return productionContributors;
    }

    public ProductionContributor entityProductionContributorToProductionContributor(EntityProductionContributor entityProductionContributor) {

        ProductionContributor productionContributor = new ProductionContributor();

        productionContributor.setId(entityProductionContributor.getUser().getId());

        productionContributor.setFullName(entityProductionContributor.getUser().getFullName());

        return productionContributor;
    }
}
