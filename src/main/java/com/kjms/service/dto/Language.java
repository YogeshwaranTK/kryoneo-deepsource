package com.kjms.service.dto;

import com.kjms.domain.EntityLanguage;
import java.io.Serializable;

/**
 * A DTO for the {@link EntityLanguage} entity
 */
public class Language implements Serializable {

    private static final long serialVersionUID = 1L;

    private String langKey;
    private String name;

    public Language() {}

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
