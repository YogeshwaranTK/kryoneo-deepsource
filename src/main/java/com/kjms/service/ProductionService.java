package com.kjms.service;

import com.kjms.config.notification.ProductionEmailKeyConstant;
import com.kjms.domain.*;
import com.kjms.repository.*;
import com.kjms.security.BadRequestAlertException;
import com.kjms.security.SecurityUtils;
import com.kjms.service.dto.*;
import com.kjms.service.errors.JournalNotFoundException;
import com.kjms.service.mail.MailService;
import com.kjms.service.mapper.ProductionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductionService {

    private final EntitySubmissionRepository entitySubmissionRepository;
    private final SubmissionService submissionService;
    private final UserService userService;
    private final MailService mailService;
    private final JournalService journalService;
    private final EntityProductionContributorRepository entityProductionContributorRepository;
    private final EntityUserRepository entityUserRepository;
    private final ProductionMapper productionMapper;
    private final EntityJournalRepository entityJournalRepository;
    private final EntityProductionDiscussionRepository entityProductionDiscussionRepository;
    private final WorkflowFileStorageService fileStorageService;
    private final EntityProductionDiscussionFileRepository entityProductionDiscussionFileRepository;
    private final EntityProductionDiscussionMemberRepository entityProductionDiscussionMemberRepository;
    private final EntityProductionReadyFileRepository entityProductionReadyFileRepository;
    private final EntityProductionCompletedFileRepository entityProductionCompletedFileRepository;
    private final EntityProductionDiscussionMessageRepository entityProductionDiscussionMessageRepository;
    private final EntityProductionDiscussionMessageFileRepository entityProductionDiscussionMessageFileRepository;
    private final EntityFileTypeRepository entityFileTypeRepository;
    private final CommonService commonService;

    private final EntitySubmissionFileRepository entitySubmissionFileRepository;

    private final EntityReviewRoundFileRepository entityReviewRoundFileRepository;
    private final EntityCopyEditedFileRepository entityCopyEditedFileRepository;


    public ProductionService(EntitySubmissionRepository entitySubmissionRepository, SubmissionService submissionService,
                             UserService userService, MailService mailService, JournalService journalService, EntityProductionContributorRepository entityProductionContributorRepository, EntityUserRepository entityUserRepository,
                             ProductionMapper productionMapper, EntityJournalRepository entityJournalRepository,
                             EntityProductionDiscussionRepository entityProductionDiscussionRepository,
                             WorkflowFileStorageService fileStorageService,
                             EntityProductionDiscussionFileRepository entityProductionDiscussionFileRepository,
                             EntityProductionDiscussionMemberRepository entityProductionDiscussionMemberRepository,
                             EntityProductionReadyFileRepository entityProductionReadyFileRepository, EntityProductionCompletedFileRepository entityProductionCompletedFileRepository,
                             EntityProductionDiscussionMessageRepository entityProductionDiscussionMessageRepository,
                             EntityProductionDiscussionMessageFileRepository entityProductionDiscussionMessageFileRepository, EntityFileTypeRepository entityFileTypeRepository, CommonService commonService, EntitySubmissionFileRepository entitySubmissionFileRepository, EntityReviewRoundFileRepository entityReviewRoundFileRepository,
                             EntityCopyEditedFileRepository entityCopyEditedFileRepository) {
        this.entitySubmissionRepository = entitySubmissionRepository;
        this.submissionService = submissionService;
        this.userService = userService;
        this.mailService = mailService;
        this.journalService = journalService;
        this.entityProductionContributorRepository = entityProductionContributorRepository;
        this.entityUserRepository = entityUserRepository;
        this.productionMapper = productionMapper;
        this.entityJournalRepository = entityJournalRepository;
        this.entityProductionDiscussionRepository = entityProductionDiscussionRepository;
        this.fileStorageService = fileStorageService;
        this.entityProductionDiscussionFileRepository = entityProductionDiscussionFileRepository;
        this.entityProductionDiscussionMemberRepository = entityProductionDiscussionMemberRepository;
        this.entityProductionReadyFileRepository = entityProductionReadyFileRepository;
        this.entityProductionCompletedFileRepository = entityProductionCompletedFileRepository;
        this.entityProductionDiscussionMessageRepository = entityProductionDiscussionMessageRepository;
        this.entityProductionDiscussionMessageFileRepository = entityProductionDiscussionMessageFileRepository;
        this.entityFileTypeRepository = entityFileTypeRepository;
        this.commonService = commonService;
        this.entitySubmissionFileRepository = entitySubmissionFileRepository;
        this.entityReviewRoundFileRepository = entityReviewRoundFileRepository;
        this.entityCopyEditedFileRepository = entityCopyEditedFileRepository;
    }


    public List<ProductionContributor> getContributors(Long journalId, Long submissionId) {

        return productionMapper.entityProductionContributorsToProductionContributors(entityProductionContributorRepository.getContributors(journalId, submissionId));
    }

    public ProductionContributor getContributor(Long journalId, Long submissionId, String userId) {

        Optional<EntityProductionContributor> entityUser = entityProductionContributorRepository.findContributorUser(journalId, submissionId, userId);

        return entityUser.map(productionMapper::entityProductionContributorToProductionContributor).orElse(null);

    }

    public List<ProductionContributor> addContributor(Long journalId, Long submissionId, List<String> userIds, String description) {

        List<EntityProductionContributor> entityProductionContributors = new ArrayList<>();

        Optional<EntitySubmission> submissionArticle = entitySubmissionRepository.findById(submissionId);

        if (submissionArticle.isEmpty()) {
            throw new RuntimeException();
        }
        EntitySubmission entitySubmission = submissionArticle.get();

        for (String userId : userIds) {

            if (getContributor(journalId, submissionId, userId) == null) {

                EntityProductionContributor entityProductionContributor = new EntityProductionContributor();

                Optional<EntityUser> entityUser = entityUserRepository.findById(userId);

                entityUser.ifPresent(entityProductionContributor::setUser);

                entityProductionContributor.setSubmission(entitySubmission);

                entityUserRepository.findById(Objects.requireNonNull(SecurityUtils.getCurrentUserId())).ifPresent(entityProductionContributor::setCreatedBy);

                entityProductionContributor.setCreatedAt(Instant.now());
                entityProductionContributor.setDeleted(false);
                entityProductionContributors.add(entityProductionContributor);
            }

            entityProductionContributorRepository.saveAll(entityProductionContributors);

            sendMailToProductionContributor(entityProductionContributors, description, journalId, entitySubmission.getTitle());

        }
        return productionMapper.entityProductionContributorsToProductionContributors(entityProductionContributors);
    }

    private void sendMailToProductionContributor(List<EntityProductionContributor> entityProductionContributors, String description, Long journalId, String submissionTitle) {

        EntityJournal entityJournal = entityJournalRepository
            .findOneByIdAndIsDeletedIsFalse(journalId)
            .orElseThrow(JournalNotFoundException::new);

        final String journalTitle = entityJournal.getKey() + " - " + entityJournal.getTitle();

//        entityProductionContributors
//            .forEach(entityProductionContributor -> mailService.sendProductionUserAddEmailFromTemplate(entityProductionContributor.getUser(),
//                ProductionEmailKeyConstant.USER_ADDED_SUBJECT, submissionTitle,
//                description, journalTitle));
    }

    public Production getProductionDetail(Long journalId, Long submissionId) {

        Production production = new Production();

        production.setProductionContributors(getContributors(journalId, submissionId));

        production.setDiscussions(getDiscussions(journalId, submissionId));

        production.setProductionReadyFiles(getProductionReadyFiles(journalId, submissionId));

        production.setProductionCompletedFiles(getProductionCompletedFiles(journalId, submissionId));

        return production;
    }

    public List<ProductionCompletedFile> getProductionCompletedFiles(Long journalId, Long submissionId) {


        return productionMapper.entityProductionCompletedFilesToProductionCompletedFiles(entityProductionCompletedFileRepository.getFiles(journalId, submissionId), journalId);
    }

    public List<ProductionReadyFile> getProductionReadyFiles(Long journalId, Long submissionId) {

        return productionMapper.entityProductionReadyFilesToProductionReadyFiles(entityProductionReadyFileRepository.getFiles(journalId, submissionId), journalId);
    }

    public ProductionDiscussion createDiscussion(Long journalId, Long submissionId, String topic, String description, Map<String, MultipartFile> files, List<String> members) {

        EntityProductionDiscussion entityProductionDiscussion = new EntityProductionDiscussion();

        entityProductionDiscussion.setTopic(topic);

        entityProductionDiscussion.setDescription(description);

        entityProductionDiscussion.setStatus(ProductionDiscussionStatus.open);

        entityUserRepository.findById(Objects.requireNonNull(SecurityUtils.getCurrentUserId())).ifPresent(entityProductionDiscussion::setUser);

        entitySubmissionRepository.findById(submissionId).ifPresent(entityProductionDiscussion::setSubmission);

        entityJournalRepository.findById(journalId).ifPresent(entityProductionDiscussion::setJournal);

        entityUserRepository.findById(Objects.requireNonNull(SecurityUtils.getCurrentUserId())).
            ifPresent(entityProductionDiscussion::setCreatedBy);

        entityProductionDiscussion.setCreatedAt(Instant.now());

        entityProductionDiscussionRepository.save(entityProductionDiscussion);

        List<EntityProductionDiscussionFile> entityProductionDiscussionFiles = new ArrayList<>();

        files.forEach((key, file) -> {

            String typeId = Arrays.stream(key.split("-")).collect(Collectors.toList()).get(0);

            String filePath = fileStorageService.uploadToFinalDirectory(
                commonService.buildFileStoragePath(FilePartition.workflow, journalId,
                    submissionId, WorkflowStage.PRODUCTION, "discussion", file.getOriginalFilename()), file);

            EntityProductionDiscussionFile entityProductionDiscussionFile = new EntityProductionDiscussionFile();

            entityProductionDiscussionFile.setFile(file.getOriginalFilename());

            entityProductionDiscussionFile.setFilePath(filePath);

            entityFileTypeRepository.findFileTypeByJournalIdAndFileTypeId(journalId, Long.valueOf(typeId)).ifPresent(entityProductionDiscussionFile::setJournalFileType);

            entityProductionDiscussionFile.setProductionDiscussion(entityProductionDiscussion);

            entityProductionDiscussionFile.setCreatedAt(Instant.now());

            entityProductionDiscussionFile.setCreatedBy(userService.getCurrentUser());

            entityProductionDiscussionFiles.add(entityProductionDiscussionFile);

        });

        entityProductionDiscussionFileRepository.saveAll(entityProductionDiscussionFiles);

        entityProductionDiscussion.setFiles(entityProductionDiscussionFiles);

        List<EntityProductionDiscussionMember> entityProductionDiscussionMembers = new ArrayList<>();

        members.forEach(member -> {

            EntityProductionDiscussionMember entityProductionDiscussionMember = new EntityProductionDiscussionMember();

            entityUserRepository.findById(member).ifPresent(entityProductionDiscussionMember::setUser);

            entityProductionDiscussionMember.setDiscussion(entityProductionDiscussion);

            entityUserRepository.findById(SecurityUtils.getCurrentUserId()).ifPresent(entityProductionDiscussionMember::setCreatedBy);

            entityProductionDiscussionMember.setCreatedAt(Instant.now());

            entityProductionDiscussionMembers.add(entityProductionDiscussionMember);

        });

        entityProductionDiscussionMemberRepository.saveAll(entityProductionDiscussionMembers);

        entityProductionDiscussion.setDiscussionMessages(new ArrayList<>());

        return productionMapper.mapEntityDiscussionToDiscussion(entityProductionDiscussion, journalId);
    }


    public List<ProductionDiscussion> getDiscussions(Long journalId, Long submissionId) {

        return productionMapper.entityProductionDiscussionsToProductionDiscussions(entityProductionDiscussionRepository.getDiscussions(journalId, submissionId), journalId);
    }

    public ProductionDiscussion updateDiscussionStatus(Long journalId, Long submissionId, Long discussionId, ProductionDiscussionStatus productionDiscussionStatus) {

        Optional<EntityProductionDiscussion> entityProductionDiscussion = entityProductionDiscussionRepository.findDiscussion(discussionId, journalId, submissionId);

        if (entityProductionDiscussion.isPresent()) {

            EntityProductionDiscussion discussion = entityProductionDiscussion.get();

            discussion.setStatus(productionDiscussionStatus);

            entityProductionDiscussionRepository.save(discussion);

            return productionMapper.mapEntityDiscussionToDiscussion(discussion, journalId);
        } else {
            throw new BadRequestAlertException("Invalid Request.", "EntityProductionDiscussion", "invalid.request");
        }
    }

    public List<ProductionReadyFile> uploadProductionReadyFiles(Long journalId, Long submissionId, Map<String, MultipartFile> files) {

        List<EntityProductionReadyFile> entityProductionReadyFiles = new ArrayList<>();

        files.forEach((key, file) -> {

            String typeId = Arrays.stream(key.split("-")).collect(Collectors.toList()).get(0);

            EntityProductionReadyFile entityProductionReadyFile = new EntityProductionReadyFile();

            entityProductionReadyFile.setFile(file.getOriginalFilename());

            entitySubmissionRepository.findById(submissionId).ifPresent(entityProductionReadyFile::setSubmission);

            entityFileTypeRepository.findFileTypeByJournalIdAndFileTypeId(journalId, Long.valueOf(typeId)).ifPresent(entityProductionReadyFile::setJournalFileType);

            String filePath = fileStorageService.uploadToFinalDirectory(
                commonService.buildFileStoragePath(FilePartition.workflow, journalId,
                    submissionId, WorkflowStage.PRODUCTION, "production-ready", file.getOriginalFilename()), file);

            entityProductionReadyFile.setFilePath(filePath);

            entitySubmissionRepository.findById(submissionId).ifPresent(entityProductionReadyFile::setSubmission);

            entityProductionReadyFile.setCreatedAt(Instant.now());

            entityProductionReadyFile.setCreatedBy(userService.getCurrentUser());

            entityProductionReadyFile.setDeleted(false);

            entityUserRepository.findById(Objects.requireNonNull(SecurityUtils.getCurrentUserId())).ifPresent(entityProductionReadyFile::setCreatedBy);

            entityProductionReadyFiles.add(entityProductionReadyFile);
        });

        entityProductionReadyFileRepository.saveAll(entityProductionReadyFiles);

        return productionMapper.entityProductionReadyFilesToProductionReadyFiles(entityProductionReadyFiles, journalId);
    }

    public List<ProductionCompletedFile> uploadProductionCompletedFile(Long journalId, Long submissionId, Map<String, MultipartFile> files) {

        EntityJournal entityJournal = journalService.getEntityJournal(journalId);

        List<EntityProductionCompletedFile> entityProductionCompletedFiles = new ArrayList<>();

        files.forEach((key, file) -> {

            String typeId = Arrays.stream(key.split("-")).collect(Collectors.toList()).get(0);

            EntityProductionCompletedFile entityProductionCompletedFile = new EntityProductionCompletedFile();

            entityProductionCompletedFile.setFile(file.getOriginalFilename());

            entityFileTypeRepository.findFileTypeByJournalIdAndFileTypeId(journalId, Long.valueOf(typeId)).ifPresent(entityProductionCompletedFile::setJournalFileType);

            String filePath = fileStorageService.uploadToFinalDirectory(
                commonService.buildFileStoragePath(FilePartition.workflow, journalId,
                    submissionId, WorkflowStage.PRODUCTION, "production-completed", file.getOriginalFilename()), file);

            entityProductionCompletedFile.setFilePath(filePath);

            entitySubmissionRepository.findById(submissionId).ifPresent(entityProductionCompletedFile::setSubmission);

            entityProductionCompletedFile.setCreatedAt(Instant.now());

            entityUserRepository.findById(Objects.requireNonNull(SecurityUtils.getCurrentUserId())).ifPresent(entityProductionCompletedFile::setCreatedBy);

            entityProductionCompletedFiles.add(entityProductionCompletedFile);
        });

        entityProductionCompletedFileRepository.saveAll(entityProductionCompletedFiles);

        return productionMapper.entityProductionCompletedFilesToProductionCompletedFiles(entityProductionCompletedFiles, journalId);
    }

    public ProductionDiscussionMessage createDiscussionChatMessage(Long journalId, Long submissionId, Long discussionId, Map<String, MultipartFile> files, String message) {

        EntityProductionDiscussionMessage entityProductionDiscussionMessage = new EntityProductionDiscussionMessage();

        entityJournalRepository.findById(journalId).ifPresent(entityProductionDiscussionMessage::setJournal);
        entitySubmissionRepository.findById(submissionId).ifPresent(entityProductionDiscussionMessage::setSubmission);
        entityProductionDiscussionRepository.findById(discussionId).ifPresent(entityProductionDiscussionMessage::setDiscussion);
        entityProductionDiscussionMessage.setMessage(message);
        entityUserRepository.findById(Objects.requireNonNull(SecurityUtils.getCurrentUserId())).ifPresent(entityProductionDiscussionMessage::setCreatedBy);
        entityProductionDiscussionMessage.setUser(userService.getCurrentUser());
        entityProductionDiscussionMessage.setCreatedAt(Instant.now());
        entityProductionDiscussionMessage.setMessage(message);
        entityProductionDiscussionMessageRepository.save(entityProductionDiscussionMessage);

        List<EntityProductionDiscussionMessageFile> entityProductionDiscussionFiles = new ArrayList<>();

        files.forEach((key, file) -> {

            String typeId = Arrays.stream(key.split("-")).collect(Collectors.toList()).get(0);

            EntityProductionDiscussionMessageFile entityProductionDiscussionFile = new EntityProductionDiscussionMessageFile();

            entityProductionDiscussionFile.setFile(file.getOriginalFilename());


            entityFileTypeRepository.findFileTypeByJournalIdAndFileTypeId(journalId, Long.valueOf(typeId)).ifPresent(entityProductionDiscussionFile::setJournalFileType);

            entityProductionDiscussionFile.setDiscussionMessage(entityProductionDiscussionMessage);

            String filePath = fileStorageService.uploadToFinalDirectory(
                commonService.buildFileStoragePath(FilePartition.workflow, journalId,
                    submissionId, WorkflowStage.PRODUCTION, "production-discussion-message", file.getOriginalFilename()), file);

            entityProductionDiscussionFile.setFilePath(filePath);

            entityProductionDiscussionFile.setCreatedBy(userService.getCurrentUser());

            entityProductionDiscussionFile.setCreatedAt(Instant.now());

            entityUserRepository.findById(Objects.requireNonNull(SecurityUtils.getCurrentUserId())).ifPresent(entityProductionDiscussionFile::setCreatedBy);

            entityProductionDiscussionFiles.add(entityProductionDiscussionFile);

        });

        entityProductionDiscussionMessageFileRepository.saveAll(entityProductionDiscussionFiles);

        entityProductionDiscussionMessage.setFiles(entityProductionDiscussionFiles);

        return productionMapper.mapEntityProductionDiscussionMessageToProductionDiscussionMessage(entityProductionDiscussionMessage, journalId);
    }

    public List<ProductionDiscussionMessage> getDiscussionChatMessages(Long journalId, Long submissionId, Long discussionId) {


        return productionMapper.mapEntityProductionDiscussionMessagesToProductionDiscussionMessages(entityProductionDiscussionMessageRepository.getMessages(journalId, submissionId, discussionId), journalId);
    }

    public List<ProductionContributor> removeContributor(Long journalId, Long submissionId, String contributorId) {

        entityProductionContributorRepository.removeContributor(submissionId, contributorId);

        return this.getContributors(journalId, submissionId);
    }

    public void moveFromSubmissionToProduction(Long journalId, Long submissionId, List<Long> fileIds) {

        EntitySubmission entitySubmission = submissionService
            .getEntitySubmission(submissionId, journalId);

        submissionService.moveSubmissionToOtherWorkflow(userService.getCurrentUser(), entitySubmission, WorkflowStage.PRODUCTION);

        entitySubmissionFileRepository.findAllById(fileIds).forEach(entitySubmissionFile -> {

            EntityProductionReadyFile entityProductionReadyFile = new EntityProductionReadyFile();

            entitySubmissionRepository.findById(submissionId).ifPresent(entityProductionReadyFile::setSubmission);

            entityProductionReadyFile.setFile(entitySubmissionFile.getFile());

            entityProductionReadyFile.setFilePath(fileStorageService.moveFile(entitySubmissionFile.getFilePath(), commonService.buildFileStoragePath(FilePartition.workflow, journalId,
                submissionId, WorkflowStage.PRODUCTION, "production-ready", entitySubmissionFile.getFile())));

            entityProductionReadyFile.setJournalFileType(entitySubmissionFile.getJournalFileType());

            entityProductionReadyFile.setCreatedAt(Instant.now());

            entityProductionReadyFile.setCreatedBy(userService.getCurrentUser());

            entityProductionReadyFileRepository.save(entityProductionReadyFile);
        });

        entitySubmissionRepository.save(entitySubmission);
    }

    public void moveFromReviewToProduction(Long journalId, Long submissionId, List<Long> fileIds, Long reviewRoundId) {

        final EntityUser currentUser = userService.getCurrentUser();

        EntitySubmission entitySubmission = submissionService
            .getEntitySubmission(submissionId, journalId);

        submissionService.moveSubmissionToOtherWorkflow(currentUser, entitySubmission, WorkflowStage.PRODUCTION);

        entityReviewRoundFileRepository.findAllById(fileIds).forEach(entityReviewRoundFile -> {

            EntityProductionReadyFile entityProductionReadyFile = new EntityProductionReadyFile();

            entityProductionReadyFile.setFile(entityReviewRoundFile.getFile());

            entityProductionReadyFile.setFilePath(fileStorageService.moveFile(entityReviewRoundFile.getFilePath(), commonService.buildFileStoragePath(FilePartition.workflow, journalId,
                submissionId, WorkflowStage.PRODUCTION, "production-ready", entityReviewRoundFile.getFile())));

            entityProductionReadyFile.setJournalFileType(entityReviewRoundFile.getJournalFileType());

            entityProductionReadyFile.setCreatedAt(Instant.now());

            entityProductionReadyFile.setCreatedBy(userService.getCurrentUser());

            entityProductionReadyFileRepository.save(entityProductionReadyFile);
        });

        entitySubmissionRepository.save(entitySubmission);
    }

    public void deleteProductionReadyFile(Long journalId, Long submissionId, Long fileId) {


        entityProductionReadyFileRepository.deleteProductionReadyFile(submissionId, fileId);
    }

    public void moveFromCopyEditingToProduction(Long journalId, Long submissionId, List<Long> fileIds) {

        final EntityUser currentUser = userService.getCurrentUser();

        EntitySubmission entitySubmission = submissionService
            .getEntitySubmission(submissionId, journalId);

        submissionService.moveSubmissionToOtherWorkflow(currentUser, entitySubmission, WorkflowStage.PRODUCTION);

        List<EntityProductionReadyFile> entityProductionReadyFiles = new ArrayList<>();

        entityCopyEditedFileRepository.findAllById(fileIds).forEach(entityCopyEditedFile -> {

            EntityProductionReadyFile entityProductionReadyFile = new EntityProductionReadyFile();

            entityProductionReadyFile.setFile(entityCopyEditedFile.getFile());

            entityProductionReadyFile.setFilePath(fileStorageService.moveFile(entityCopyEditedFile.getFilePath(), commonService.buildFileStoragePath(FilePartition.workflow, journalId,
                submissionId, WorkflowStage.PRODUCTION, "production-ready", entityCopyEditedFile.getFile())));

            entityProductionReadyFile.setJournalFileType(entityCopyEditedFile.getJournalFileType());

            entityProductionReadyFile.setDeleted(false);

            entityProductionReadyFile.setCreatedAt(Instant.now());

            entityProductionReadyFile.setCreatedBy(userService.getCurrentUser());

            entityProductionReadyFiles.add(entityProductionReadyFile);
        });

        entityProductionReadyFileRepository.saveAll(entityProductionReadyFiles);

        entitySubmissionRepository.save(entitySubmission);
    }

    public File downloadCProductionReadyFilesAsZip(Long journalId, Long submissionId) throws IOException {

        List<String> filePaths = entityProductionReadyFileRepository.getProductionReadyFilePaths(journalId, submissionId);

        return fileStorageService.createZipFile(String.valueOf(journalId + submissionId), filePaths);
    }

    public File downloadCProductionCompletedFilesAsZip(Long journalId, Long submissionId) throws IOException {

        List<String> filePaths = entityProductionReadyFileRepository.getProductionCompletedFilePaths(journalId, submissionId);

        return fileStorageService.createZipFile(String.valueOf(journalId + submissionId), filePaths);
    }
}
