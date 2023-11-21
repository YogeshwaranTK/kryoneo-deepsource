package com.kjms.service;

import com.kjms.domain.*;
import com.kjms.repository.*;
import com.kjms.security.BadRequestAlertException;
import com.kjms.security.BadRequestEntityConstants;
import com.kjms.security.ExportConstants;
import com.kjms.security.SecurityUtils;
import com.kjms.service.dto.*;
import com.kjms.service.dto.requests.JournalBasicDetailUpdateRequest;
import com.kjms.service.dto.requests.JournalCreateRequest;
import com.kjms.service.dto.requests.JournalGuidelineUpdateRequest;
import com.kjms.service.dto.requests.JournalMetadataUpdateRequest;
import com.kjms.service.errors.InvalidOrgException;
import com.kjms.service.errors.InvalidUserException;
import com.kjms.service.errors.JournalNotFoundException;
import com.kjms.service.mapper.JournalLanguageMapper;
import com.kjms.service.mapper.JournalMapper;
import com.kjms.service.utils.WordUtils;
import com.kjms.web.rest.utils.ListUtils;
import com.kjms.web.rest.utils.dto.LongDiff;
import com.kjms.web.rest.utils.dto.StringDiff;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;


/**
 * Service class for managing Journals.
 */
@Service
@Transactional
public class JournalService {

    private final EntityJournalRepository entityJournalRepository;
    private final EntityJournalFileTypeRepository journalSubmissionFileTypeRepository;
    private final EntityJournalSubmissionLanguageRepository journalSubmissionLanguageRepository;
    private final EntityCategoryRepository categoryRepository;
    private final EntityLanguageRepository languageRepository;
    private final EntityJournalCategoryRepository journalCategoryRepository;
    private final JournalMapper journalMapper;
    private final JournalLanguageMapper journalLanguageMapper;
    private final WorkflowFileStorageService workFileStorageService;
    private final UserService userService;
    private final EntityFileTypeRepository fileTypeRepository;
    private final Logger log = LoggerFactory.getLogger(JournalService.class);
    private final EntityJournalEditorialRoleRepository entityJournalEditorialRoleRepository;
    private final EntityJournalEditorialUserRepository entityJournalEditorialUserRepository;
    private final CommonService commonService;

    public JournalService(EntityJournalRepository journalRepository,
                          EntityJournalFileTypeRepository journalFileTypeRepository,
                          EntityJournalSubmissionLanguageRepository journalSubmissionLanguageRepository,
                          EntityCategoryRepository categoryRepository,
                          EntityLanguageRepository languageRepository,
                          EntityJournalCategoryRepository journalCategoryRepository,
                          JournalMapper journalMapper,
                          JournalLanguageMapper journalLanguageMapper,
                          WorkflowFileStorageService workFileStorageService, UserService userService,
                          EntityFileTypeRepository fileTypeRepository,
                          EntityJournalEditorialRoleRepository entityJournalEditorialRoleRepository,
                          EntityJournalEditorialUserRepository entityJournalEditorialUserRepository,
                          CommonService commonService) {
        this.entityJournalRepository = journalRepository;
        this.journalSubmissionFileTypeRepository = journalFileTypeRepository;
        this.journalSubmissionLanguageRepository = journalSubmissionLanguageRepository;
        this.categoryRepository = categoryRepository;
        this.languageRepository = languageRepository;
        this.journalCategoryRepository = journalCategoryRepository;
        this.journalMapper = journalMapper;
        this.journalLanguageMapper = journalLanguageMapper;
        this.workFileStorageService = workFileStorageService;
        this.userService = userService;
        this.fileTypeRepository = fileTypeRepository;
        this.entityJournalEditorialRoleRepository = entityJournalEditorialRoleRepository;
        this.entityJournalEditorialUserRepository = entityJournalEditorialUserRepository;
        this.commonService = commonService;
    }

