package com.kjms.service;

import com.kjms.domain.*;
import com.kjms.repository.*;
import com.kjms.security.BadRequestAlertException;
import com.kjms.security.BadRequestEntityConstants;
import com.kjms.security.SecurityUtils;
import com.kjms.service.dto.*;
import com.kjms.service.dto.requests.SubmissionAuthorRequest;
import com.kjms.service.dto.requests.SubmissionCreateRequest;
import com.kjms.service.dto.requests.SubmissionUpdateRequest;
import com.kjms.service.errors.*;
import com.kjms.service.mail.CommonMailActionId;
import com.kjms.service.mail.MailService;
import com.kjms.service.mail.MailToVariable;
import com.kjms.service.mapper.SubmissionArticleAuthorMapper;
//import com.kjms.service.mapper.SubmissionArticleFileMapper;
import com.kjms.service.mapper.SubmissionMapper;
import com.kjms.service.storage.FileStorageService;
import com.kjms.service.utils.AppStringUtils;
import com.kjms.service.utils.FileUtils;
import com.kjms.service.utils.WordUtils;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import com.kjms.web.rest.utils.ListUtils;
import com.kjms.web.rest.utils.dto.LongDiff;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


/**
 * Service class for managing Submission Articles.
 */
@Service
@Transactional
public class SubmissionService {

    private final Logger log = LoggerFactory.getLogger(SubmissionService.class);

    private final EntityJournalSubmissionLanguageRepository journalSubmissionLanguageRepository;
    private final EntitySubmissionRepository entitySubmissionRepository;
    private final EntitySubmissionCategoryRepository submissionArticleCategoryRepository;
    private final EntityJournalCategoryRepository journalCategoryRepository;
    private final EntityUserRepository userRepository;
    private final SubmissionMapper submissionMapper;
    private final FileStorageService oldFileStorageService;
    private final WorkflowFileStorageService fileStorageService;
    private final EntitySubmissionFileRepository entitySubmissionFileRepository;
    private final EntitySubmissionAuthorRepository submissionAuthorRepository;
    private final SubmissionArticleAuthorMapper submissionArticleAuthorMapper;
    private final MailService mailService;
    private final UserService userService;
    private final JournalService journalService;
    private final CommonService commonService;
    private final EntityFileTypeRepository entityFileTypeRepository;
    private final EntitySubmissionKeywordRepository submissionArticleKeywordRepository;
    private final EntityUserRepository entityUserRepository;
    public final static String FILE_NAME = "fileName";

    public final static String FILE_PATH = "filePath";
    public final static String FILE_SIZE = "fileSize";
    private final EntityJournalRepository entityJournalRepository;
    private final EntitySubmissionDiscussionRepository entitySubmissionDiscussionRepository;
    private final EntitySubmissionDiscussionFileRepository entitySubmissionDiscussionFileRepository;
    private final EntitySubmissionDiscussionMemberRepository entitySubmissionDiscussionMemberRepository;
    private final EntitySubmissionDiscussionMessageRepository entitySubmissionDiscussionMessageRepository;
    private final EntitySubmissionDiscussionMessageFileRepository entitySubmissionDiscussionMessageFileRepository;
    private final EntitySubmissionContributorRepository entitySubmissionContributorRepository;
    private final EntityJournalEditorialRoleRepository entityJournalEditorialRoleRepository;
    private final EntityJournalEditorialUserRepository entityJournalEditorialUserRepository;

