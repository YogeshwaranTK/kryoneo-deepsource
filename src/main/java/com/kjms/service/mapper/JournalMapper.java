package com.kjms.service.mapper;

import com.kjms.domain.*;
import com.kjms.service.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper class responsible for converting {@link EntityJournal} to {@link Journal}
 */
@Service
public class JournalMapper {

    public Journal entityJournalToJournal(EntityJournal entityJournal) {

        Journal journal = new Journal();

        journal.setId(entityJournal.getId());
        journal.setTitle(entityJournal.getTitle());
        journal.setAbbreviation(entityJournal.getAbbreviation());
        journal.setDescription(entityJournal.getDescription());
        journal.setInitials(entityJournal.getInitials());
        journal.setOnlineIssn(entityJournal.getOnlineIssn());
        journal.setPrintIssn(entityJournal.getPrintIssn());
        journal.setInitials(entityJournal.getInitials());
        journal.setPublished(entityJournal.getPublished());
        journal.setCompletedStep(entityJournal.getCompletedStep());
        journal.setEditorChief(entityJournal.getEditorChief());
        journal.setPublishedDateTime(entityJournal.getPublishedAt());
        journal.setKey(entityJournal.getKey());
        journal.setThumbnail(entityJournal.getThumbnail());
        journal.setSummary(entityJournal.getSummary());
        journal.setGuidelines(entityJournal.getGuidelines());
        journal.setCopyrightNotice(entityJournal.getCopyrightNotice());
        journal.setFileTypes(entityJournalFileTypesToJournalFileTypes(entityJournal.getFileTypes()));
        journal.setCategories(entityJournalCategoriesToJournalCategories(entityJournal.getCategories()));
        journal.setLanguages(entityJournalLanguagesToJournalLanguages(entityJournal.getLanguages()));
        journal.setCreatedByName((entityJournal.getCreatedBy() != null) ? entityJournal.getCreatedBy().getFullName() : null);
        journal.setLastModifiedByName((entityJournal.getLastModifiedBy() != null) ? entityJournal.getLastModifiedBy().getFullName() : null);
        journal.setCreatedDate(entityJournal.getCreatedAt());
        journal.setLastModifiedDate(entityJournal.getLastModifiedAt());

        return journal;
    }

    private List<JournalLanguage> entityJournalLanguagesToJournalLanguages(List<EntityJournalLanguage> entityJournalLanguages) {

        List<JournalLanguage> journalLanguages = new ArrayList<>();

        entityJournalLanguages.forEach(entityJournalLanguage -> journalLanguages.add(entityJournalLanguageToJournalLanguage(entityJournalLanguage)));

        return journalLanguages;
    }

    private JournalLanguage entityJournalLanguageToJournalLanguage(EntityJournalLanguage entityJournalLanguage) {

        JournalLanguage journalLanguage = new JournalLanguage();

        journalLanguage.setId(entityJournalLanguage.getId());

        journalLanguage.setName(entityJournalLanguage.getLanguage().getName());

        journalLanguage.setLangKey(entityJournalLanguage.getLanguage().getLangKey());

        return journalLanguage;
    }


    public JournalCategory entityJournalCategoryToJournalCategory(EntityJournalCategory entityJournalCategory) {

        JournalCategory journalCategory = new JournalCategory();

        journalCategory.setId(entityJournalCategory.getCategoryId().getId());
        journalCategory.setName(entityJournalCategory.getCategoryId().getName());

        return journalCategory;

    }

    public JournalFileType entityJournalFileTypeToJournalFileType(EntityJournalFileType entityJournalFileType) {

        JournalFileType journalFileType = new JournalFileType();

        journalFileType.setName(entityJournalFileType.getFileType().getName());
        journalFileType.setId(entityJournalFileType.getFileType().getId());

        return journalFileType;

    }


    public List<JournalFileType> entityJournalFileTypesToJournalFileTypes(List<EntityJournalFileType> entityJournalFileTypes) {

        List<JournalFileType> journalFileTypes = new ArrayList<>();

        entityJournalFileTypes
            .forEach(entityJournalFileType -> {

                journalFileTypes.add(entityJournalFileTypeToJournalFileType(entityJournalFileType));
            });

        return journalFileTypes;
    }


    public List<JournalCategory> entityJournalCategoriesToJournalCategories(List<EntityJournalCategory> entityJournalCategories) {

        List<JournalCategory> journalCategories = new ArrayList<>();

        entityJournalCategories
            .forEach(entityJournalCategory -> {

                journalCategories.add(entityJournalCategoryToJournalCategory(entityJournalCategory));
            });

        return journalCategories;
    }


    public Page<Journal> entityJournalsToJournals(Page<EntityJournal> journals) {
        return journals.map(this::entityJournalToJournal);
    }


}