    public Long createJournal(JournalCreateRequest journalCreateRequest, Map<String, MultipartFile> files) {

        final EntityUser currentUser = userService.getCurrentUser();

        final String title = WordUtils.makeFirstLetterUpper(journalCreateRequest.getTitle());

        final String journalKey = StringUtils.trimAllWhitespace(journalCreateRequest.getKey()).toUpperCase();

        // Checking Title if Already Exists
        entityJournalRepository.findOneByTitleIgnoreCaseAndIsDeletedIsFalse(title).ifPresent(existingJournalTitle -> {
            throw new BadRequestAlertException("Journal Title Already Exists", BadRequestEntityConstants.JOURNAL, "titleAlreadyExists");
        });

        // Checking Key if Already Exists
        entityJournalRepository.findOneByKeyAndIsDeletedIsFalse(journalKey).ifPresent(existingJournalKey -> {
            throw new BadRequestAlertException("Journal Key Already Exists", BadRequestEntityConstants.JOURNAL, "keyAlreadyExists");
        });

        final String description = journalCreateRequest.getDescription();

        final String printIssn = StringUtils.trimWhitespace(journalCreateRequest.getPrintIssn());

        final String onlineIssn = StringUtils.trimWhitespace(journalCreateRequest.getOnlineIssn());

        final String editorChief = StringUtils.trimWhitespace(journalCreateRequest.getEditorChief());

        EntityJournal entityJournal = new EntityJournal();

        // setting initials details
        entityJournal.setCreatedBy(currentUser);
        entityJournal.setCreatedAt(Instant.now());
        entityJournal.setDeleted(false);
        entityJournal.setTitle(title);
        entityJournal.setDescription(description);
        entityJournal.setPrintIssn(printIssn);
        entityJournal.setOnlineIssn(onlineIssn);
        entityJournal.setEditorChief(editorChief);
        entityJournal.setKey(journalKey);
        entityJournal.setStorageContainerName(UUID.randomUUID().toString()); // set the unique name for file-saving folder

        EntityJournal createdJournal = entityJournalRepository.save(entityJournal);

        files.forEach((key, file) -> {

            String filePath = workFileStorageService.uploadToFinalDirectory(
                commonService.buildFileStoragePath(FilePartition.common, entityJournal.getId(),
                    null, null, "journal-files", file.getOriginalFilename()), file);


            entityJournal.setThumbnail(filePath);
        });


        entityJournal.setSummary(StringUtils.trimWhitespace(journalCreateRequest.getSummary()));

        entityJournal.setLastModifiedAt(Instant.now());
        entityJournal.setLastModifiedBy(currentUser);

        if (StringUtils.hasText(journalCreateRequest.getGuidelines())
            && journalCreateRequest.getArticleSubmissionLanguages().size() > 0
        ) {
            entityJournalRepository
                .findOneByIdNotAndTitleIgnoreCaseAndIsDeletedIsFalse(entityJournal.getId(), entityJournal.getTitle())
                .ifPresent(existingJournal -> {
                    throw new BadRequestAlertException("Journal Title Already Exists", BadRequestEntityConstants.JOURNAL, "titleAlreadyExists");
                });

            entityJournalRepository
                .findOneByIdNotAndKeyAndIsDeletedIsFalse(entityJournal.getId(), entityJournal.getKey())
                .ifPresent(existingJournal -> {
                    throw new BadRequestAlertException("Journal Key Already Exists", BadRequestEntityConstants.JOURNAL, "keyAlreadyExists");
                });

            // set step 3 completed is completed if the step is in less than 3.

        }

        entityJournal.setCompletedStep(3);

        updateJournalFileType(entityJournal, currentUser, journalCreateRequest.getFileTypes());

        updateJournalFileCategories(entityJournal, currentUser, journalCreateRequest.getCategories());

        updateJournalLanguage(entityJournal, currentUser, journalCreateRequest.getArticleSubmissionLanguages());

        entityJournal.setGuidelines(journalCreateRequest.getGuidelines());

        entityJournal.setLastModifiedAt(Instant.now());

        entityJournal.setLastModifiedBy(currentUser);

        entityJournalRepository.save(entityJournal);

//        if (entityJournalEditorialRoleRepository.hasAnyRoleAssigned(entityJournal.getId())) {

        assignJournalManager(entityJournal, currentUser);
//        }

        log.debug("Created Information for Journal: {}", createdJournal);

        return createdJournal.getId();
    }