    public SubmissionService(EntityJournalSubmissionLanguageRepository journalSubmissionLanguageRepository,
                             EntitySubmissionRepository entitySubmissionRepository,
                             EntitySubmissionCategoryRepository submissionArticleCategoryRepository,
                             EntityJournalCategoryRepository journalCategoryRepository,
                             EntityUserRepository userRepository,
                             SubmissionMapper submissionMapper,
                             FileStorageService oldFileStorageService,
                             WorkflowFileStorageService fileStorageService,
                             EntitySubmissionFileRepository entitySubmissionFileRepository,
                             EntitySubmissionAuthorRepository submissionAuthorRepository,
                             SubmissionArticleAuthorMapper submissionArticleAuthorMapper,
                             UserService userService, JournalService journalService,
                             EntityFileTypeRepository entityFileTypeRepository,
                             MailService mailService,
                             CommonService commonService,
                             EntitySubmissionKeywordRepository submissionArticleKeywordRepository,
                             EntityUserRepository entityUserRepository,
                             EntityJournalRepository entityJournalRepository,
                             EntitySubmissionDiscussionRepository entitySubmissionDiscussionRepository,
                             EntitySubmissionDiscussionFileRepository entitySubmissionDiscussionFileRepository,
                             EntitySubmissionDiscussionMemberRepository entitySubmissionDiscussionMemberRepository,
                             EntitySubmissionDiscussionMessageRepository entitySubmissionDiscussionMessageRepository,
                             EntitySubmissionDiscussionMessageFileRepository entitySubmissionDiscussionMessageFileRepository,
                             EntitySubmissionContributorRepository entitySubmissionContributorRepository,
                             EntityJournalEditorialRoleRepository entityJournalEditorialRoleRepository,
                             EntityJournalEditorialUserRepository entityJournalEditorialUserRepository) {

        this.journalSubmissionLanguageRepository = journalSubmissionLanguageRepository;
        this.entitySubmissionRepository = entitySubmissionRepository;
        this.submissionArticleCategoryRepository = submissionArticleCategoryRepository;
        this.journalCategoryRepository = journalCategoryRepository;
        this.userRepository = userRepository;
        this.submissionMapper = submissionMapper;
        this.oldFileStorageService = oldFileStorageService;
        this.fileStorageService = fileStorageService;
        this.entitySubmissionFileRepository = entitySubmissionFileRepository;
        this.submissionAuthorRepository = submissionAuthorRepository;
        this.submissionArticleAuthorMapper = submissionArticleAuthorMapper;
        this.userService = userService;
        this.journalService = journalService;
        this.entityFileTypeRepository = entityFileTypeRepository;
        this.mailService = mailService;
        this.commonService = commonService;
        this.submissionArticleKeywordRepository = submissionArticleKeywordRepository;
        this.entityUserRepository = entityUserRepository;
        this.entityJournalRepository = entityJournalRepository;
        this.entitySubmissionDiscussionRepository = entitySubmissionDiscussionRepository;
        this.entitySubmissionDiscussionFileRepository = entitySubmissionDiscussionFileRepository;
        this.entitySubmissionDiscussionMemberRepository = entitySubmissionDiscussionMemberRepository;
        this.entitySubmissionDiscussionMessageRepository = entitySubmissionDiscussionMessageRepository;
        this.entitySubmissionDiscussionMessageFileRepository = entitySubmissionDiscussionMessageFileRepository;
        this.entitySubmissionContributorRepository = entitySubmissionContributorRepository;
        this.entityJournalEditorialRoleRepository = entityJournalEditorialRoleRepository;
        this.entityJournalEditorialUserRepository = entityJournalEditorialUserRepository;
    }


    public Page<SubmissionCategory> getJournalCategories(Long journalId, String searchText, Pageable pageable) {
        userService.validateCurrentUser();

        EntityJournal entityJournal = journalService.getEntityJournal(journalId);

        Page<EntityJournalCategory> entityJournalCategories;

        if (!StringUtils.hasText(searchText)) {
            entityJournalCategories = journalCategoryRepository.findAllByIsDeletedIsFalseAndJournalId(entityJournal, pageable);
        } else {
            entityJournalCategories = journalCategoryRepository.findAllByIsDeletedIsFalseAndJournalIdAndCategoryId_NameContaining(entityJournal, searchText, pageable);
        }

        return submissionMapper.entitySubmissionCategoryToSubmissionCategory(entityJournalCategories);
    }

    private void setEntitySubmissionArticleAttributeValue(String prefix, String title, String subTitle, String
        description, EntitySubmission entitySubmission) {

        prefix = WordUtils.makeFirstLetterUpper(prefix);

        title = WordUtils.makeFirstLetterUpper(title);

        subTitle = WordUtils.makeFirstLetterUpper(subTitle);

        description = StringUtils.trimWhitespace(description);

        entitySubmission.setPrefix(prefix);
        entitySubmission.setLastUpdatedPrefix(prefix);
        entitySubmission.setTitle(title);
        entitySubmission.setLastUpdatedTitle(title);
        entitySubmission.setSubTitle(subTitle);
        entitySubmission.setLastUpdatedSubTitle(subTitle);
        entitySubmission.setDescription(description);
        entitySubmission.setLastUpdatedDesc(description);
    }

    /**
     * Create Submission Article Basic details
     *
     * @param submissionCreateRequest the submission basic details to add
     * @return the created submission article id.
     * @throws InvalidOrgException  if the organization isn't found.
     * @throws InvalidUserException if the user isn't found.
     */
    public Long createSubmission(Long journalId, SubmissionCreateRequest submissionCreateRequest) {

        EntityUser currentUser = userService.getCurrentUser();

        EntityJournal entityJournal = journalService.getEntityJournal(journalId);

        Integer journalCompletedStep = entityJournal.getCompletedStep();

        if (journalCompletedStep == null || journalCompletedStep < 3) {
            throw new JournalNotFoundException();
        }

        EntitySubmission entitySubmission = new EntitySubmission();
        entityJournalRepository.findById(journalId).ifPresent(entitySubmission::setJournal);
        entitySubmission.setAuthor(currentUser);
        entitySubmission.setCreatedAt(Instant.now());
        entitySubmission.setCreatedBy(currentUser);
        entitySubmission.setDeleted(false);
        entitySubmission.setStatus(SubmissionStatus.DRAFTED);
        journalSubmissionLanguageRepository.findOneByIdAndIsDeletedIsFalse(submissionCreateRequest.getJournalLanguageId()).ifPresent(entitySubmission::setJournalLanguage);
        entitySubmission.setLastModifiedAt(Instant.now());
        entitySubmission.setLastModifiedBy(currentUser);
        entitySubmission.setWorkflowStage(WorkflowStage.SUBMISSION);

        setEntitySubmissionArticleAttributeValue(submissionCreateRequest.getPrefix(), submissionCreateRequest.getTitle(), submissionCreateRequest.getSubTitle(), submissionCreateRequest.getDescription(), entitySubmission);

        entitySubmissionRepository.save(entitySubmission);

        EntitySubmissionAuthor entitySubmissionAuthor = new EntitySubmissionAuthor();

        entitySubmissionAuthor.setCreatedBy(currentUser);
        entitySubmissionAuthor.setCreatedAt(Instant.now());
        entitySubmissionAuthor.setSubmission(entitySubmission);
        entitySubmissionAuthor.setJournal(entityJournal);
        entitySubmissionAuthor.setDeleted(false);
        entitySubmissionAuthor.setFirstName(currentUser.getFullName());
        entitySubmissionAuthor.setEmail(currentUser.getEmail());
        entitySubmissionAuthor.setPrimary(true);

        submissionAuthorRepository.save(entitySubmissionAuthor);

        log.debug("Created Information for Submission Article: {}", entitySubmission);

        return entitySubmission.getId();
    }

