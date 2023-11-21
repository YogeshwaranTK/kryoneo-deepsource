package com.kjms.service.mapper;

import com.kjms.domain.EntityJournalLanguage;
import com.kjms.service.dto.JournalLanguage;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper class responsible for converting {@link EntityJournalLanguage} to {@link JournalLanguage}
 */
@Service
public class JournalLanguageMapper {

    public JournalLanguage entityJournalLanguageToJournalLanguage(EntityJournalLanguage entityJournalLanguage) {

        JournalLanguage journalLanguage = new JournalLanguage();

        journalLanguage.setId(entityJournalLanguage.getId());

        journalLanguage.setLangKey(entityJournalLanguage.getLanguage().getLangKey());

        journalLanguage.setName(entityJournalLanguage.getLanguage().getName());

        return journalLanguage;
    }

    public Page<JournalLanguage> entityJournalLanguagesToJournalLanguages(Page<EntityJournalLanguage> entityJournalLanguages) {
        return entityJournalLanguages.map(this::entityJournalLanguageToJournalLanguage);
    }

    public List<JournalLanguage> entityJournalLanguagesToJournalLanguages(List<EntityJournalLanguage> entityJournalLanguages) {
        return entityJournalLanguages.stream().map(this::entityJournalLanguageToJournalLanguage)
            .collect(Collectors.toList());
    }
}