    /**
     * Update a new journal.
     *
     * @param journalBasicDetailUpdateRequest the journal to update.
     * @throws InvalidOrgException      if the organization isn't found.
     * @throws InvalidUserException     if the user isn't found.
     * @throws BadRequestAlertException if the title or key already exists
     */
    public Journal updateJournal(Long journalId, JournalBasicDetailUpdateRequest journalBasicDetailUpdateRequest) {

        final EntityUser currentUser = userService.getCurrentUser();

        EntityJournal entityJournal = getEntityJournal(journalId);

        entityJournal.setDescription(journalBasicDetailUpdateRequest.getDescription());
        entityJournal.setOnlineIssn(journalBasicDetailUpdateRequest.getOnlineIssn());
        entityJournal.setPrintIssn(journalBasicDetailUpdateRequest.getPrintIssn());
        entityJournal.setEditorChief(journalBasicDetailUpdateRequest.getEditorChief());

        entityJournal.setLastModifiedBy(currentUser);
        entityJournal.setLastModifiedAt(Instant.now());

        entityJournalRepository.save(entityJournal);

        log.debug("Updated Information for Journal: {}", entityJournal);

        return journalMapper.entityJournalToJournal(entityJournal);
    }

    /**
     * Update Journal Metadata
     *
     * @param journalMetadataUpdateRequest the Metadata to update
     * @throws JournalNotFoundException if the Journal is not existed.
     * @throws InvalidOrgException      if the organization isn't found.
     * @throws InvalidUserException     if the user isn't found.
     */
    public Journal updateMetadata(Long journalId, JournalMetadataUpdateRequest journalMetadataUpdateRequest) {

        EntityUser currentUser = userService.getCurrentUser();

        EntityJournal entityJournal = getEntityJournal(journalId);

        if (journalMetadataUpdateRequest.getSubmissionLanguages().size() > 0
        ) {
            entityJournalRepository
                .findOneByIdNotAndTitleIgnoreCaseAndIsDeletedIsFalse(entityJournal.getId(), entityJournal.getTitle())
                .ifPresent(existingJournal -> {
                    throw new BadRequestAlertException("Journal Title Already Exists", BadRequestEntityConstants.JOURNAL, "titleAlreadyExists");
                });

            entityJournalRepository
                .findOneByIdNotAndKeyAndIsDeletedIsFalse(entityJournal.getId(), entityJournal.getKey())
                .ifPresent(existingJournal -> {
                    throw new BadRequestAlertException("Journal Key Already Exists", BadRequestEntityConstants.JOURNAL, "keyAlreadyExists");
                });

            Integer step = entityJournal.getCompletedStep();

            // set step 3 completed is completed if the step is in less than 3.
            entityJournal.setCompletedStep(step == null ? 3 : (step <= 3 ? 3 : step));
        }

        updateJournalFileType(entityJournal, currentUser, journalMetadataUpdateRequest.getFileTypes());

        updateJournalFileCategories(entityJournal, currentUser, journalMetadataUpdateRequest.getCategories());

        updateJournalLanguage(entityJournal, currentUser, journalMetadataUpdateRequest.getSubmissionLanguages());

        entityJournal.setLastModifiedAt(Instant.now());

        entityJournal.setLastModifiedBy(currentUser);

        entityJournalRepository.save(entityJournal);

        log.debug("Updated Information for Journal Guidelines: {}", journalMetadataUpdateRequest);

        return journalMapper.entityJournalToJournal(entityJournal);
    }