    /**
     * Update Submission Article Basic details.
     *
     * @param submissionUpdateRequest the submission article to update.
     * @return title of submission article.
     * @throws InvalidOrgException  if the organization isn't found.
     * @throws InvalidUserException if the user isn't found.
     */
    public Submission updateSubmission(Long journalId, Long submissionId, SubmissionUpdateRequest
        submissionUpdateRequest) {

        EntityUser currentUser = userService.getCurrentUser();

        EntitySubmission entitySubmission = getEntitySubmission(submissionId, journalId);

        if (entitySubmission.getStatus() != SubmissionStatus.DRAFTED || !currentUser.equals(entitySubmission.getCreatedBy())) {
            throw new SubmissionNotFoundException();
        }

        entitySubmission.setAgree(submissionUpdateRequest.getAgree());

        entitySubmission.setLastModifiedAt(Instant.now());

        entitySubmission.setLastModifiedBy(currentUser);

        journalSubmissionLanguageRepository
            .findOneByIdAndIsDeletedIsFalse(submissionUpdateRequest.getLanguageId())
            .ifPresent(entitySubmission::setJournalLanguage);

        setEntitySubmissionArticleAttributeValue(submissionUpdateRequest.getPrefix(), submissionUpdateRequest.getTitle(), submissionUpdateRequest.getSubTitle(), submissionUpdateRequest.getDescription(), entitySubmission);

        entitySubmissionRepository.save(entitySubmission);

        submissionArticleKeywordRepository.removeSubmissionKeyWords(submissionId, journalId);

        ArrayList<EntitySubmissionKeyword> entitySubmissionKeywords = new ArrayList<>();

        submissionUpdateRequest.getKeywords().forEach(keyword -> {

            EntitySubmissionKeyword entitySubmissionKeyword = new EntitySubmissionKeyword();

            entitySubmissionKeyword.setSubmission(entitySubmission);
            entityJournalRepository.findOneByIdAndIsDeletedIsFalse(journalId).ifPresent(entitySubmissionKeyword::setJournal);
            entitySubmissionKeyword.setDeleted(false);
            entitySubmissionKeyword.setCreatedAt(Instant.now());
            entitySubmissionKeyword.setKeyword(keyword);
            entitySubmissionKeywords.add(entitySubmissionKeyword);
        });

        submissionArticleKeywordRepository.saveAll(entitySubmissionKeywords);

        log.debug("Updated Information for Submission Article: {}", entitySubmission);

        return getSubmission(journalId, submissionId);
    }

    /**
     * Get Submission Article
     *
     * @param submissionId the submission article to get.
     * @return {@link Submission}
     * @throws InvalidOrgException  if the organization isn't found.
     * @throws InvalidUserException if the user isn't found.
     */
    public Submission getSubmission(Long journalId, Long submissionId) {

        EntityJournal entityJournal = journalService.getEntityJournal(journalId);

        EntitySubmission entitySubmission = getEntitySubmission(submissionId, journalId);

        Submission submission = submissionMapper.entitySubmissionToSubmission(entitySubmission);

        List<EntitySubmissionCategory> entitySubmissionArticleCategories = submissionArticleCategoryRepository.findAllByIsDeletedIsFalseAndJournalAndSubmission(entityJournal, entitySubmission);

        submission.setCategories(submissionMapper.entitySubmissionCategoryToSubmissionCategory(entitySubmissionArticleCategories));

        List<EntitySubmissionAuthor> entitySubmissionAuthors = getEntitySubmissionArticleAuthors(journalId, submissionId);

        List<SubmissionArticleAuthor> submissionArticleAuthors = submissionArticleAuthorMapper.entitySubmissionArticlesAuthorToSubmissionArticleAuthors(entitySubmissionAuthors);

        submission.setAuthors(submissionArticleAuthors);

        submission.setSubmissionFiles(submissionMapper.entitySubmissionFilesToSubmissionFiles(entitySubmissionFileRepository.getSubmissionFiles(journalId, submissionId)));

        List<Map<String, String>> allFilePaths = new ArrayList<>();

//        submission.setAllFilePath(oldFileStorageService.createZipFileDownloadLink(entityJournal.getStorageContainerName(), allFilePaths));

        submission.setKeywords(submissionArticleKeywordRepository.getSubmissionKeyWords(submissionId, journalId));

        submission.setSubmissionDiscussions(getDiscussions(journalId, submissionId));

        submission.setSubmissionContributors(getContributors(journalId, submissionId));

        return submission;
    }

