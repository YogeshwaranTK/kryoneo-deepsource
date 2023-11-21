package com.kjms.service.mapper;

import com.kjms.domain.EntityLanguage;
import com.kjms.service.dto.Language;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * Mapper class responsible for converting {@link EntityLanguage} to {@link Language}
 */
@Service
public class LanguageMapper {

    private Language entityLanguageToLanguage(EntityLanguage entityLanguage) {
        Language language = new Language();

        language.setLangKey(entityLanguage.getLangKey());
        language.setName(entityLanguage.getName());

        return language;
    }

    public Page<Language> entityLanguagesToLanguageList(Page<EntityLanguage> entityLanguages) {
        return entityLanguages.map(this::entityLanguageToLanguage);
    }
}