    /**
     * Update Journal Guidelines
     *
     * @param journalGuidelineUpdateRequest the guidelines to update
     * @throws JournalNotFoundException if the Journal is not existed.
     * @throws InvalidOrgException      if the organization isn't found.
     * @throws InvalidUserException     if the user isn't found.
     */
    public Journal updateGuideLine(Long journalId, JournalGuidelineUpdateRequest journalGuidelineUpdateRequest) {
        EntityJournal entityJournal = getEntityJournal(journalId);

        entityJournal.setGuidelines(journalGuidelineUpdateRequest.getGuidelines());
        entityJournal.setCopyrightNotice(journalGuidelineUpdateRequest.getCopyrightNotice());
        entityJournal.setLastModifiedBy(userService.getCurrentUser());
        entityJournal.setLastModifiedAt(Instant.now());

        entityJournalRepository.save(entityJournal);

        log.debug("Updated Information for Journal Guideline: {}", entityJournal);

        return journalMapper.entityJournalToJournal(entityJournal);
    }

    private void assignJournalManager(EntityJournal entityJournal, EntityUser currentUser) {

        EntityJournalEditorialRole entityJournalEditorialRole = new EntityJournalEditorialRole();

        entityJournalEditorialRole.setRoleName("Journal Manager");

        entityJournalEditorialRole.setJournal(entityJournal);

        entityJournalEditorialRole.setEditorialRoleType(EditorialRoleType.JOURNAL_MANAGER);

        entityJournalEditorialRole.setSubmissionEnabled(true);

        entityJournalEditorialRole.setReviewEnabled(true);

        entityJournalEditorialRole.setCopyEditingEnabled(true);

        entityJournalEditorialRole.setProductionEnabled(true);

        entityJournalEditorialRole.setCreatedBy(currentUser);

        entityJournalEditorialRole.setCreatedAt(Instant.now());

        entityJournalEditorialRole.setDeleted(false);

        entityJournalEditorialRole.setDefault(true);

        entityJournalEditorialRoleRepository.save(entityJournalEditorialRole);

        EntityJournalEditorialUser entityJournalEditorialUser = new EntityJournalEditorialUser();

        entityJournalEditorialUser.setEntityJournalEditorialRole(entityJournalEditorialRole);

        entityJournalEditorialUser.setUser(currentUser);

        entityJournalEditorialUser.setDeleted(false);

        entityJournalEditorialUser.setCreatedAt(Instant.now());

        entityJournalEditorialUser.setCreatedBy(currentUser);

        entityJournalEditorialUserRepository.save(entityJournalEditorialUser);
    }

    private void updateJournalLanguage(EntityJournal entityJournal, EntityUser currentUser, List<String> articleSubmissionLanguages) {

        List<String> existingLanguageIds = journalSubmissionLanguageRepository
            .findAllJournalIdsByJournalAndIsDeletedIsFalse(entityJournal.getId());

        StringDiff difference = ListUtils.findStringDifference(existingLanguageIds, articleSubmissionLanguages);

        journalSubmissionLanguageRepository.deleteLanguages(entityJournal.getId(), difference.getRemoved());

        List<EntityJournalLanguage> entityJournalLanguages = new ArrayList<>();

        difference.getAdded().forEach(fileCategoryId -> {

            EntityJournalLanguage entityJournalLanguage = new EntityJournalLanguage();

            entityJournalLanguage.setDeleted(false);

            languageRepository.findById(fileCategoryId).ifPresent(entityJournalLanguage::setLanguage);

            entityJournalLanguage.setJournal(entityJournal);

            entityJournalLanguage.setCreatedAt(OffsetDateTime.now());

            entityJournalLanguage.setCreatedBy(currentUser);

            entityJournalLanguages.add(entityJournalLanguage);
        });

        journalSubmissionLanguageRepository.saveAll(entityJournalLanguages);
    }