    /**
     * Get all Submission Article.
     *
     * @param pageable   pageable for pagination
     * @param searchText text to search language name & key.
     * @param journalId  the journal to get a list.
     * @return list of {@link Submission}
     * @throws InvalidOrgException  if the organization isn't found.
     * @throws InvalidUserException if the user isn't found.
     */
    public Page<Submission> getSubmissions(Long journalId, String searchText, Pageable
        pageable, WorkflowStage workflowStage) {

        EntityUser currentUser = userService.getCurrentUser();

        EntityJournal entityJournal = journalService.getEntityJournal(journalId);

        Page<EntitySubmission> entitySubmissionArticles;

        entitySubmissionArticles = entitySubmissionRepository.findAllByJournalIdAndIsDeletedIsFalseAndTextContaining(entityJournal, searchText, currentUser, workflowStage, pageable);

        return submissionMapper.entitySubmissionToSubmission(entitySubmissionArticles);
    }

    public List<SubmissionAuthor> updateSubmissionAuthor(Long journalId, Long submissionId,
                                                         List<SubmissionAuthorRequest> submissionAuthorRequests) {

        List<Long> existingIds = submissionAuthorRepository.getJournalSubmissionAuthorIds(journalId, submissionId);

        List<Long> newIds = submissionAuthorRequests.stream()
            .map(SubmissionAuthorRequest::getId) // Extract the ID from each User object
            .filter(Objects::nonNull) // Filter out null IDs
            .collect(Collectors.toList()); // Collect non-null IDs into a new list

        LongDiff diff = ListUtils.findLongDifference(existingIds, newIds);

        submissionAuthorRepository.deleteSubmissionAuthor(diff.getRemoved());

        List<EntitySubmissionAuthor> entitySubmissionAuthors = new ArrayList<>();

        for (SubmissionAuthorRequest authorRequest : submissionAuthorRequests) {

            EntitySubmissionAuthor entitySubmissionAuthor = new EntitySubmissionAuthor();

            entitySubmissionRepository.findById(submissionId).ifPresent(entitySubmissionAuthor::setSubmission);

            entitySubmissionAuthor.setPrefix(authorRequest.getPrefix());

            entitySubmissionAuthor.setFirstName(authorRequest.getFirstName());

            entitySubmissionAuthor.setSurName(authorRequest.getSurName());

            entitySubmissionAuthor.setMiddleName(authorRequest.getMiddleName());

            entitySubmissionAuthor.setOrcidId(authorRequest.getOrcidId());

            entitySubmissionAuthor.setEmail(authorRequest.getEmail());

            entitySubmissionAuthor.setPrimary(authorRequest.getPrimary());

            entitySubmissionAuthor.setDeleted(false);

            entitySubmissionAuthor.setCreatedAt(Instant.now());

            entitySubmissionAuthor.setCreatedBy(userService.getCurrentUser());
        }
        submissionAuthorRepository.saveAll(entitySubmissionAuthors);

        return submissionMapper.mapEntitySubmissionAuthorsToEntitySubmissionAuthors(entitySubmissionAuthors);
    }

    /**
     * Submit Final Submission of article
     *
     * @param journalId    the submission article's journal.
     * @param submissionId the submission article to submit.
     * @return article title.
     */
    public Submission finishSubmission(Long journalId, Long submissionId) {

        EntityUser currentUser = userService.getCurrentUser();

        Optional<EntityJournal> entityJournal = entityJournalRepository.findOneByIdAndIsDeletedIsFalse(journalId);

        if (entityJournal.isEmpty()) {

            throw new RuntimeException("Invalid journal Id");
        }

        EntitySubmission entitySubmission = getEntitySubmission(submissionId, journalId);

        if (entitySubmission.getCreatedBy() != currentUser) {

            throw new SubmissionNotFoundException();
        }

        entitySubmission.setStatus(SubmissionStatus.SUBMITTED);

        entitySubmission.setSubmittedDate(Instant.now());

        entitySubmission.setLastModifiedBy(currentUser);

        entitySubmission.setLastModifiedAt(Instant.now());

        entitySubmissionRepository.save(entitySubmission);

        addDefaultContributorsForSubmission(journalId, entitySubmission, currentUser);

        log.debug("Submission Article Submitted: {}", entitySubmission);

//        mailService.triggerMail(journalId, CommonMailActionId.SUBMISSION, Map.of(MailToVariable.AUTHOR, entitySubmission.getAuthor().getFullName()),
//            Map.of(MailToVariable.AUTHOR, entitySubmission.getAuthor().getEmail()));

        return submissionMapper.entitySubmissionToSubmission(entitySubmission);
    }

