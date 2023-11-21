package com.kjms.domain;

import javax.persistence.*;

/**
 * Entity class representing a languages in Organization.
 */
@Entity
@Table(name = "jm_language")
public class EntityLanguage {

    @Id
    @Column(name = "lang_key", nullable = false, unique = true, length = 10)
    private String langKey;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