    private void updateJournalFileCategories(EntityJournal entityJournal, EntityUser currentUser, List<String> categoryIds) {

        List<String> existingCategoryIds = journalCategoryRepository
            .findAllCategoryId_IdByIsDeletedIsFalseAndJournalId(entityJournal)
            .collect(Collectors.toList());
        StringDiff difference = ListUtils.findStringDifference(existingCategoryIds, categoryIds);

        journalCategoryRepository.deleteCategories(entityJournal.getId(), difference.getRemoved());

        List<EntityJournalCategory> entityJournalCategories = new ArrayList<>();

        difference.getAdded().forEach(fileCategoryId -> {

            EntityJournalCategory entityJournalCategory = new EntityJournalCategory();

            entityJournalCategory.setDeleted(false);

            categoryRepository.findById(fileCategoryId).ifPresent(entityJournalCategory::setCategoryId);

            entityJournalCategory.setJournal(entityJournal);

            entityJournalCategory.setCreatedAt(Instant.now());

            entityJournalCategory.setCreatedBy(currentUser);

            entityJournalCategories.add(entityJournalCategory);
        });

        journalCategoryRepository.saveAll(entityJournalCategories);
    }

    private void updateJournalFileType(EntityJournal entityJournal, EntityUser currentUser, List<Long> fileTypeIds) {

        List<Long> entityJournalFileExistingTypes = journalSubmissionFileTypeRepository.
            findAllByIsDeletedIsFalseAndJournal(entityJournal.getId()).stream().map
                (entityJournalFileType -> entityJournalFileType.getFileType().getId()).collect(Collectors.toList());

        List<Long> entityJournalFileDefaultTypes = fileTypeRepository.findAllByIsDeletedFalseAndIsDefaultIsTrue();

        System.out.println(entityJournalFileDefaultTypes);

        entityJournalFileDefaultTypes.forEach(defaultType -> {
            if (!fileTypeIds.contains(defaultType)) {
                fileTypeIds.add(defaultType);
            }
        });

        LongDiff difference = ListUtils.findLongDifference(entityJournalFileExistingTypes, fileTypeIds);

        journalSubmissionFileTypeRepository.deleteFileTypes(entityJournal.getId(), difference.getRemoved());

        List<EntityJournalFileType> fileTypes = new ArrayList<>();

        difference.getAdded().forEach(fileTypeId -> {

            EntityJournalFileType entityJournalFileType = new EntityJournalFileType();

            entityJournalFileType.setJournal(entityJournal);
            entityJournalFileType.setDeleted(false);
            entityJournalFileType.setCreatedBy(currentUser);
            entityJournalFileType.setCreatedAt(Instant.now());
            fileTypeRepository.findById(fileTypeId).ifPresent(entityJournalFileType::setFileType);
            fileTypes.add(entityJournalFileType);

        });

        journalSubmissionFileTypeRepository.saveAll(fileTypes);
    }

    /**
     * Get a specific journal by id, and return the specific journal.
     *
     * @param journalId journal to get.
     * @return {@link Journal}.
     * @throws JournalNotFoundException if the Journal is not existed.
     * @throws InvalidOrgException      if the organization isn't found.
     * @throws InvalidUserException     if the user isn't found.
     */
    @Transactional(readOnly = true)
    public Journal getJournalById(Long journalId) {

        EntityJournal journal = entityJournalRepository.findById(journalId).orElseThrow();
        
        return journalMapper.entityJournalToJournal(journal);
    }

    /**
     * Gets a list of all the journals.
     *
     * @param pageable   pageable for pagination
     * @param searchText is for got name related journals
     * @return a list of all the {@link Journal} with paginate.
     * @throws InvalidOrgException  if the organization isn't found.
     * @throws InvalidUserException if the user isn't found.
     */
    @Transactional(readOnly = true)
    public Page<Journal> getJournals(String searchText, Pageable pageable) {


        return journalMapper.entityJournalsToJournals(entityJournalRepository.findAllByTitleAndKeyContainingAndIsDeletedIsFalse(SecurityUtils.getCurrentUserId(), searchText, pageable));
    }