    private void addDefaultContributorsForSubmission(Long journalId, EntitySubmission entitySubmission, EntityUser currentUser) {

        //Adding Primary Author as Contributor
        EntitySubmissionContributor entitySubmissionAuthor = new EntitySubmissionContributor();

        entitySubmissionAuthor.setSubmission(entitySubmission);

        entitySubmissionAuthor.setUser(entitySubmission.getAuthor());

        entitySubmissionAuthor.setCreatedAt(Instant.now());

        entitySubmissionAuthor.setCreatedBy(currentUser);

        entitySubmissionAuthor.setDeleted(false);

        entitySubmissionContributorRepository.save(entitySubmissionAuthor);

        EntitySubmissionContributor entitySubmissionJournalManager = new EntitySubmissionContributor();

        entitySubmissionJournalManager.setSubmission(entitySubmission);

        entitySubmissionJournalManager.setUser(entitySubmission.getAuthor());

        entitySubmissionJournalManager.setCreatedAt(Instant.now());

        entitySubmissionJournalManager.setCreatedBy(currentUser);

        entitySubmissionJournalManager.setDeleted(false);

        entitySubmissionContributorRepository.save(entitySubmissionJournalManager);
    }


    private List<EntitySubmissionAuthor> getEntitySubmissionArticleAuthors(Long entityJournal, Long
        entitySubmission) {
        return submissionAuthorRepository.getJournalSubmissionAuthors(entityJournal, entitySubmission);
    }


    EntitySubmission getEntitySubmission(Long submissionId, Long journalId) {
        return entitySubmissionRepository.getSubmission(submissionId, journalId).orElseThrow(SubmissionNotFoundException::new);
    }

    public List<SubmissionArticleAuthor> readSubArticleAuthorExcel(Long journalId, MultipartFile multipartFile) {
        userService.validateCurrentUser();

        if (!FileUtils.isValidExcel(multipartFile)) {
            throw new BadRequestAlertException("Wrong Excel Format", BadRequestEntityConstants.COMMON, "wrongExcelFormat");
        }

        // Keys to check
        Set<String> keysToCheck = Set.of("prefix", "firstname", "surname", "middlename", "email", "orcid", "showbrowserlist", "role", "primary");

        List<Map<String, Object>> dataList = FileUtils.readExcel(multipartFile);

        return dataList.stream().map(map -> {
            Map<String, Object> lowercasedMap = new HashMap<>();
            map.forEach((key, value) -> lowercasedMap.put(StringUtils.trimAllWhitespace(key).toLowerCase(), value));
            return lowercasedMap;
        }).filter(map -> (keysToCheck.stream().anyMatch(map::containsKey))).map(map -> {
            SubmissionArticleAuthor submissionArticleAuthor = new SubmissionArticleAuthor();

            submissionArticleAuthor.setPrefix((String) map.get("prefix"));
            submissionArticleAuthor.setFirstName((String) map.get("firstname"));
            submissionArticleAuthor.setSurName((String) map.get("surname"));
            submissionArticleAuthor.setMiddleName((String) map.get("middlename"));
            submissionArticleAuthor.setEmail((String) map.get("email"));
            submissionArticleAuthor.setOrcidId((String) map.get("orcid"));
            submissionArticleAuthor.setOwnAuthorDetail(false);

            String primary = (map.get("primary") != null) ? String.valueOf(map.get("primary")).toLowerCase() : "no";
            String browserList = (map.get("showbrowserlist") != null) ? String.valueOf(map.get("showbrowserlist")).toLowerCase() : "no";

            submissionArticleAuthor.setPrimary(primary.equals("yes"));
            submissionArticleAuthor.setBrowserList(browserList.equals("yes"));

            String role = StringUtils.trimWhitespace(String.valueOf(map.get("role")));

            submissionArticleAuthor.setRole(role.equalsIgnoreCase("author") ? SubmissionArticleContributorRole.AUTHOR.name() : role.equalsIgnoreCase("translator") ? SubmissionArticleContributorRole.TRANSLATOR.name() : null);

            return submissionArticleAuthor;
        }).collect(Collectors.toList());
    }

    public void moveSubmissionToOtherWorkflow(EntityUser currentUser, EntitySubmission entitySubmission, WorkflowStage workflowStage) {

        entitySubmission.setWorkflowStage(workflowStage);
        entitySubmission.setLastModifiedBy(currentUser);
        entitySubmission.setLastModifiedAt(Instant.now());

    }

