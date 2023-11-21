package com.kjms.service.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.kjms.domain.EntityUser;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.*;
import javax.validation.constraints.*;

/**
 * A DTO representing a user, with his authorities.
 */
public class AccountUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @Size(max = 100)
    private String fullName;

    @Email
    @Size(min = 5, max = 254)
    private String email;

    @Size(max = 256)
    private String imageUrl;

    private boolean activated = false;

    @Size(min = 2, max = 10)
    private String langKey;

    private String createdBy;

    private OffsetDateTime createdDate;

    private String lastModifiedBy;

    private OffsetDateTime lastModifiedDate;

    private Set<String> authorities;
    private List<String> administrationPermissions;
    private Map<Long, List<String>> journalPermissions;

    @JsonProperty("id_token")
    private String idToken;

    public AccountUser() {
        // Empty constructor needed for Jackson.
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public Map<Long, List<String>> getJournalPermissions() {
        return journalPermissions;
    }

    public void setJournalPermissions(Map<Long, List<String>> journalPermissions) {
        this.journalPermissions = journalPermissions;
    }
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getLangKey() {
        return langKey;
    }

    public void setLangKey(String langKey) {
        this.langKey = langKey;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public OffsetDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(OffsetDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public OffsetDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(OffsetDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }
    // prettier-ignore


    public List<String> getAdministrationPermissions() {
        return administrationPermissions;
    }

    public void setAdministrationPermissions(List<String> administrationPermissions) {
        this.administrationPermissions = administrationPermissions;
    }
}
