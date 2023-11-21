package com.kjms.domain;

import javax.persistence.*;

@Entity
@Table(name = "jm_site_config")
public class EntitySiteConfig {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;
    // TODO: 24-Oct-23 @varghesh check length
    @Column(name = "value", nullable = false)
    private String value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