    public SubmissionDiscussion createDiscussion(Long journalId, Long submissionId, String topic, String
        description, Map<String, MultipartFile> files, List<String> members) {

        EntitySubmissionDiscussion entitySubmissionDiscussion = new EntitySubmissionDiscussion();

        entitySubmissionDiscussion.setTopic(topic);

        entitySubmissionDiscussion.setDescription(description);

        entitySubmissionDiscussion.setStatus(SubmissionDiscussionStatus.open);

        entityUserRepository.findById(Objects.requireNonNull(SecurityUtils.getCurrentUserId())).ifPresent(entitySubmissionDiscussion::setUser);

        entitySubmissionRepository.findById(submissionId).ifPresent(entitySubmissionDiscussion::setSubmission);

        entityJournalRepository.findById(journalId).ifPresent(entitySubmissionDiscussion::setJournal);

        entityUserRepository.findById(Objects.requireNonNull(SecurityUtils.getCurrentUserId())).ifPresent(entitySubmissionDiscussion::setCreatedBy);

        entitySubmissionDiscussion.setCreatedAt(Instant.now());

        entitySubmissionDiscussionRepository.save(entitySubmissionDiscussion);

        List<EntitySubmissionDiscussionFile> entitySubmissionDiscussionFiles = new ArrayList<>();

        files.forEach((key, file) -> {

            String typeId = Arrays.stream(key.split("-")).collect(Collectors.toList()).get(0);

            String filePath = fileStorageService.uploadToFinalDirectory(commonService.buildFileStoragePath(FilePartition.workflow, journalId, submissionId, WorkflowStage.SUBMISSION, "discussion", file.getOriginalFilename()), file);

            EntitySubmissionDiscussionFile entitySubmissionDiscussionFile = new EntitySubmissionDiscussionFile();

            entitySubmissionDiscussionFile.setFile(file.getOriginalFilename());

            entitySubmissionDiscussionFile.setFilePath(filePath);

            entityFileTypeRepository.findFileTypeByJournalIdAndFileTypeId(journalId, Long.valueOf(typeId)).ifPresent(entitySubmissionDiscussionFile::setJournalFileType);

            entitySubmissionDiscussionFile.setSubmissionDiscussion(entitySubmissionDiscussion);

            entitySubmissionDiscussionFile.setCreatedAt(Instant.now());

            entitySubmissionDiscussionFile.setCreatedBy(userService.getCurrentUser());

            entitySubmissionDiscussionFiles.add(entitySubmissionDiscussionFile);

        });

        entitySubmissionDiscussionFileRepository.saveAll(entitySubmissionDiscussionFiles);

        entitySubmissionDiscussion.setFiles(entitySubmissionDiscussionFiles);

        List<EntitySubmissionDiscussionMember> entitySubmissionDiscussionMembers = new ArrayList<>();

        members.forEach(member -> {

            EntitySubmissionDiscussionMember entitySubmissionDiscussionMember = new EntitySubmissionDiscussionMember();

            entityUserRepository.findById(member).ifPresent(entitySubmissionDiscussionMember::setUser);

            entitySubmissionDiscussionMember.setDiscussion(entitySubmissionDiscussion);

            entityUserRepository.findById(SecurityUtils.getCurrentUserId()).ifPresent(entitySubmissionDiscussionMember::setCreatedBy);

            entitySubmissionDiscussionMember.setCreatedAt(Instant.now());

            entitySubmissionDiscussionMembers.add(entitySubmissionDiscussionMember);

        });

        entitySubmissionDiscussionMemberRepository.saveAll(entitySubmissionDiscussionMembers);

        entitySubmissionDiscussion.setDiscussionMessages(new ArrayList<>());

        return submissionMapper.mapEntityDiscussionToDiscussion(entitySubmissionDiscussion, journalId);
    }

    public SubmissionDiscussion updateDiscussionStatus(Long journalId, Long submissionId, Long
        discussionId, SubmissionDiscussionStatus productionDiscussionStatus) {

        Optional<EntitySubmissionDiscussion> entitySubmissionDiscussion = entitySubmissionDiscussionRepository.findDiscussion(discussionId, journalId, submissionId);

        if (entitySubmissionDiscussion.isPresent()) {

            EntitySubmissionDiscussion discussion = entitySubmissionDiscussion.get();

            discussion.setStatus(productionDiscussionStatus);

            entitySubmissionDiscussionRepository.save(discussion);

            return submissionMapper.mapEntityDiscussionToDiscussion(discussion, journalId);
        } else {
            throw new BadRequestAlertException("Invalid Request.", "EntityProductionDiscussion", "invalid.request");
        }
    }

    public List<SubmissionDiscussion> getDiscussions(Long journalId, Long submissionId) {

        return submissionMapper.mapEntitySubmissionDiscussionsToSubmissionDiscussions(entitySubmissionDiscussionRepository.getDiscussions(journalId, submissionId), journalId);
    }


