package com.kjms.service;

import com.kjms.domain.*;
import com.kjms.repository.*;
import com.kjms.security.BadRequestAlertException;
import com.kjms.security.SecurityUtils;
import com.kjms.service.dto.*;
import com.kjms.service.errors.InvalidOrgException;
import com.kjms.service.errors.InvalidUserException;
import com.kjms.service.mapper.SubmissionFileFormatMapper;
import com.kjms.service.mapper.CommonMapper;
import com.kjms.service.mapper.CountryMapper;
import com.kjms.service.mapper.LanguageMapper;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;


@Service
public class CommonService {
    private final EntityUserRepository userRepository;
    private final EntityFileFormatRepository articleSubmissionFileFormatRepository;
    private final SubmissionFileFormatMapper submissionFileFormatMapper;
    private final LanguageMapper languageMapper;
    private final EntityLanguageRepository languageRepository;
    private final EntityCountryRepository entityCountryRepository;
    private final EntityCategoryRepository entityCategoryRepository;
    private final CountryMapper countryMapper;
    private final CommonMapper commonMapper;
    private final EntityFileTypeRepository fileTypeRepository;

    public CommonService(EntityUserRepository userRepository,
                         EntityFileFormatRepository articleSubmissionFileFormatRepository,
                         SubmissionFileFormatMapper submissionFileFormatMapper,
                         LanguageMapper languageMapper, EntityLanguageRepository languageRepository,
                         EntityCountryRepository entityCountryRepository,
                         EntityCategoryRepository entityCategoryRepository,
                         CountryMapper countryMapper,
                         CommonMapper commonMapper,
                         EntityFileTypeRepository fileTypeRepository) {
        this.userRepository = userRepository;
        this.articleSubmissionFileFormatRepository = articleSubmissionFileFormatRepository;
        this.submissionFileFormatMapper = submissionFileFormatMapper;
        this.languageMapper = languageMapper;
        this.languageRepository = languageRepository;
        this.entityCountryRepository = entityCountryRepository;
        this.entityCategoryRepository = entityCategoryRepository;
        this.countryMapper = countryMapper;
        this.commonMapper = commonMapper;
        this.fileTypeRepository = fileTypeRepository;
    }

    /**
     * Get all Article Submission formats.
     *
     * @param searchText the text to search
     * @param pageable   the paginating the list.
     * @return the list of {@link SubmissionFileFormat}.
     * @throws InvalidOrgException  if the organization isn't found.
     * @throws InvalidUserException if the user isn't found.
     */
    @Transactional(readOnly = true)
    public Page<SubmissionFileFormat> getAllFileFormats(String searchText, Pageable pageable) {
        userRepository.findOneByIdAndIsDeletedIsFalse(SecurityUtils.getCurrentUserId()).orElseThrow(InvalidUserException::new);

        Page<EntityFileFormat> fileFormats;

        if (searchText == null) {
            fileFormats = articleSubmissionFileFormatRepository.findAllByIsDeletedIsFalse(pageable);
        } else {
            fileFormats =
                articleSubmissionFileFormatRepository.findAllByIsDeletedIsFalseAndNameContainingIgnoreCase(
                    searchText,
                    pageable
                );
        }

        return submissionFileFormatMapper.entityArticleSubmissionFileFormatsToArticleSubmissionFileFormats(fileFormats);
    }

    /**
     * Gets a list of all the countries.
     *
     * @param searchText the text to search
     * @param pageable   the paginating the list.
     * @return a list of all the countries {@link Country}.
     */
    @Transactional(readOnly = true)
    public Page<Country> countryList(Pageable pageable, String searchText) {
        Page<EntityCountry> countries;

        if (searchText != null) {
            countries = entityCountryRepository.findBySearchWithPaginate(searchText, pageable);
        } else {
            countries = entityCountryRepository.findAll(pageable);
        }
        return countryMapper.entityCountriesToCountryList(countries);
    }

    /**
     * Get all Language list.
     *
     * @param userId     the current user id.
     * @param searchText the text to search language name.
     * @param pageable   the paginating the list.
     * @return all languages {@link Language}
     * @throws InvalidOrgException  if the organization isn't found.
     * @throws InvalidUserException if the user isn't found.
     */
    @Transactional(readOnly = true)
    public Page<Language> getLanguages(String userId, String searchText, Pageable pageable) {
        userRepository.findOneByIdAndIsDeletedIsFalse(userId).orElseThrow(InvalidUserException::new);

        Page<EntityLanguage> entityLanguages;

        if (searchText == null) {
            entityLanguages = languageRepository.findAll(pageable);
        } else {
            entityLanguages = languageRepository.findAllByNameContaining(searchText, pageable);
        }

        return languageMapper.entityLanguagesToLanguageList(entityLanguages);
    }


    @Transactional(readOnly = true)
    public List<Category> getCategoryList(String searchText) {
        List<EntityCategory> entityCategories;

        if (searchText == null) {
            entityCategories = entityCategoryRepository.findAll();

        } else {
            entityCategories = entityCategoryRepository.findAllByIdContainingIgnoreCaseAndNameContainingIgnoreCase(
                searchText
            );
        }

        return commonMapper.entityCategoriesToCategoryTreeNodes(entityCategories);
    }


    public String createCategory(String name) {

        if (name.isEmpty()) {
            throw new BadRequestAlertException("Invalid Name", "Category", "category.invalid.name");
        }

        EntityCategory entityCategory = new EntityCategory();
        entityCategory.setId(name.toLowerCase().trim().replace(" ", "-").replace("  ", "-")
            .replace("   ", "-"));
        entityCategory.setName(name.trim());
        entityCategory.setParentId(null);
        return entityCategoryRepository.save(entityCategory).getId();
    }

    public Long createFileType(String name) {

        if (name == null || name.isEmpty()) {
            throw new BadRequestAlertException("Invalid Name", "SubmissionFileType", "SubmissionFileType.invalid.name");
        }

        EntityFileType entityFileType = new EntityFileType();

        entityFileType.setDefault(false);
        entityFileType.setDeleted(false);
        entityFileType.setName(StringUtils.trimWhitespace(name));

        EntityFileType createdFileType = fileTypeRepository.save(entityFileType);

        return createdFileType.getId();
    }

    public Page<FileType> getFileTypeList(String searchText, Pageable pageable) {

        Page<EntityFileType> entityFileTypes;

        if (!StringUtils.hasText(searchText)) {
            entityFileTypes = fileTypeRepository.findAllByIsDeletedIsFalseAndIsDefaultIsFalse(pageable);
        } else {
            entityFileTypes = fileTypeRepository.findAllByIsDeletedIsFalseAndNameContainingIgnoreCaseAndIsDefaultIsFalse(searchText, pageable);
        }

        return commonMapper.entityFileTypeToFileTypeDto(entityFileTypes);
    }

    public String buildFileStoragePath(FilePartition partition, Long journalId, Long submissionId, WorkflowStage workflowStage, String moduleName, String originalFilename) {

        if (partition.name().equals(FilePartition.workflow.name())) {


            return FilePartition.workflow.name() + "/" + journalId + "/" + submissionId + "/" + workflowStage.name() + "/" + originalFilename;

        } else if (partition.name().equals(FilePartition.common.name())) {

            if (journalId == null) {

                throw new NotImplementedException();

            } else {
                return FilePartition.workflow.name() + "/" + journalId + "/" + originalFilename;

            }
        }

        throw new NotImplementedException();
    }
}
