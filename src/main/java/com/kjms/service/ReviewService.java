package com.kjms.service;


import com.kjms.domain.*;
import com.kjms.repository.*;
import com.kjms.security.BadRequestAlertException;
import com.kjms.security.SecurityUtils;
import com.kjms.service.dto.*;
import com.kjms.service.mail.MailService;
import com.kjms.service.mapper.ReviewMapper;
import com.kjms.service.mapper.SubmissionMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public class ReviewService {


    private final JournalService journalService;
    private final SubmissionService submissionService;
    private final EntitySubmissionRepository submissionRepository;
    private final UserService userService;
    private final EntityReviewRoundRepository entityReviewRoundRepository;
    private final EntityJournalRepository entityJournalRepository;
    private final EntityFileTypeRepository entityFileTypeRepository;
    private final WorkflowFileStorageService fileStorageService;
    private final CommonService commonService;
    private final ReviewMapper reviewMapper;
    private final EntityReviewRoundFileRepository entityReviewRoundFileRepository;
    private final EntityUserRepository entityUserRepository;
    private final EntityReviewRoundDiscussionRepository entityReviewRoundDiscussionRepository;
    private final EntityReviewRoundDiscussionFileRepository entityReviewRoundDiscussionFileRepository;
    private final EntityReviewRoundDiscussionMemberRepository entityReviewRoundDiscussionMemberRepository;
    private final EntityReviewRoundDiscussionMessageRepository entityReviewRoundDiscussionMessageRepository;
    private final EntityReviewRoundDiscussionMessageFileRepository entityReviewRoundDiscussionMessageFileRepository;
    private final EntityReviewRoundContributorRepository entityReviewRoundContributorRepository;

    private final MailService mailService;
    private final EntityReviewRoundReviewRepository entityReviewRoundReviewRepository;
    private final EntityReviewRoundReviewerFileRepository entityReviewRoundReviewerFileRepository;
    private final EntityReviewerFileRepository entityReviewerFileRepository;
    private final EntityJournalReviewerRepository entityJournalReviewerRepository;

    private final SubmissionMapper submissionMapper;
    private final EntitySubmissionFileRepository entitySubmissionFileRepository;
    private final EntityReviewerReplyFileRepository entityReviewerReplyFileRepository;
    private final EntityReviewRoundReviewerDiscussionRepository entityReviewRoundReviewerDiscussionRepository;
    private final EntityReviewRoundReviewerDiscussionFileRepository entityReviewRoundReviewerDiscussionFileRepository;
    private final EntityReviewRoundReviewerDiscussionMemberRepository entityReviewRoundReviewerDiscussionMemberRepository;
    private final EntityReviewRoundReviewerDiscussionMessageRepository entityReviewRoundReviewerDiscussionMessageRepository;
    private final EntityReviewRoundReviewerDiscussionMessageFileRepository entityReviewRoundReviewerDiscussionMessageFileRepository;
    private final EntityReviewRoundReviewerContributorRepository entityReviewRoundReviewerContributorRepository;

    public ReviewService(JournalService journalService, SubmissionService submissionService, EntitySubmissionRepository submissionRepository, UserService userService,
                         EntityReviewRoundRepository entityReviewRoundRepository,
                         EntityJournalRepository entityJournalRepository, EntityFileTypeRepository entityFileTypeRepository, WorkflowFileStorageService fileStorageService, CommonService commonService, ReviewMapper reviewMapper,
                         EntityReviewRoundFileRepository entityReviewRoundFileRepository,
                         EntityUserRepository entityUserRepository,
                         EntityReviewRoundDiscussionRepository entityReviewRoundDiscussionRepository,
                         EntityReviewRoundDiscussionFileRepository entityReviewRoundDiscussionFileRepository,
                         EntityReviewRoundDiscussionMemberRepository entityReviewRoundDiscussionMemberRepository,
                         EntityReviewRoundDiscussionMessageRepository entityReviewRoundDiscussionMessageRepository,
                         EntityReviewRoundDiscussionMessageFileRepository entityReviewRoundDiscussionMessageFileRepository,
                         EntityReviewRoundContributorRepository entityReviewRoundContributorRepository, MailService mailService,
                         EntityReviewRoundReviewRepository entityReviewRoundReviewRepository,
                         EntityReviewRoundReviewerFileRepository entityReviewRoundReviewerFileRepository,
                         EntityReviewerFileRepository entityReviewerFileRepository,
                         EntityJournalReviewerRepository entityJournalReviewerRepository,
                         EntityJournalAuthorRepository entityJournalAuthorRepository, SubmissionMapper submissionMapper,
                         EntitySubmissionFileRepository entitySubmissionFileRepository,
                         EntityReviewerReplyFileRepository entityReviewerReplyFileRepository,
                         EntityReviewRoundReviewerDiscussionRepository entityReviewRoundReviewerDiscussionRepository,
                         EntityReviewRoundReviewerDiscussionFileRepository entityReviewRoundReviewerDiscussionFileRepository,
                         EntityReviewRoundReviewerDiscussionMemberRepository entityReviewRoundReviewerDiscussionMemberRepository,
                         EntityReviewRoundReviewerDiscussionMessageRepository entityReviewRoundReviewerDiscussionMessageRepository,
                         EntityReviewRoundReviewerDiscussionMessageFileRepository entityReviewRoundReviewerDiscussionMessageFileRepository,
                         EntityReviewRoundReviewerContributorRepository entityReviewRoundReviewerContributorRepository) {
        this.journalService = journalService;
        this.submissionService = submissionService;
        this.submissionRepository = submissionRepository;
        this.userService = userService;
        this.entityReviewRoundRepository = entityReviewRoundRepository;
        this.entityJournalRepository = entityJournalRepository;
        this.entityFileTypeRepository = entityFileTypeRepository;
        this.fileStorageService = fileStorageService;
        this.commonService = commonService;
        this.reviewMapper = reviewMapper;
        this.entityReviewRoundFileRepository = entityReviewRoundFileRepository;
        this.entityUserRepository = entityUserRepository;
        this.entityReviewRoundDiscussionRepository = entityReviewRoundDiscussionRepository;
        this.entityReviewRoundDiscussionFileRepository = entityReviewRoundDiscussionFileRepository;
        this.entityReviewRoundDiscussionMemberRepository = entityReviewRoundDiscussionMemberRepository;
        this.entityReviewRoundDiscussionMessageRepository = entityReviewRoundDiscussionMessageRepository;
        this.entityReviewRoundDiscussionMessageFileRepository = entityReviewRoundDiscussionMessageFileRepository;
        this.entityReviewRoundContributorRepository = entityReviewRoundContributorRepository;
        this.mailService = mailService;
        this.entityReviewRoundReviewRepository = entityReviewRoundReviewRepository;
        this.entityReviewRoundReviewerFileRepository = entityReviewRoundReviewerFileRepository;
        this.entityReviewerFileRepository = entityReviewerFileRepository;
        this.entityJournalReviewerRepository = entityJournalReviewerRepository;
        this.submissionMapper = submissionMapper;
        this.entitySubmissionFileRepository = entitySubmissionFileRepository;
        this.entityReviewerReplyFileRepository = entityReviewerReplyFileRepository;
        this.entityReviewRoundReviewerDiscussionRepository = entityReviewRoundReviewerDiscussionRepository;
        this.entityReviewRoundReviewerDiscussionFileRepository = entityReviewRoundReviewerDiscussionFileRepository;
        this.entityReviewRoundReviewerDiscussionMemberRepository = entityReviewRoundReviewerDiscussionMemberRepository;
        this.entityReviewRoundReviewerDiscussionMessageRepository = entityReviewRoundReviewerDiscussionMessageRepository;
        this.entityReviewRoundReviewerDiscussionMessageFileRepository = entityReviewRoundReviewerDiscussionMessageFileRepository;
        this.entityReviewRoundReviewerContributorRepository = entityReviewRoundReviewerContributorRepository;
    }

    public void moveToReview(Long journalId, Long submissionId, List<Long> fileIds) {

        final EntityUser currentUser = userService.getCurrentUser();

        EntitySubmission entitySubmission = submissionService
            .getEntitySubmission(submissionId, journalId);

        submissionService.moveSubmissionToOtherWorkflow(currentUser, entitySubmission, WorkflowStage.REVIEW);

        ReviewRound reviewRound = createReviewRound(journalId, submissionId);

        moveSubmissionFilesToReviewRoundFile(journalId, submissionId, fileIds, reviewRound);

        submissionRepository.save(entitySubmission);
    }

    private void moveSubmissionFilesToReviewRoundFile(Long journalId, Long submissionId, List<Long> fileIds, ReviewRound reviewRound) {

        entitySubmissionFileRepository.findAllById(fileIds).forEach(entitySubmissionFile ->
            entityReviewRoundRepository.findById(reviewRound.getId()).ifPresent(entityReviewRound -> {

                EntityReviewRoundFile entityReviewRoundFile = new EntityReviewRoundFile();
                entityReviewRoundFile.setReviewRound(entityReviewRound);
                entityReviewRoundFile.setFile(entitySubmissionFile.getFile());
                entityReviewRoundFile.setFilePath(fileStorageService.moveFile(entitySubmissionFile.getFilePath(), commonService.buildFileStoragePath(FilePartition.workflow, journalId,
                    submissionId, WorkflowStage.REVIEW, "review-round", entitySubmissionFile.getFile())));
                entityReviewRoundFile.setJournalFileType(entitySubmissionFile.getJournalFileType());
                entityReviewRoundFile.setCreatedAt(Instant.now());
                entityReviewRoundFile.setCreatedBy(userService.getCurrentUser());

                entityReviewRoundFileRepository.save(entityReviewRoundFile);
            }));

    }


    public ReviewRound createReviewRound(Long journalId, Long submissionId) {

        Optional<Integer> lastReviewRound = entityReviewRoundRepository.getReviewRoundNumber(journalId, submissionId).stream().findFirst();

        EntityReviewRound entityReviewRound = new EntityReviewRound();

        if (lastReviewRound.isEmpty()) {

            entityReviewRound.setRound(1);

        } else {

            entityReviewRound.setRound(lastReviewRound.get() + 1);

        }

        entityReviewRound.setName("Round " + entityReviewRound.getRound());

        entityJournalRepository.findById(journalId).ifPresent(entityReviewRound::setJournal);

        submissionRepository.findById(submissionId).ifPresent(entityReviewRound::setSubmission);

        entityReviewRound.setCreatedBy(userService.getCurrentUser());

        entityReviewRound.setDeleted(false);

        entityReviewRound.setCreatedAt(Instant.now());

        entityReviewRoundRepository.save(entityReviewRound);

        return reviewMapper.entityReviewRoundToReviewRound(entityReviewRound);
    }

    public List<ReviewRound> getReviews(Long journalId, Long submissionId) {

        return reviewMapper.entityReviewRoundsToReviewRounds(entityReviewRoundRepository.getReviewRounds(journalId, submissionId));
    }

    public List<ReviewRoundFile> uploadReviewRoundFile(Long journalId, Long submissionId, Long reviewRoundId, Map<String, MultipartFile> files) {

        List<EntityReviewRoundFile> entityReviewRoundFiles = new ArrayList<>();

        files.forEach((key, file) -> {

            String typeId = Arrays.stream(key.split("-")).collect(Collectors.toList()).get(0);

            EntityReviewRoundFile entityReviewRoundFile = new EntityReviewRoundFile();

            entityReviewRoundFile.setFile(file.getOriginalFilename());

            entityReviewRoundRepository.findById(reviewRoundId).ifPresent(entityReviewRoundFile::setReviewRound);

            String filePath = fileStorageService.uploadToFinalDirectory(
                commonService.buildFileStoragePath(FilePartition.workflow, journalId,
                    submissionId, WorkflowStage.REVIEW, "review-round", file.getOriginalFilename()), file);

            entityReviewRoundFile.setFilePath(filePath);

            entityReviewRoundFile.setCreatedBy(userService.getCurrentUser());

            entityReviewRoundFile.setCreatedAt(Instant.now());

            entityFileTypeRepository.findFileTypeByJournalIdAndFileTypeId(journalId, Long.valueOf(typeId)).ifPresent(entityReviewRoundFile::setJournalFileType);

            entityReviewRoundFiles.add(entityReviewRoundFile);

        });

        entityReviewRoundFileRepository.saveAll(entityReviewRoundFiles);

        return reviewMapper.entityReviewRoundFilesToReviewRoundFiles(entityReviewRoundFiles);
    }

    public ReviewRound getReviewRound(Long journalId, Long submissionId, Long reviewRoundId) {

        ReviewRound reviewRoundDetail = new ReviewRound();

        EntityReviewRound entityReviewRound = entityReviewRoundRepository.getReviewRound(journalId, submissionId, reviewRoundId);

        if (entityReviewRound != null) {

            reviewRoundDetail.setId(entityReviewRound.getId());

            reviewRoundDetail.setName(entityReviewRound.getName());

            reviewRoundDetail.setRound(entityReviewRound.getRound());

            reviewRoundDetail.setReviewRoundDiscussions(getReviewRoundDiscussions(reviewRoundId));

            reviewRoundDetail.setReviewRoundFiles(getReviewRoundFiles(reviewRoundId, journalId));

            reviewRoundDetail.setReviewRoundContributors(getReviewRoundContributors(reviewRoundId));

            reviewRoundDetail.setReviewRoundReviewerReviews(getReviewers(reviewRoundId));
        }

        return reviewRoundDetail;
    }

    public ReviewRoundDiscussion createReviewRoundDiscussion(Long journalId, Long submissionId, Long reviewRoundId, String topic, String description, Map<String, MultipartFile> files, List<String> members) {

        EntityReviewRoundDiscussion entityReviewRoundDiscussion = new EntityReviewRoundDiscussion();

        entityReviewRoundDiscussion.setTopic(topic);

        entityReviewRoundDiscussion.setDescription(description);

        entityReviewRoundDiscussion.setStatus(ReviewRoundDiscussionStatus.open);

        entityReviewRoundRepository.findById(reviewRoundId).ifPresent(entityReviewRoundDiscussion::setReviewRound);

        entityUserRepository.findById(Objects.requireNonNull(SecurityUtils.getCurrentUserId())).ifPresent(entityReviewRoundDiscussion::setUser);

        entityUserRepository.findById(Objects.requireNonNull(SecurityUtils.getCurrentUserId())).
            ifPresent(entityReviewRoundDiscussion::setCreatedBy);

        entityReviewRoundDiscussion.setCreatedAt(Instant.now());

        entityReviewRoundDiscussionRepository.save(entityReviewRoundDiscussion);

        List<EntityReviewRoundDiscussionFile> entityReviewRoundDiscussionFiles = new ArrayList<>();

        files.forEach((key, file) -> {

            String typeId = Arrays.stream(key.split("-")).collect(Collectors.toList()).get(0);

            String filePath = fileStorageService.uploadToFinalDirectory(
                commonService.buildFileStoragePath(FilePartition.workflow, journalId,
                    submissionId, WorkflowStage.REVIEW, "discussion", file.getOriginalFilename()), file);

            EntityReviewRoundDiscussionFile entityReviewRoundDiscussionFile = new EntityReviewRoundDiscussionFile();

            entityReviewRoundDiscussionFile.setFile(file.getOriginalFilename());

            entityReviewRoundDiscussionFile.setFilePath(filePath);

            entityFileTypeRepository.findFileTypeByJournalIdAndFileTypeId(journalId, Long.valueOf(typeId)).ifPresent(entityReviewRoundDiscussionFile::setJournalFileType);

            entityReviewRoundDiscussionFile.setReviewRoundDiscussion(entityReviewRoundDiscussion);

            entityReviewRoundDiscussionFile.setCreatedAt(Instant.now());

            entityReviewRoundDiscussionFile.setCreatedBy(userService.getCurrentUser());

            entityReviewRoundDiscussionFiles.add(entityReviewRoundDiscussionFile);

        });

        entityReviewRoundDiscussionFileRepository.saveAll(entityReviewRoundDiscussionFiles);

        entityReviewRoundDiscussion.setFiles(entityReviewRoundDiscussionFiles);

        List<EntityReviewRoundDiscussionMember> entityReviewRoundDiscussionMembers = new ArrayList<>();

        members.forEach(member -> {

            EntityReviewRoundDiscussionMember entityReviewRoundDiscussionMember = new EntityReviewRoundDiscussionMember();

            entityUserRepository.findById(member).ifPresent(entityReviewRoundDiscussionMember::setUser);

            entityReviewRoundDiscussionMember.setDiscussion(entityReviewRoundDiscussion);

            entityUserRepository.findById(SecurityUtils.getCurrentUserId()).ifPresent(entityReviewRoundDiscussionMember::setCreatedBy);

            entityReviewRoundDiscussionMember.setCreatedAt(Instant.now());

            entityReviewRoundDiscussionMembers.add(entityReviewRoundDiscussionMember);

        });

        entityReviewRoundDiscussionMemberRepository.saveAll(entityReviewRoundDiscussionMembers);

        entityReviewRoundDiscussion.setDiscussionMessages(new ArrayList<>());

        return reviewMapper.mapEntityReviewRoundDiscussionToReviewRoundDiscussion(entityReviewRoundDiscussion);
    }

    public ReviewRoundDiscussion updateReviewRoundDiscussionStatus(Long discussionId, Long reviewRoundId, ReviewRoundDiscussionStatus reviewRoundDiscussionStatus) {

        Optional<EntityReviewRoundDiscussion> entityReviewRoundDiscussion = entityReviewRoundDiscussionRepository.findDiscussion(discussionId, reviewRoundId);

        if (entityReviewRoundDiscussion.isPresent()) {

            EntityReviewRoundDiscussion discussion = entityReviewRoundDiscussion.get();

            discussion.setStatus(reviewRoundDiscussionStatus);

            entityReviewRoundDiscussionRepository.save(discussion);

            return reviewMapper.mapEntityReviewRoundDiscussionToReviewRoundDiscussion(discussion);
        } else {
            throw new BadRequestAlertException("Invalid Request.", "EntityReviewRoundDiscussion", "invalid.request");
        }
    }

    public List<ReviewRoundDiscussion> getReviewRoundDiscussions(Long reviewRoundId) {

        return reviewMapper.entityReviewRoundDiscussionsToReviewRoundDiscussions(entityReviewRoundDiscussionRepository.getDiscussions(reviewRoundId));
    }

    public List<ReviewRoundDiscussionMessage> getReviewRoundDiscussionChatMessages(Long reviewRoundId, Long discussionId) {

        return reviewMapper.mapEntityReviewRoundDiscussionMessagesToReviewRoundDiscussionMessages(entityReviewRoundDiscussionMessageRepository.getMessages(reviewRoundId, discussionId));
    }

    public List<ReviewRoundFile> getReviewRoundFiles(Long reviewRoundId, Long journalId) {

        return reviewMapper.entityReviewRoundFilesToReviewRoundFiles(entityReviewRoundFileRepository.getFiles(reviewRoundId, journalId));
    }

    public ReviewRoundDiscussionMessage createReviewRoundDiscussionChatMessage(Long journalId, Long submissionId, Long discussionId, Long reviewRoundId, Map<String, MultipartFile> files, String message) {

        EntityReviewRoundDiscussionMessage entityReviewRoundDiscussionMessage = new EntityReviewRoundDiscussionMessage();

        entityReviewRoundRepository.findById(reviewRoundId).ifPresent(entityReviewRoundDiscussionMessage::setReviewRound);

        entityReviewRoundDiscussionRepository.findById(discussionId).ifPresent(entityReviewRoundDiscussionMessage::setDiscussion);

        entityReviewRoundDiscussionMessage.setMessage(message);

        entityUserRepository.findById(Objects.requireNonNull(SecurityUtils.getCurrentUserId())).ifPresent(entityReviewRoundDiscussionMessage::setCreatedBy);

        entityReviewRoundDiscussionMessage.setUser(userService.getCurrentUser());

        entityReviewRoundDiscussionMessage.setCreatedAt(Instant.now());

        entityReviewRoundDiscussionMessageRepository.save(entityReviewRoundDiscussionMessage);

        List<EntityReviewRoundDiscussionMessageFile> entityReviewRoundDiscussionMessageFiles = new ArrayList<>();

        files.forEach((key, file) -> {

            String typeId = Arrays.stream(key.split("-")).collect(Collectors.toList()).get(0);

            EntityReviewRoundDiscussionMessageFile entityReviewRoundDiscussionMessageFile = new EntityReviewRoundDiscussionMessageFile();

            entityReviewRoundDiscussionMessageFile.setFile(file.getOriginalFilename());

            entityFileTypeRepository.findFileTypeByJournalIdAndFileTypeId(journalId, Long.valueOf(typeId)).ifPresent(entityReviewRoundDiscussionMessageFile::setJournalFileType);

            entityReviewRoundDiscussionMessageFile.setDiscussionMessage(entityReviewRoundDiscussionMessage);

            String filePath = fileStorageService.uploadToFinalDirectory(
                commonService.buildFileStoragePath(FilePartition.workflow, journalId,
                    submissionId, WorkflowStage.REVIEW, "production-discussion-message", file.getOriginalFilename()), file);

            entityReviewRoundDiscussionMessageFile.setFilePath(filePath);

            entityReviewRoundDiscussionMessageFile.setCreatedBy(userService.getCurrentUser());

            entityReviewRoundDiscussionMessageFile.setCreatedAt(Instant.now());

            entityUserRepository.findById(Objects.requireNonNull(SecurityUtils.getCurrentUserId())).ifPresent(entityReviewRoundDiscussionMessageFile::setCreatedBy);

            entityReviewRoundDiscussionMessageFiles.add(entityReviewRoundDiscussionMessageFile);

        });

        entityReviewRoundDiscussionMessageFileRepository.saveAll(entityReviewRoundDiscussionMessageFiles);

        entityReviewRoundDiscussionMessage.setFiles(entityReviewRoundDiscussionMessageFiles);

        return reviewMapper.mapEntityReviewRoundDiscussionMessageToReviewRoundDiscussionMessage(entityReviewRoundDiscussionMessage);
    }

    public List<ReviewRoundContributor> addReviewRoundContributor(Long journalId, Long submissionId, Long reviewRoundId, List<String> userIds, String description) {

        List<EntityReviewRoundContributor> entityReviewRoundContributors = new ArrayList<>();

        Optional<EntityReviewRound> entityReviewRound = entityReviewRoundRepository.findById(reviewRoundId);

        if (entityReviewRound.isEmpty()) {
            throw new RuntimeException();
        }

        for (String userId : userIds) {

            if (getReviewRoundContributor(reviewRoundId, userId) == null) {

                EntityReviewRoundContributor entityReviewRoundContributor = new EntityReviewRoundContributor();

                Optional<EntityUser> entityUser = entityUserRepository.findById(userId);

                entityUser.ifPresent(entityReviewRoundContributor::setUser);

                entityReviewRoundContributor.setReviewRound(entityReviewRound.get());

                entityUserRepository.findById(Objects.requireNonNull(SecurityUtils.getCurrentUserId())).ifPresent(entityReviewRoundContributor::setCreatedBy);

                entityReviewRoundContributor.setCreatedAt(Instant.now());
                entityReviewRoundContributor.setDeleted(false);
                entityReviewRoundContributors.add(entityReviewRoundContributor);
            }

            entityReviewRoundContributorRepository.saveAll(entityReviewRoundContributors);

        }

        return reviewMapper.entityReviewRoundContributorsToReviewRoundContributors(entityReviewRoundContributors);

    }

    private ReviewRoundContributor getReviewRoundContributor(Long reviewRoundId, String userId) {
        Optional<EntityReviewRoundContributor> entityUser = entityReviewRoundContributorRepository.findContributorUser(reviewRoundId, userId);

        return entityUser.map(reviewMapper::entityReviewRoundContributorToReviewRoundContributor).orElse(null);
    }

    public List<ReviewRoundContributor> getReviewRoundContributors(Long reviewRoundId) {

        return reviewMapper.entityReviewRoundContributorsToReviewRoundContributors(entityReviewRoundContributorRepository.getContributors(reviewRoundId));
    }

    public void removeContributor(String contributorId, Long reviewRoundId) {

        entityReviewRoundContributorRepository.removeContributor(contributorId, reviewRoundId);
    }

    public ReviewRoundReviewerReview addReviewer(ReviewerRequest reviewerRequest, Long journalId) {

        EntityReviewRoundReview entityReviewRoundReview = new EntityReviewRoundReview();

        entityReviewRoundReview.setReviewerReviewType(reviewerRequest.getSubmissionReviewType());

        entityReviewRoundReview.setReviewDueDate(reviewerRequest.getReviewDueDate());

        entityReviewRoundReview.setResponseDueDate(reviewerRequest.getResponseDueDate());

        entityReviewRoundReview.setReviewStatus(ReviewStatus.REQUEST_SENT);

        entityJournalReviewerRepository.getReviewer(journalId, reviewerRequest.getUserId()).ifPresentOrElse(entityReviewRoundReview::setJournalReviewer,
            () -> {
                throw new BadRequestAlertException("Invalid Request.", "Reviewer Not Found", "invalid.request");
            });

        entityReviewRoundRepository.findById(reviewerRequest.getReviewRoundId()).ifPresent(entityReviewRoundReview::setReviewRound);

        entityReviewRoundRepository.findById(reviewerRequest.getReviewRoundId()).ifPresent(entityReviewRoundReview::setReviewRound);

        entityReviewRoundReview.setCreatedAt(Instant.now());

        entityUserRepository.findById(Objects.requireNonNull(SecurityUtils.getCurrentUserId())).ifPresent(entityReviewRoundReview::setCreatedBy);

        entityReviewRoundReviewRepository.save(entityReviewRoundReview);

        List<EntityReviewRoundReviewerFile> entityReviewRoundReviewerFiles = new ArrayList<>();

        reviewerRequest.getReviewRoundFileIds().forEach(aLong -> {

            EntityReviewRoundReviewerFile entityReviewRoundReviewerFile = new EntityReviewRoundReviewerFile();

            entityReviewRoundFileRepository.findById(aLong).ifPresent(entityReviewRoundReviewerFile::setReviewRoundFile);

            entityReviewRoundReviewerFile.setReviewRoundReviewer(entityReviewRoundReview);

            entityUserRepository.findById(Objects.requireNonNull(SecurityUtils.getCurrentUserId())).ifPresent(entityReviewRoundReviewerFile::setCreatedBy);

            entityReviewRoundReviewerFile.setCreatedAt(Instant.now());

            entityReviewRoundReviewerFiles.add(entityReviewRoundReviewerFile);

        });

        entityReviewRoundReviewerFileRepository.saveAll(entityReviewRoundReviewerFiles);

        entityReviewRoundReview.setReviewerFiles(new ArrayList<>());

        sendMailToReviewer(entityReviewRoundReview, reviewerRequest.getMessage(), entityReviewRoundReview.getReviewRound().getJournal(), entityReviewRoundReview.getReviewRound().getSubmission().getTitle());

        entityReviewRoundReview.setReviewRoundReviewerDiscussions(new ArrayList<>());

        return reviewMapper.mapEntityReviewRoundReviewerToReviewRoundReviewer(entityReviewRoundReview);
    }

    private void sendMailToReviewer(EntityReviewRoundReview entityReviewRoundReview, String message, EntityJournal entityJournal, String submissionTitle) {

        final String journalTitle = entityJournal.getKey() + " - " + entityJournal.getTitle() + " (Round-" + entityReviewRoundReview.getReviewRound().getRound() + ")";

//        mailService.sendReviewRoundUserAddEmailFromTemplate(entityReviewRoundReview.getJournalReviewer().getUser(), ReviewRoundEmailKeyConstant.REVIEWER_ADDED_SUBJECT, submissionTitle, message, journalTitle);

    }

    private void sendMailToCanceledReviewer(EntityReviewRoundReview entityReviewRoundReview, String message, EntityJournal entityJournal, String submissionTitle) {

        final String journalTitle = entityJournal.getKey() + " - " + entityJournal.getTitle() + " (Round-" + entityReviewRoundReview.getReviewRound().getRound() + ")";

//        mailService.sendReviewRoundUserAddEmailFromTemplate(entityReviewRoundReview.getJournalReviewer().getUser(), ReviewRoundEmailKeyConstant.REVIEWER_CANCEL_SUBJECT, submissionTitle, message, journalTitle);

    }

    public List<ReviewRoundReviewerReview> getReviewers(Long reviewRoundId) {

        return reviewMapper.mapEntityReviewRoundReviewersToReviewRoundReviewers(entityReviewRoundReviewRepository.findByReviewRoundId(reviewRoundId));
    }

    public List<ReviewerReplyFile> reviewerReplayFileUpload(Long reviewRoundReviewerId, Long reviewRoundId, Long journalId, Map<String, MultipartFile> files) {

        List<EntityReviewerReplyFile> entityReviewerFiles = new ArrayList<>();

        files.forEach((key, file) -> {

            EntityReviewerReplyFile entityReviewerFile = new EntityReviewerReplyFile();

            entityReviewerFile.setFile(file.getOriginalFilename());

            entityReviewRoundRepository.findById(reviewRoundId).ifPresent(entityReviewerFile::setReviewRound);

            String filePath = fileStorageService.uploadToFinalDirectory(
                commonService.buildFileStoragePath(FilePartition.workflow, journalId,
                    reviewRoundReviewerId, WorkflowStage.REVIEW, "review-round-reviewer-replay", file.getOriginalFilename()), file);

            entityReviewerFile.setFilePath(filePath);

            entityReviewerFile.setCreatedBy(userService.getCurrentUser());

            entityReviewerFile.setCreatedAt(Instant.now());

            entityReviewRoundReviewRepository.findById(reviewRoundReviewerId).ifPresent(entityReviewerFile::setReviewRoundReviewer);

            entityUserRepository.findById(Objects.requireNonNull(SecurityUtils.getCurrentUserId())).ifPresent(entityReviewerFile::setCreatedBy);

            entityReviewerFiles.add(entityReviewerFile);

        });

        entityReviewerReplyFileRepository.saveAll(entityReviewerFiles);

        return reviewMapper.mapEntityReviewerReplyFilesToReviewerReplyFiles(entityReviewerFiles);
    }

    public void unAssignReviewer(String userId, Long reviewRoundId) {

        entityReviewRoundReviewRepository.getReviewer(reviewRoundId, userId).ifPresent(entityReviewRoundReviewer -> entityReviewRoundReviewer.setReviewStatus(ReviewStatus.UNASSIGNED));
    }

    public void cancelReviewer(String userId, Long reviewRoundId, String desc) {

        Optional<EntityReviewRoundReview> entityReviewRoundReviewer = entityReviewRoundReviewRepository.getReviewer(reviewRoundId, userId);

        entityReviewRoundReviewer.ifPresent(entityReviewRoundReview -> entityReviewRoundReview.setReviewStatus(ReviewStatus.CANCELED));
    }

    public Page<ReviewRoundReviewerReview> getAssignedReviews(Long journalId, ReviewType reviewType, String searchText, Pageable pageable) {

        List<ReviewStatus> reviewCurrentStatuses = Arrays.asList(ReviewStatus.REQUEST_ACCEPTED, ReviewStatus.REQUEST_SENT);

        if (reviewType == ReviewType.PENDING) {

            return reviewMapper.mapEntityReviewRoundReviewersToReviewRoundReviewers(entityReviewRoundReviewRepository.getReviewerForPending(SecurityUtils.getCurrentUserId(), reviewCurrentStatuses, journalId, pageable));

        } else {

            return reviewMapper.mapEntityReviewRoundReviewersToReviewRoundReviewers(entityReviewRoundReviewRepository.getReviewerForCompleted(SecurityUtils.getCurrentUserId(), reviewCurrentStatuses, journalId, pageable));

        }
    }


    public ReviewRoundReviewerReview getAssignedReview(Long reviewRoundReviewId) {


        Optional<EntityReviewRoundReview> entityReviewRoundReview = entityReviewRoundReviewRepository.findById(reviewRoundReviewId);

        if (entityReviewRoundReview.isPresent()) {
            return reviewMapper.mapEntityReviewRoundReviewerToReviewRoundReviewer(entityReviewRoundReview.get());
        } else {
            throw new RuntimeException("Invalid Id");
        }
    }

    public ReviewRoundReviewerReview rejectReviewRequest(Long reviewRoundReviewerId, String rejectReason) {

        Optional<EntityReviewRoundReview> entityReviewRoundReviewer = entityReviewRoundReviewRepository.findById(reviewRoundReviewerId);

        if (entityReviewRoundReviewer.isPresent()) {

            entityReviewRoundReviewer.get().setReviewStatus(ReviewStatus.REQUEST_DECLINED);

            entityReviewRoundReviewer.get().setRejectReason(rejectReason);

            return reviewMapper.mapEntityReviewRoundReviewerToReviewRoundReviewerForReviewer(entityReviewRoundReviewer.get());
        }

        throw new BadRequestAlertException("Invalid Request.", "Not Found", "invalid.request");

    }

    public ReviewRoundReviewerReview reviewComplete(Long reviewRoundReviewerId, ReviewerRecommendation reviewerRecommendation, String editorAndAuthorComment, String editorComment) {

        Optional<EntityReviewRoundReview> entityReviewRoundReviewer = entityReviewRoundReviewRepository.findById(reviewRoundReviewerId);

        if (entityReviewRoundReviewer.isPresent()) {

            entityReviewRoundReviewer.get().setReviewStatus(ReviewStatus.COMPLETED);

            entityReviewRoundReviewer.get().setReviewerRecommendation(reviewerRecommendation);

            entityReviewRoundReviewer.get().setEditorComment(editorComment);

            entityReviewRoundReviewer.get().setEditorAndAuthorComment(editorAndAuthorComment);

            entityReviewRoundReviewer.get().setReviewCompletedAt(Instant.now());

            return reviewMapper.mapEntityReviewRoundReviewerToReviewRoundReviewerForReviewer(entityReviewRoundReviewer.get());
        }

        throw new BadRequestAlertException("Invalid Request.", "Not Found", "invalid.request");
    }

    public ReviewRoundReviewerReview updateComment(Long reviewRoundReviewerId, String editorAndAuthorComment, String editorComment, ReviewerRecommendation reviewerRecommendation) {

        Optional<EntityReviewRoundReview> entityReviewRoundReviewer = entityReviewRoundReviewRepository.findById(reviewRoundReviewerId);

        if (entityReviewRoundReviewer.isPresent()) {

            entityReviewRoundReviewer.get().setEditorComment(editorComment);

            entityReviewRoundReviewer.get().setEditorAndAuthorComment(editorAndAuthorComment);

            entityReviewRoundReviewer.get().setReviewerRecommendation(reviewerRecommendation);

            return reviewMapper.mapEntityReviewRoundReviewerToReviewRoundReviewerForReviewer(entityReviewRoundReviewer.get());
        }

        throw new BadRequestAlertException("Invalid Request.", "Not Found", "invalid.request");
    }

    public ReviewRoundReviewerReview acceptRequest(Long reviewRoundReviewerId) {

        entityReviewRoundReviewRepository.findById(reviewRoundReviewerId).ifPresent(entityReviewRoundReviewer -> entityReviewRoundReviewer.setReviewStatus(ReviewStatus.REQUEST_ACCEPTED));

        Optional<EntityReviewRoundReview> entityReviewRoundReviewer = entityReviewRoundReviewRepository.findById(reviewRoundReviewerId);

        if (entityReviewRoundReviewer.isPresent()) {

            entityReviewRoundReviewer.get().setReviewStatus(ReviewStatus.REQUEST_ACCEPTED);

            return reviewMapper.mapEntityReviewRoundReviewerToReviewRoundReviewerForReviewer(entityReviewRoundReviewer.get());

        }
        throw new BadRequestAlertException("Invalid Request.", "Not Found", "invalid.request");
    }


    public ReviewRoundReviewerReview rateReviewer(Long reviewRoundReviewerId, Short rating, ReviewerRecommendation reviewerRecommendation) {

        Optional<EntityReviewRoundReview> entityReviewRoundReviewer = entityReviewRoundReviewRepository.findById(reviewRoundReviewerId);

        if (entityReviewRoundReviewer.isPresent()) {

            entityReviewRoundReviewer.get().setRating(rating);

            entityReviewRoundReviewer.get().setReviewerRecommendation(reviewerRecommendation);

            return reviewMapper.mapEntityReviewRoundReviewerToReviewRoundReviewer(entityReviewRoundReviewer.get());
        }
        throw new BadRequestAlertException("Invalid Request.", "Not Found", "invalid.request");
    }

    public List<ReviewerReplyFile> getReviewerReplayFiles(Long reviewRoundReviewerId, Long reviewRoundId, Long journalId) {

        return reviewMapper.mapEntityReviewerReplyFilesToReviewerReplyFiles(entityReviewerReplyFileRepository.getReviewerReplyFiles(reviewRoundReviewerId, reviewRoundId, journalId));
    }

    public ReviewRoundReviewerDiscussion createReviewRoundReviewerDiscussion(Long reviewRoundReviewerId, Long journalId, Long submissionId, Long reviewRoundId, String topic, String description, Map<String, MultipartFile> files, List<String> members) {

        EntityReviewRoundReviewerDiscussion entityReviewRoundReviewerDiscussion = new EntityReviewRoundReviewerDiscussion();

        entityReviewRoundReviewerDiscussion.setTopic(topic);

        entityReviewRoundReviewerDiscussion.setDescription(description);

        entityReviewRoundReviewerDiscussion.setStatus(ReviewRoundReviewerDiscussionStatus.open);

        entityReviewRoundReviewRepository.findById(reviewRoundReviewerId).ifPresent(entityReviewRoundReviewerDiscussion::setReviewRoundReview);

        entityReviewRoundRepository.findById(reviewRoundId).ifPresent(entityReviewRoundReviewerDiscussion::setReviewRound);

        entityUserRepository.findById(Objects.requireNonNull(SecurityUtils.getCurrentUserId())).ifPresent(entityReviewRoundReviewerDiscussion::setUser);

        entityUserRepository.findById(Objects.requireNonNull(SecurityUtils.getCurrentUserId())).
            ifPresent(entityReviewRoundReviewerDiscussion::setCreatedBy);

        entityReviewRoundReviewerDiscussion.setCreatedAt(Instant.now());

        entityReviewRoundReviewerDiscussionRepository.save(entityReviewRoundReviewerDiscussion);

        List<EntityReviewRoundReviewerDiscussionFile> entityReviewRoundDiscussionFiles = new ArrayList<>();

        files.forEach((key, file) -> {

            String typeId = Arrays.stream(key.split("-")).collect(Collectors.toList()).get(0);

            String filePath = fileStorageService.uploadToFinalDirectory(
                commonService.buildFileStoragePath(FilePartition.workflow, journalId,
                    submissionId, WorkflowStage.REVIEW, "discussion", file.getOriginalFilename()), file);

            EntityReviewRoundReviewerDiscussionFile entityReviewRoundDiscussionFile = new EntityReviewRoundReviewerDiscussionFile();

            entityReviewRoundDiscussionFile.setFile(file.getOriginalFilename());

            entityReviewRoundDiscussionFile.setFilePath(filePath);

            entityFileTypeRepository.findFileTypeByJournalIdAndFileTypeId(journalId, Long.valueOf(typeId)).ifPresent(entityReviewRoundDiscussionFile::setJournalFileType);

            entityReviewRoundDiscussionFile.setReviewRoundDiscussion(entityReviewRoundReviewerDiscussion);

            entityReviewRoundDiscussionFile.setCreatedAt(Instant.now());

            entityReviewRoundDiscussionFile.setCreatedBy(userService.getCurrentUser());

            entityReviewRoundDiscussionFiles.add(entityReviewRoundDiscussionFile);

        });

        entityReviewRoundReviewerDiscussionFileRepository.saveAll(entityReviewRoundDiscussionFiles);

        entityReviewRoundReviewerDiscussion.setFiles(entityReviewRoundDiscussionFiles);

        List<EntityReviewRoundReviewerDiscussionMember> entityReviewRoundDiscussionMembers = new ArrayList<>();

        members.forEach(member -> {

            EntityReviewRoundReviewerDiscussionMember entityReviewRoundDiscussionMember = new EntityReviewRoundReviewerDiscussionMember();

            entityUserRepository.findById(member).ifPresent(entityReviewRoundDiscussionMember::setUser);

            entityReviewRoundDiscussionMember.setDiscussion(entityReviewRoundReviewerDiscussion);

            entityUserRepository.findById(SecurityUtils.getCurrentUserId()).ifPresent(entityReviewRoundDiscussionMember::setCreatedBy);

            entityReviewRoundDiscussionMember.setCreatedAt(Instant.now());

            entityReviewRoundDiscussionMembers.add(entityReviewRoundDiscussionMember);

        });

        entityReviewRoundReviewerDiscussionMemberRepository.saveAll(entityReviewRoundDiscussionMembers);

        entityReviewRoundReviewerDiscussion.setDiscussionMessages(new ArrayList<>());

        return reviewMapper.mapEntityReviewRoundReviewerDiscussionToReviewRoundReviewerDiscussion(entityReviewRoundReviewerDiscussion);

    }

    public ReviewRoundReviewerDiscussion updateReviewRoundReviewerDiscussionStatus(Long reviewRoundReviewerId, Long discussionId, Long reviewRoundId, ReviewRoundReviewerDiscussionStatus reviewRoundDiscussionStatus) {

        Optional<EntityReviewRoundReviewerDiscussion> entityReviewRoundDiscussion = entityReviewRoundReviewerDiscussionRepository.findDiscussion(discussionId, reviewRoundId);

        if (entityReviewRoundDiscussion.isPresent()) {

            EntityReviewRoundReviewerDiscussion discussion = entityReviewRoundDiscussion.get();

            discussion.setStatus(reviewRoundDiscussionStatus);

            entityReviewRoundReviewerDiscussionRepository.save(discussion);

            return reviewMapper.mapEntityReviewRoundReviewerDiscussionToReviewRoundReviewerDiscussion(discussion);
        } else {
            throw new BadRequestAlertException("Invalid Request.", "EntityReviewRoundDiscussion", "invalid.request");
        }
    }

    public List<ReviewRoundReviewerDiscussion> getReviewRoundReviewerDiscussions(Long reviewRoundReviewerId, Long reviewRoundId) {

        return reviewMapper.entityReviewRoundReviewerDiscussionsToReviewRoundReviewerDiscussions(entityReviewRoundReviewerDiscussionRepository.getDiscussions(reviewRoundReviewerId, reviewRoundId));
    }


    public Object createReviewRoundReviewerDiscussionChatMessage(Long journalId, Long submissionId, Long discussionId, Long reviewRoundId, Map<String, MultipartFile> files, String message) {

        EntityReviewRoundReviewerDiscussionMessage entityReviewRoundReviewerDiscussionMessage = new EntityReviewRoundReviewerDiscussionMessage();

        entityReviewRoundRepository.findById(reviewRoundId).ifPresent(entityReviewRoundReviewerDiscussionMessage::setReviewRound);

        entityReviewRoundReviewerDiscussionRepository.findById(discussionId).ifPresent(entityReviewRoundReviewerDiscussionMessage::setDiscussion);

        entityReviewRoundReviewerDiscussionMessage.setMessage(message);

        entityUserRepository.findById(Objects.requireNonNull(SecurityUtils.getCurrentUserId())).ifPresent(entityReviewRoundReviewerDiscussionMessage::setCreatedBy);

        entityReviewRoundReviewerDiscussionMessage.setUser(userService.getCurrentUser());

        entityReviewRoundReviewerDiscussionMessage.setCreatedAt(Instant.now());

        entityReviewRoundReviewerDiscussionMessageRepository.save(entityReviewRoundReviewerDiscussionMessage);

        List<EntityReviewRoundReviewerDiscussionMessageFile> entityReviewRoundReviewerDiscussionMessageFiles = new ArrayList<>();

        files.forEach((key, file) -> {

            String typeId = Arrays.stream(key.split("-")).collect(Collectors.toList()).get(0);

            EntityReviewRoundReviewerDiscussionMessageFile entityReviewRoundReviewerDiscussionMessageFile = new EntityReviewRoundReviewerDiscussionMessageFile();

            entityReviewRoundReviewerDiscussionMessageFile.setFile(file.getOriginalFilename());

            entityFileTypeRepository.findFileTypeByJournalIdAndFileTypeId(journalId, Long.valueOf(typeId)).ifPresent(entityReviewRoundReviewerDiscussionMessageFile::setJournalFileType);

            entityReviewRoundReviewerDiscussionMessageFile.setDiscussionMessage(entityReviewRoundReviewerDiscussionMessage);

            String filePath = fileStorageService.uploadToFinalDirectory(
                commonService.buildFileStoragePath(FilePartition.workflow, journalId,
                    submissionId, WorkflowStage.REVIEW, "production-discussion-message", file.getOriginalFilename()), file);

            entityReviewRoundReviewerDiscussionMessageFile.setFilePath(filePath);

            entityReviewRoundReviewerDiscussionMessageFile.setCreatedBy(userService.getCurrentUser());

            entityReviewRoundReviewerDiscussionMessageFile.setCreatedAt(Instant.now());

            entityUserRepository.findById(Objects.requireNonNull(SecurityUtils.getCurrentUserId())).ifPresent(entityReviewRoundReviewerDiscussionMessageFile::setCreatedBy);

            entityReviewRoundReviewerDiscussionMessageFiles.add(entityReviewRoundReviewerDiscussionMessageFile);

        });

        entityReviewRoundReviewerDiscussionMessageFileRepository.saveAll(entityReviewRoundReviewerDiscussionMessageFiles);

        entityReviewRoundReviewerDiscussionMessage.setFiles(entityReviewRoundReviewerDiscussionMessageFiles);

        return reviewMapper.mapEntityReviewRoundReviewerDiscussionMessageToReviewRoundReviewerDiscussionMessage(entityReviewRoundReviewerDiscussionMessage);
    }

    public List<ReviewRoundReviewerDiscussionMessage> getReviewRoundReviewerDiscussionChatMessages(Long reviewRoundId, Long discussionId) {

        return reviewMapper.mapEntityReviewRoundReviewerDiscussionMessagesToReviewRoundReviewerDiscussionMessages(entityReviewRoundReviewerDiscussionMessageRepository.getMessages(reviewRoundId, discussionId));
    }

    public File downloadReviewRoundFilesAsZip(Long journalId,Long submissionId, Long reviewRoundId) throws IOException {

        List<String> filePaths = entityReviewRoundFileRepository.getSubmissionFilePaths(journalId,submissionId, reviewRoundId);

        return fileStorageService.createZipFile(String.valueOf(journalId + submissionId), filePaths);
    }
}