    public SubmissionDiscussionMessage createDiscussionChatMessage(Long journalId, Long submissionId, Long
        discussionId, Map<String, MultipartFile> files, String message) {

        EntitySubmissionDiscussionMessage entitySubmissionDiscussionMessage = new EntitySubmissionDiscussionMessage();

        entityJournalRepository.findById(journalId).ifPresent(entitySubmissionDiscussionMessage::setJournal);

        entitySubmissionRepository.findById(submissionId).ifPresent(entitySubmissionDiscussionMessage::setSubmission);

        entitySubmissionDiscussionRepository.findById(discussionId).ifPresent(entitySubmissionDiscussionMessage::setDiscussion);

        entitySubmissionDiscussionMessage.setMessage(message);

        entityUserRepository.findById(Objects.requireNonNull(SecurityUtils.getCurrentUserId())).ifPresent(entitySubmissionDiscussionMessage::setCreatedBy);

        entitySubmissionDiscussionMessage.setUser(userService.getCurrentUser());

        entitySubmissionDiscussionMessage.setCreatedAt(Instant.now());

        entitySubmissionDiscussionMessage.setMessage(message);

        entitySubmissionDiscussionMessageRepository.save(entitySubmissionDiscussionMessage);

        List<EntitySubmissionDiscussionMessageFile> entitySubmissionDiscussionMessageFiles = new ArrayList<>();

        files.forEach((key, file) -> {

            String typeId = Arrays.stream(key.split("-")).collect(Collectors.toList()).get(0);

            EntitySubmissionDiscussionMessageFile entitySubmissionDiscussionMessageFile = new EntitySubmissionDiscussionMessageFile();

            entitySubmissionDiscussionMessageFile.setFile(file.getOriginalFilename());


            entityFileTypeRepository.findFileTypeByJournalIdAndFileTypeId(journalId, Long.valueOf(typeId)).ifPresent(entitySubmissionDiscussionMessageFile::setJournalFileType);

            entitySubmissionDiscussionMessageFile.setDiscussionMessage(entitySubmissionDiscussionMessage);

            String filePath = fileStorageService.uploadToFinalDirectory(
                commonService.buildFileStoragePath(FilePartition.workflow, journalId,
                    submissionId, WorkflowStage.SUBMISSION, "production-discussion-message", file.getOriginalFilename()), file);

            entitySubmissionDiscussionMessageFile.setFilePath(filePath);

            entitySubmissionDiscussionMessageFile.setCreatedBy(userService.getCurrentUser());

            entitySubmissionDiscussionMessageFile.setCreatedAt(Instant.now());

            entityUserRepository.findById(Objects.requireNonNull(SecurityUtils.getCurrentUserId())).ifPresent(entitySubmissionDiscussionMessageFile::setCreatedBy);

            entitySubmissionDiscussionMessageFiles.add(entitySubmissionDiscussionMessageFile);

        });

        entitySubmissionDiscussionMessageFileRepository.saveAll(entitySubmissionDiscussionMessageFiles);

        entitySubmissionDiscussionMessage.setFiles(entitySubmissionDiscussionMessageFiles);

        return submissionMapper.mapEntitySubmissionDiscussionMessageToSubmissionDiscussionMessage(entitySubmissionDiscussionMessage, journalId);
    }

    public List<SubmissionDiscussionMessage> getDiscussionChatMessages(Long journalId, Long submissionId, Long
        discussionId) {

        return submissionMapper.mapEntitySubmissionDiscussionMessagesToSubmissionDiscussionMessages(entitySubmissionDiscussionMessageRepository.getMessages(journalId, submissionId, discussionId), journalId);
    }

    public List<SubmissionContributor> addContributor(Long journalId, Long
        submissionId, List<String> userIds, String description) {
        List<EntitySubmissionContributor> entitySubmissionContributors = new ArrayList<>();

        Optional<EntitySubmission> submissionArticle = entitySubmissionRepository.findById(submissionId);

        if (submissionArticle.isEmpty()) {
            throw new RuntimeException();
        }
        EntitySubmission entitySubmission = submissionArticle.get();

        for (String userId : userIds) {

            if (getContributor(journalId, submissionId, userId) == null) {

                EntitySubmissionContributor entitySubmissionContributor = new EntitySubmissionContributor();

                Optional<EntityUser> entityUser = entityUserRepository.findById(userId);

                entityUser.ifPresent(entitySubmissionContributor::setUser);

                entitySubmissionContributor.setSubmission(entitySubmission);

                entityUserRepository.findById(Objects.requireNonNull(SecurityUtils.getCurrentUserId())).ifPresent(entitySubmissionContributor::setCreatedBy);

                entitySubmissionContributor.setCreatedAt(Instant.now());
                entitySubmissionContributor.setDeleted(false);
                entitySubmissionContributors.add(entitySubmissionContributor);
            }

            entitySubmissionContributorRepository.saveAll(entitySubmissionContributors);

            sendMailToSubmissionContributor(entitySubmissionContributors, description, journalId, entitySubmission.getTitle());

        }
        return submissionMapper.mapEntitySubmissionContributorsToSubmissionContributors(entitySubmissionContributors);

    }

    private void sendMailToSubmissionContributor
        (List<EntitySubmissionContributor> entitySubmissionContributors, String description, Long journalId, String
            submissionTitle) {

        EntityJournal entityJournal = entityJournalRepository
            .findOneByIdAndIsDeletedIsFalse(journalId)
            .orElseThrow(JournalNotFoundException::new);

        final String journalTitle = entityJournal.getKey() + " - " + entityJournal.getTitle();

//        entitySubmissionContributors
//            .forEach(entitySubmissionContributor -> mailService.sendProductionUserAddEmailFromTemplate(entitySubmissionContributor.getUser(),
//                SubmissionNotificationKeyConstants.USER_ADDED_SUBJECT, submissionTitle,
//                description, journalTitle));
    }

    private SubmissionContributor getContributor(Long journalId, Long submissionId, String userId) {

        Optional<EntitySubmissionContributor> entityUser = entitySubmissionContributorRepository.findContributorUser(journalId, submissionId, userId);

        return entityUser.map(submissionMapper::mapEntitySubmissionContributorToSubmissionContributor).orElse(null);
    }