    /**
     * Update isDeleted as true for Journal delete.
     *
     * @param journalId      the journal to delete.
     * @param deletedRemarks the remarks for delete.
     * @throws JournalNotFoundException if the Journal is not existed.
     * @throws InvalidOrgException      if the organization isn't found.
     * @throws InvalidUserException     if the user isn't found.
     */
    public String deleteJournal(Long journalId, String key, String deletedRemarks) {
        EntityUser currentUser = userService.getCurrentUser();

        EntityJournal entityJournal = getEntityJournal(journalId);

        if (!entityJournal.getKey().equals(key.toUpperCase())) {
            throw new BadRequestAlertException("Invalid Key", BadRequestEntityConstants.JOURNAL, "invalidKey");
        }

        entityJournal.setDeleted(true);
        entityJournal.setDeletedRemarks(deletedRemarks);
        entityJournal.setLastModifiedBy(currentUser);
        entityJournal.setLastModifiedAt(Instant.now());

        entityJournalRepository.save(entityJournal);

        log.debug("Deleted Journal: {}", entityJournal.getTitle());

        return entityJournal.getTitle();
    }

    /**
     * Create Excel with journal details
     *
     * @return Name of the file
     * @throws IOException          file not able to save
     * @throws InvalidOrgException  if the organization isn't found.
     * @throws InvalidUserException if the user isn't found.
     */
    @Transactional(readOnly = true)
    public JasperPrint export(String type) throws IOException, JRException, ParseException {
        userService.validateCurrentUser();

        List<Journal> journals = entityJournalRepository.findAllByIsDeletedIsFalse().stream().map(journalMapper::entityJournalToJournal).collect(Collectors.toList());

        if (type.equals(ExportConstants.EXCEL)) {
            File file = ResourceUtils.getFile(ExportConstants.JASPER_EXCEL_FILE_PATH);
            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(journals);

            return JasperFillManager.fillReport(jasperReport, null, dataSource);
        } else {
            File file = ResourceUtils.getFile(ExportConstants.JASPER_PDF_FILE_PATH);
            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(journals);

            return JasperFillManager.fillReport(jasperReport, null, dataSource);
        }
    }

    /**
     * Get all Journal Languages.
     *
     * @param pageable   pageable for pagination
     * @param searchText text to search language name & key.
     * @return a list of all the {@link JournalLanguage} with paginate.
     * @throws InvalidOrgException  if the organization isn't found.
     * @throws InvalidUserException if the user isn't found.
     */
    @Transactional(readOnly = true)
    public Page<JournalLanguage> getJournalLangList(Long journalId, String searchText, Pageable pageable) {
        userService.validateCurrentUser();

        EntityJournal entityJournal = getEntityJournal(journalId);

        Page<EntityJournalLanguage> entityJournalLanguages;

        if (searchText == null) {
            entityJournalLanguages = journalSubmissionLanguageRepository.findAllByJournalIdAndIsDeletedIsFalse(entityJournal.getId(), pageable);
        } else {
            entityJournalLanguages = journalSubmissionLanguageRepository.findAllByJournalIdAndIsDeletedIsFalseAndTextContaining(entityJournal, searchText, pageable);
        }

        return journalLanguageMapper.entityJournalLanguagesToJournalLanguages(entityJournalLanguages);
    }

    /**
     * Get Journal Entity by Journal ID.
     *
     * @param journalId the Journal ID to get.
     * @return the Journal Entity {@link EntityJournal}
     * @throws JournalNotFoundException if the Journal doesn't Present.
     */
    public EntityJournal getEntityJournal(Long journalId) {
        return entityJournalRepository.findOneByIdAndIsDeletedIsFalse(journalId)
            .orElseThrow(JournalNotFoundException::new);
    }

    public List<JournalFileType> getJournalFileTypes(Long journalId) {

        return journalMapper.entityJournalFileTypesToJournalFileTypes(journalSubmissionFileTypeRepository.getJournalFileTypes(journalId));
    }


}
