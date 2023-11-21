package com.kjms.service;

import com.kjms.domain.*;
import com.kjms.repository.*;
import com.kjms.security.BadRequestAlertException;
import com.kjms.security.SecurityUtils;
import com.kjms.service.dto.*;
import com.kjms.service.mail.MailService;
import com.kjms.service.mapper.CopyEditingMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class CopyEditingService {
    private final EntitySubmissionRepository entitySubmissionRepository;
    private final JournalService journalService;
    private final UserService userService;
    private final SubmissionService submissionService;
    private final EntityFileTypeRepository entityFileTypeRepository;
    private final WorkflowFileStorageService fileStorageService;
    private final CommonService commonService;
    private final EntityCopyEditingDraftFileRepository entityCopyEditingDraftFileRepository;
    private final CopyEditingMapper copyEditingMapper;
    private final EntityCopyEditedFileRepository entityCopyEditedFileRepository;
    private final EntityUserRepository entityUserRepository;
    private final EntityJournalRepository entityJournalRepository;
    private final EntityCopyEditingDiscussionRepository entityCopyEditingDiscussionRepository;
    private final EntityCopyEditingDiscussionFileRepository entityCopyEditingDiscussionFileRepository;
    private final EntityCopyEditingDiscussionMemberRepository entityCopyEditingDiscussionMemberRepository;
    private final EntityCopyEditingDiscussionMessageRepository entityCopyEditingDiscussionMessageRepository;
    private final EntityCopyEditingDiscussionMessageFileRepository entityCopyEditingDiscussionMessageFileRepository;
    private final EntityCopyEditingContributorRepository entityCopyEditingContributorRepository;
    private final EntitySubmissionFileRepository entitySubmissionFileRepository;
    private final EntityReviewRoundFileRepository entityReviewRoundFileRepository;

    public CopyEditingService(JournalService journalService,
                              UserService userService,
                              SubmissionService submissionService,
                              EntityFileTypeRepository entityFileTypeRepository,
                              WorkflowFileStorageService fileStorageService,
                              CommonService commonService,
                              EntityCopyEditingDraftFileRepository entityCopyEditingDraftFileRepository,
                              CopyEditingMapper copyEditingMapper,
                              EntityCopyEditedFileRepository entityCopyEditedFileRepository,
                              EntityUserRepository entityUserRepository,
                              EntitySubmissionRepository entitySubmissionRepository,
                              EntityJournalRepository entityJournalRepository,
                              EntityCopyEditingDiscussionRepository entityCopyEditingDiscussionRepository,
                              EntityCopyEditingDiscussionFileRepository entityCopyEditingDiscussionFileRepository,
                              EntityCopyEditingDiscussionMemberRepository entityCopyEditingDiscussionMemberRepository,
                              EntityCopyEditingDiscussionMessageRepository entityCopyEditingDiscussionMessageRepository,
                              EntityCopyEditingDiscussionMessageFileRepository entityCopyEditingDiscussionMessageFileRepository,
                              EntityCopyEditingContributorRepository entityCopyEditingContributorRepository,
                              EntitySubmissionFileRepository entitySubmissionFileRepository,
                              EntityReviewRoundFileRepository entityReviewRoundFileRepository) {
        this.journalService = journalService;
        this.userService = userService;
        this.submissionService = submissionService;
        this.entityFileTypeRepository = entityFileTypeRepository;
        this.fileStorageService = fileStorageService;
        this.commonService = commonService;
        this.entityCopyEditingDraftFileRepository = entityCopyEditingDraftFileRepository;
        this.copyEditingMapper = copyEditingMapper;
        this.entityCopyEditedFileRepository = entityCopyEditedFileRepository;
        this.entityUserRepository = entityUserRepository;
        this.entitySubmissionRepository = entitySubmissionRepository;
        this.entityJournalRepository = entityJournalRepository;
        this.entityCopyEditingDiscussionRepository = entityCopyEditingDiscussionRepository;
        this.entityCopyEditingDiscussionFileRepository = entityCopyEditingDiscussionFileRepository;
        this.entityCopyEditingDiscussionMemberRepository = entityCopyEditingDiscussionMemberRepository;
        this.entityCopyEditingDiscussionMessageRepository = entityCopyEditingDiscussionMessageRepository;
        this.entityCopyEditingDiscussionMessageFileRepository = entityCopyEditingDiscussionMessageFileRepository;
        this.entityCopyEditingContributorRepository = entityCopyEditingContributorRepository;
        this.entitySubmissionFileRepository = entitySubmissionFileRepository;
        this.entityReviewRoundFileRepository = entityReviewRoundFileRepository;
    }

    public void moveFromSubmissionToCopyEditing(final Long journalId, final Long submissionId, List<Long> fileIds) {

        EntitySubmission entitySubmission = submissionService
            .getEntitySubmission(submissionId, journalId);

        submissionService.moveSubmissionToOtherWorkflow(userService.getCurrentUser(), entitySubmission, WorkflowStage.COPY_EDITING);

        entitySubmissionFileRepository.findAllById(fileIds).forEach(entitySubmissionFile -> {

            EntityCopyEditingDraftFile entityCopyEditingDraftFile = new EntityCopyEditingDraftFile();

            entityCopyEditingDraftFile.setFile(entitySubmissionFile.getFile());

            entityCopyEditingDraftFile.setFilePath(fileStorageService.moveFile(entitySubmissionFile.getFilePath(), commonService.buildFileStoragePath(FilePartition.workflow, journalId,
                submissionId, WorkflowStage.COPY_EDITING, "copy-editing-draft", entitySubmissionFile.getFile())));

            entityCopyEditingDraftFile.setJournalFileType(entitySubmissionFile.getJournalFileType());

            entityJournalRepository.findById(journalId).ifPresent(entityCopyEditingDraftFile::setJournal);

            entityCopyEditingDraftFile.setSubmission(entitySubmission);

            entityCopyEditingDraftFile.setCreatedAt(Instant.now());

            entityCopyEditingDraftFile.setCreatedBy(userService.getCurrentUser());

            entityCopyEditingDraftFileRepository.save(entityCopyEditingDraftFile);
        });

        entitySubmissionRepository.save(entitySubmission);
    }

    public void moveFromReviewToCopyEditing(Long journalId, Long submissionId, List<Long> fileIds, Long reviewRoundId) {

        final EntityUser currentUser = userService.getCurrentUser();

        EntitySubmission entitySubmission = submissionService
            .getEntitySubmission(submissionId, journalId);

        submissionService.moveSubmissionToOtherWorkflow(currentUser, entitySubmission, WorkflowStage.COPY_EDITING);

        entityReviewRoundFileRepository.findAllById(fileIds).forEach(entityReviewRoundFile -> {

            EntityCopyEditingDraftFile entityCopyEditingDraftFile = new EntityCopyEditingDraftFile();

            entityCopyEditingDraftFile.setFile(entityReviewRoundFile.getFile());

            entityJournalRepository.findById(journalId).ifPresent(entityCopyEditingDraftFile::setJournal);

            entitySubmissionRepository.findById(submissionId).ifPresent(entityCopyEditingDraftFile::setSubmission);

            entityCopyEditingDraftFile.setFilePath(fileStorageService.moveFile(entityReviewRoundFile.getFilePath(), commonService.buildFileStoragePath(FilePartition.workflow, journalId,
                submissionId, WorkflowStage.COPY_EDITING, "copy-editing-draft", entityReviewRoundFile.getFile())));

            entityCopyEditingDraftFile.setJournalFileType(entityReviewRoundFile.getJournalFileType());

            entityCopyEditingDraftFile.setCreatedAt(Instant.now());

            entityCopyEditingDraftFile.setCreatedBy(userService.getCurrentUser());

            entityCopyEditingDraftFileRepository.save(entityCopyEditingDraftFile);
        });

        entitySubmissionRepository.save(entitySubmission);
    }

    public List<CopyEditingDraftFile> uploadCopyEditingDraftFiles(Long journalId, Long submissionId,
                                                                  Map<String, MultipartFile> files) {
        EntityJournal entityJournal = journalService.getEntityJournal(journalId);

        List<EntityCopyEditingDraftFile> entityCopyEditingDraftFiles = new ArrayList<>();

        files.forEach((key, file) -> {

            String typeId = Arrays.stream(key.split("-")).collect(Collectors.toList()).get(0);

            EntityCopyEditingDraftFile entityCopyEditingDraftFile = new EntityCopyEditingDraftFile();

            entityCopyEditingDraftFile.setFile(file.getOriginalFilename());

            String filePath = fileStorageService.uploadToFinalDirectory(
                commonService.buildFileStoragePath(FilePartition.workflow, journalId,
                    submissionId, WorkflowStage.COPY_EDITING, "copy-editing-draft", file.getOriginalFilename()), file);

            entityCopyEditingDraftFile.setFilePath(filePath);

            entitySubmissionRepository.findById(submissionId).ifPresent(entityCopyEditingDraftFile::setSubmission);

            entityCopyEditingDraftFile.setJournal(entityJournal);

            entityCopyEditingDraftFile.setCreatedBy(userService.getCurrentUser());

            entityCopyEditingDraftFile.setCreatedAt(Instant.now());

            entityFileTypeRepository.findFileTypeByJournalIdAndFileTypeId(journalId, Long.valueOf(typeId)).ifPresent(entityCopyEditingDraftFile::setJournalFileType);

            entityCopyEditingDraftFiles.add(entityCopyEditingDraftFile);

        });

        entityCopyEditingDraftFileRepository.saveAll(entityCopyEditingDraftFiles);

        return copyEditingMapper.entityCopyEditingDraftFilesToCopyEditingDraftFiles(entityCopyEditingDraftFiles);
    }

    public List<CopyEditedFile> uploadCopyEditedFiles(Long journalId, Long submissionId, Map<String, MultipartFile> files) {

        EntityJournal entityJournal = journalService.getEntityJournal(journalId);

        List<EntityCopyEditedFile> entityCopyEditedFiles = new ArrayList<>();

        files.forEach((key, file) -> {

            String typeId = Arrays.stream(key.split("-")).collect(Collectors.toList()).get(0);

            EntityCopyEditedFile entityCopyEditedFile = new EntityCopyEditedFile();

            entityCopyEditedFile.setFile(file.getOriginalFilename());

            String filePath = fileStorageService.uploadToFinalDirectory(
                commonService.buildFileStoragePath(FilePartition.workflow, journalId,
                    submissionId, WorkflowStage.COPY_EDITING, "copy-editing-draft", file.getOriginalFilename()), file);

            entityCopyEditedFile.setFilePath(filePath);

            entitySubmissionRepository.findById(submissionId).ifPresent(entityCopyEditedFile::setSubmission);

            entityCopyEditedFile.setJournal(entityJournal);

            entityCopyEditedFile.setCreatedBy(userService.getCurrentUser());

            entityCopyEditedFile.setCreatedAt(Instant.now());

            entityFileTypeRepository.findFileTypeByJournalIdAndFileTypeId(journalId, Long.valueOf(typeId)).ifPresent(entityCopyEditedFile::setJournalFileType);

            entityCopyEditedFiles.add(entityCopyEditedFile);
        });

        entityCopyEditedFileRepository.saveAll(entityCopyEditedFiles);

        return copyEditingMapper.entityCopyEditedFilesToCopyEditedFiles(entityCopyEditedFiles);
    }


    public CopyEditingDiscussion createDiscussion(Long journalId, Long submissionId, String topic, String description, Map<String, MultipartFile> files, List<String> members) {

        EntityCopyEditingDiscussion entityCopyEditingDiscussion = new EntityCopyEditingDiscussion();

        entityCopyEditingDiscussion.setTopic(topic);

        entityCopyEditingDiscussion.setDescription(description);

        entityCopyEditingDiscussion.setStatus(CopyEditingDiscussionStatus.open);

        entityUserRepository.findById(Objects.requireNonNull(SecurityUtils.getCurrentUserId())).ifPresent(entityCopyEditingDiscussion::setUser);

        entitySubmissionRepository.findById(submissionId).ifPresent(entityCopyEditingDiscussion::setSubmission);

        entityJournalRepository.findById(journalId).ifPresent(entityCopyEditingDiscussion::setJournal);

        entityUserRepository.findById(Objects.requireNonNull(SecurityUtils.getCurrentUserId())).
            ifPresent(entityCopyEditingDiscussion::setCreatedBy);

        entityCopyEditingDiscussion.setCreatedAt(Instant.now());

        entityCopyEditingDiscussionRepository.save(entityCopyEditingDiscussion);

        List<EntityCopyEditingDiscussionFile> entityCopyEditingDiscussionFiles = new ArrayList<>();

        files.forEach((key, file) -> {

            String typeId = Arrays.stream(key.split("-")).collect(Collectors.toList()).get(0);

            String filePath = fileStorageService.uploadToFinalDirectory(
                commonService.buildFileStoragePath(FilePartition.workflow, journalId,
                    submissionId, WorkflowStage.COPY_EDITING, "discussion", file.getOriginalFilename()), file);

            EntityCopyEditingDiscussionFile entityCopyEditingDiscussionFile = new EntityCopyEditingDiscussionFile();

            entityCopyEditingDiscussionFile.setFile(file.getOriginalFilename());

            entityCopyEditingDiscussionFile.setFilePath(filePath);

            entityFileTypeRepository.findFileTypeByJournalIdAndFileTypeId(journalId, Long.valueOf(typeId)).ifPresent(entityCopyEditingDiscussionFile::setJournalFileType);

            entityCopyEditingDiscussionFile.setCopyEditingDiscussion(entityCopyEditingDiscussion);

            entityCopyEditingDiscussionFile.setCreatedAt(Instant.now());

            entityCopyEditingDiscussionFile.setCreatedBy(userService.getCurrentUser());

            entityCopyEditingDiscussionFiles.add(entityCopyEditingDiscussionFile);

        });

        entityCopyEditingDiscussionFileRepository.saveAll(entityCopyEditingDiscussionFiles);

        entityCopyEditingDiscussion.setFiles(entityCopyEditingDiscussionFiles);

        entityCopyEditingDiscussion.setDiscussionMessages(new ArrayList<>());

        List<EntityCopyEditingDiscussionMember> entityCopyEditingDiscussionMembers = new ArrayList<>();

        members.forEach(member -> {

            EntityCopyEditingDiscussionMember entityCopyEditingDiscussionMember = new EntityCopyEditingDiscussionMember();

            entityUserRepository.findById(member).ifPresent(entityCopyEditingDiscussionMember::setUser);

            entityCopyEditingDiscussionMember.setDiscussion(entityCopyEditingDiscussion);

            entityUserRepository.findById(SecurityUtils.getCurrentUserId()).ifPresent(entityCopyEditingDiscussionMember::setCreatedBy);

            entityCopyEditingDiscussionMember.setCreatedAt(Instant.now());

            entityCopyEditingDiscussionMembers.add(entityCopyEditingDiscussionMember);

        });

        entityCopyEditingDiscussionMemberRepository.saveAll(entityCopyEditingDiscussionMembers);

        return copyEditingMapper.mapEntityCopyEditingDiscussionToCopyEditingDiscussion(entityCopyEditingDiscussion, journalId);
    }

    public CopyEditingDiscussion updateDiscussionStatus(Long journalId, Long submissionId, Long discussionId, CopyEditingDiscussionStatus copyEditingDiscussionStatus) {

        Optional<EntityCopyEditingDiscussion> entityCopyEditingDiscussionOptional = entityCopyEditingDiscussionRepository.findDiscussion(discussionId, journalId, submissionId);

        if (entityCopyEditingDiscussionOptional.isPresent()) {

            EntityCopyEditingDiscussion entityCopyEditingDiscussion = entityCopyEditingDiscussionOptional.get();

            entityCopyEditingDiscussion.setStatus(copyEditingDiscussionStatus);

            entityCopyEditingDiscussionRepository.save(entityCopyEditingDiscussion);

            return copyEditingMapper.mapEntityCopyEditingDiscussionToCopyEditingDiscussion(entityCopyEditingDiscussion, journalId);

        } else {

            throw new BadRequestAlertException("Invalid Request.", "EntityCopyEditingDiscussion", "invalid.request");
        }
    }

    public List<CopyEditingDiscussion> getDiscussions(Long journalId, Long submissionId) {

        return copyEditingMapper.entityCopyEditingDiscussionsToCopyEditingDiscussions(entityCopyEditingDiscussionRepository.getDiscussions(journalId, submissionId), journalId);

    }

    public CopyEditingDiscussionMessage createDiscussionChatMessage(Long journalId, Long submissionId, Long discussionId, Map<String, MultipartFile> files, String message) {

        EntityCopyEditingDiscussionMessage entityCopyEditingDiscussionMessage = new EntityCopyEditingDiscussionMessage();

        entityJournalRepository.findById(journalId).ifPresent(entityCopyEditingDiscussionMessage::setJournal);

        entitySubmissionRepository.findById(submissionId).ifPresent(entityCopyEditingDiscussionMessage::setSubmission);

        entityCopyEditingDiscussionRepository.findById(discussionId).ifPresent(entityCopyEditingDiscussionMessage::setDiscussion);

        entityUserRepository.findById(Objects.requireNonNull(SecurityUtils.getCurrentUserId())).ifPresent(entityCopyEditingDiscussionMessage::setCreatedBy);

        entityCopyEditingDiscussionMessage.setUser(userService.getCurrentUser());

        entityCopyEditingDiscussionMessage.setCreatedAt(Instant.now());

        entityCopyEditingDiscussionMessage.setMessage(message);

        entityCopyEditingDiscussionMessageRepository.save(entityCopyEditingDiscussionMessage);

        List<EntityCopyEditingDiscussionMessageFile> entityCopyEditingDiscussionMessageFiles = new ArrayList<>();

        files.forEach((key, file) -> {

            String typeId = Arrays.stream(key.split("-")).collect(Collectors.toList()).get(0);

            EntityCopyEditingDiscussionMessageFile entityCopyEditingDiscussionMessageFile = new EntityCopyEditingDiscussionMessageFile();

            entityCopyEditingDiscussionMessageFile.setFile(file.getOriginalFilename());

            entityFileTypeRepository.findFileTypeByJournalIdAndFileTypeId(journalId, Long.valueOf(typeId)).ifPresent(entityCopyEditingDiscussionMessageFile::setJournalFileType);

            entityCopyEditingDiscussionMessageFile.setDiscussionMessage(entityCopyEditingDiscussionMessage);

            entityCopyEditingDiscussionMessageFile.setCreatedAt(Instant.now());

            String filePath = fileStorageService.uploadToFinalDirectory(
                commonService.buildFileStoragePath(FilePartition.workflow, journalId,
                    submissionId, WorkflowStage.COPY_EDITING, "copy-editing-discussion-chat", file.getOriginalFilename()), file);

            entityCopyEditingDiscussionMessageFile.setFilePath(filePath);

            entityCopyEditingDiscussionMessageFile.setCreatedAt(Instant.now());

            entityCopyEditingDiscussionMessageFile.setCreatedBy(userService.getCurrentUser());

            entityUserRepository.findById(Objects.requireNonNull(SecurityUtils.getCurrentUserId())).ifPresent(entityCopyEditingDiscussionMessageFile::setCreatedBy);

            entityCopyEditingDiscussionMessageFiles.add(entityCopyEditingDiscussionMessageFile);

        });

        entityCopyEditingDiscussionMessageFileRepository.saveAll(entityCopyEditingDiscussionMessageFiles);

        entityCopyEditingDiscussionMessage.setFiles(entityCopyEditingDiscussionMessageFiles);

        return copyEditingMapper.mapEntityCopyEditingDiscussionMessageToCopyEditingDiscussionMessage(entityCopyEditingDiscussionMessage);
    }

    public List<CopyEditingDiscussionMessage> getDiscussionChatMessages(Long journalId, Long submissionId, Long discussionId) {

        return copyEditingMapper.mapEntityCopyEditingDiscussionMessagesToCopyEditingDiscussionMessages(entityCopyEditingDiscussionMessageRepository.getMessages(journalId, submissionId, discussionId));
    }

    public CopyEditingContributor getContributor(Long journalId, Long submissionId, String userId) {

        Optional<EntityCopyEditingContributor> entityUser = entityCopyEditingContributorRepository.findContributorUser(journalId, submissionId, userId);

        return entityUser.map(copyEditingMapper::entityCopyEditingContributorToCopyEditingContributor).orElse(null);

    }

    public List<CopyEditingContributor> addContributor(Long journalId, Long submissionId, List<String> userIds, String description) {

        List<EntityCopyEditingContributor> entityCopyEditingContributors = new ArrayList<>();

        Optional<EntitySubmission> submissionArticle = entitySubmissionRepository.findById(submissionId);

        if (submissionArticle.isEmpty()) {

            throw new RuntimeException();
        }

        EntitySubmission entitySubmission = submissionArticle.get();

        for (String userId : userIds) {

            if (getContributor(journalId, submissionId, userId) == null) {

                EntityCopyEditingContributor entityCopyEditingContributor = new EntityCopyEditingContributor();

                Optional<EntityUser> entityUser = entityUserRepository.findById(userId);

                entityUser.ifPresent(entityCopyEditingContributor::setUser);

                entityCopyEditingContributor.setSubmission(entitySubmission);

                entityUserRepository.findById(Objects.requireNonNull(SecurityUtils.getCurrentUserId())).ifPresent(entityCopyEditingContributor::setCreatedBy);

                entityCopyEditingContributor.setCreatedAt(Instant.now());

                entityCopyEditingContributor.setDeleted(false);

                entityCopyEditingContributors.add(entityCopyEditingContributor);
            }

            entityCopyEditingContributorRepository.saveAll(entityCopyEditingContributors);

            sendMailToCopyEditingContributor(entityCopyEditingContributors, description, journalId, entitySubmission.getTitle());
        }
        return copyEditingMapper.mapEntityCopyEditingContributorsToCopyEditingContributors(entityCopyEditingContributors);
    }

    private void sendMailToCopyEditingContributor(List<EntityCopyEditingContributor> entityCopyEditingContributors, String description, Long journalId, String submissionTitle) {

        //// TODO: 22-10-2023 @suren Mail to be implemented
    }

    public List<CopyEditingContributor> removeContributor(Long journalId, Long submissionId, String contributorId) {

        entityCopyEditingContributorRepository.removeContributor(submissionId, contributorId);

        return this.getContributors(journalId, submissionId);
    }

    public List<CopyEditingContributor> getContributors(Long journalId, Long submissionId) {

        return copyEditingMapper.mapEntityCopyEditingContributorsToCopyEditingContributors(entityCopyEditingContributorRepository.getContributors(journalId, submissionId));
    }

    public List<CopyEditingDraftFile> getDraftFiles(Long journalId, Long submissionId) {

        return copyEditingMapper.mapEntityCopyEditingDraftFilesToCopyEditingDraftFile(entityCopyEditingDraftFileRepository.getDraftFiles(journalId, submissionId));
    }

    public List<CopyEditedFile> getCopyEditedFiles(Long journalId, Long submissionId) {

        return copyEditingMapper.mapEntityCopyEditedFilesToCopyEditedFile(entityCopyEditedFileRepository.getCopyEditedFiles(journalId, submissionId));
    }

    public CopyEditing getCopyEditingDetail(Long journalId, Long submissionId) {

        CopyEditing copyEditing = new CopyEditing();

        copyEditing.setCopyEditingContributors(getContributors(journalId, submissionId));

        copyEditing.setCopyEditingDiscussions(getDiscussions(journalId, submissionId));

        copyEditing.setCopyEditingDraftFiles(getDraftFiles(journalId, submissionId));

        copyEditing.setCopyEditedFiles(getCopyEditedFiles(journalId, submissionId));

        return copyEditing;
    }


    public File downloadCopyEditedFilesAsZip(Long journalId, Long submissionId) throws IOException {

        List<String> filePaths = entityCopyEditedFileRepository.getCopyEditedFilePaths(journalId, submissionId);

        return fileStorageService.createZipFile(String.valueOf(journalId + submissionId), filePaths);
    }

    public File downloadCopyEditingDraftFilesAsZip(Long journalId, Long submissionId) throws IOException {

        List<String> filePaths = entityCopyEditingDraftFileRepository.getCopyEditingDraftFilePaths(journalId, submissionId);

        return fileStorageService.createZipFile(String.valueOf(journalId + submissionId), filePaths);
    }
}