    public List<SubmissionContributor> getContributors(Long journalId, Long submissionId) {

        return submissionMapper.mapEntitySubmissionContributorsToSubmissionContributors(entitySubmissionContributorRepository.getContributors(journalId, submissionId));
    }

    public void removeContributor(Long submissionId, String contributorId) {

        entitySubmissionContributorRepository.removeContributor(submissionId, contributorId);
    }

    public List<SubmissionFile> uploadSubmissionFile(Long journalId, Long submissionId, String oldFileIdsString, Map<String, MultipartFile> files) {

        List<Long> newFileIds = AppStringUtils.convertAsLongList(oldFileIdsString);

        List<Long> existingFileIds = entitySubmissionFileRepository.getSubmissionFileIds(journalId, submissionId);

        entitySubmissionFileRepository.deleteSubmissionFile(submissionId, journalId, ListUtils.findLongDifference(newFileIds, existingFileIds).getRemoved());

        List<EntitySubmissionFile> entitySubmissionFiles = new ArrayList<>();

        files.forEach((key, file) -> {

            String typeId = Arrays.stream(key.split("-")).collect(Collectors.toList()).get(0);

            EntitySubmissionFile entitySubmissionFile = new EntitySubmissionFile();

            entitySubmissionFile.setFile(file.getOriginalFilename());

            String filePath = fileStorageService.uploadToFinalDirectory(
                commonService.buildFileStoragePath(FilePartition.workflow, journalId,
                    submissionId, WorkflowStage.SUBMISSION, "manuscript", file.getOriginalFilename()), file);

            entitySubmissionFile.setFilePath(filePath);

            entityJournalRepository.findById(journalId).ifPresent(entitySubmissionFile::setJournal);

            entitySubmissionRepository.findById(submissionId).ifPresent(entitySubmissionFile::setSubmission);

            entitySubmissionFile.setDeleted(false);

            entitySubmissionFile.setCreatedBy(userService.getCurrentUser());

            entitySubmissionFile.setCreatedAt(Instant.now());

            entityFileTypeRepository.findFileTypeByJournalIdAndFileTypeId(journalId, Long.valueOf(typeId)).ifPresent(entitySubmissionFile::setJournalFileType);

            entitySubmissionFiles.add(entitySubmissionFile);

        });

        entitySubmissionFileRepository.saveAll(entitySubmissionFiles);

        return submissionMapper.entitySubmissionFilesToSubmissionFiles(entitySubmissionFiles);
    }

    public SubmissionFile uploadSubmissionRevisionFile(Long journalId, Long submissionId, Long originalFileId, MultipartFile file) {

        Optional<EntitySubmissionFile> entitySubmissionFile = entitySubmissionFileRepository.getSubmissionFile(journalId, submissionId, originalFileId);

        if (entitySubmissionFile.isPresent()) {

            EntitySubmissionFile originalSubmissionFile = entitySubmissionFile.get();

            EntitySubmissionFile entityRevisedSubmissionFile = new EntitySubmissionFile();

            entityJournalRepository.findById(journalId).ifPresent(entityRevisedSubmissionFile::setJournal);

            entitySubmissionRepository.findById(submissionId).ifPresent(entityRevisedSubmissionFile::setSubmission);

            entityRevisedSubmissionFile.setFile(file.getOriginalFilename());

            String filePath = fileStorageService.uploadToFinalDirectory(
                commonService.buildFileStoragePath(FilePartition.workflow, journalId,
                    submissionId, WorkflowStage.SUBMISSION, "manuscript", file.getOriginalFilename()), file);

            entityRevisedSubmissionFile.setFilePath(filePath);

            entityRevisedSubmissionFile.setJournalFileType(originalSubmissionFile.getJournalFileType());

            entityRevisedSubmissionFile.setCreatedAt(Instant.now());

            entityRevisedSubmissionFile.setCreatedBy(userService.getCurrentUser());

            entityRevisedSubmissionFile.setDeleted(false);

            entitySubmissionFileRepository.save(entityRevisedSubmissionFile);

            originalSubmissionFile.setDeleted(true);

            originalSubmissionFile.setRevised(true);

            originalSubmissionFile.setRevisedAt(Instant.now());

            originalSubmissionFile.setRevisedFileId(entityRevisedSubmissionFile.getId());

            entitySubmissionFileRepository.save(originalSubmissionFile);

            return submissionMapper.entitySubmissionFileToSubmissionFile(entityRevisedSubmissionFile);
        }

        throw new RuntimeException("Invalid File Revision Request.");
    }

    public void deleteSubmissionFile(Long journalId, Long submissionId, Long submissionFileId) {

        entitySubmissionFileRepository.deleteSubmissionFile(submissionId, journalId, Collections.singletonList(submissionFileId));
    }

    public File downloadSubmissionFilesAsZip(Long journalId, Long submissionId) throws IOException {

        List<String> filePaths = entitySubmissionFileRepository.getSubmissionFilePaths(journalId, submissionId);

        return fileStorageService.createZipFile(String.valueOf(journalId + submissionId), filePaths);
    }
}
