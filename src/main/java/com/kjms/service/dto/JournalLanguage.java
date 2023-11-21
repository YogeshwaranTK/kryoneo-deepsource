package com.kjms.service.dto;

import com.kjms.domain.EntityJournalLanguage;

import java.io.Serializable;

/**
 * A DTO representing a {@link EntityJournalLanguage} journal group.
 */
public class JournalLanguage implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;

    private String langKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }
}
