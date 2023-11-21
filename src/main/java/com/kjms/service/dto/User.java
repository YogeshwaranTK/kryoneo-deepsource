package com.kjms.service.dto;

import com.kjms.domain.EntityUser;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Set;

/**
 * A DTO representing a user, with only the public attributes.
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String fullName;
    private String email;
    private Long countryId;
    private Boolean activated;
    private String stateProvince;
    private String mobileNumber;
    private String phoneCode;
    private boolean invitedUser = false;
    private String createdByUserId;
    private String createdByUserName;
    private OffsetDateTime createdDate;
    private OffsetDateTime lastModifiedDate;
    private String lastModifiedByUserId;
    private String lastModifiedByUserName;
    private String gender;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private Set<Role> roles;
    private String pinCode;
    private String orcid;
    private Set<UserJournalGroup> journalGroups;

    public User() {
        // Empty constructor needed for Jackson.
    }

    public User(EntityUser user) {
        this.id = user.getId();
        // Customize it here if you need, or not, firstName/lastName/etc
        //        this.login = user.getLogin();
    }

    public String getOrcid() {
        return orcid;
    }

    public void setOrcid(String orcid) {
        this.orcid = orcid;
    }

    public Set<UserJournalGroup> getJournalGroups() {
        return journalGroups;
    }

    public void setJournalGroups(Set<UserJournalGroup> journalGroups) {
        this.journalGroups = journalGroups;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getCreatedByUserName() {
        return createdByUserName;
    }

    public void setCreatedByUserName(String createdByUserName) {
        this.createdByUserName = createdByUserName;
    }

    public String getLastModifiedByUserName() {
        return lastModifiedByUserName;
    }

    public void setLastModifiedByUserName(String lastModifiedByUserName) {
        this.lastModifiedByUserName = lastModifiedByUserName;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public boolean isInvitedUser() {
        return invitedUser;
    }

    public String getCreatedByUserId() {
        return createdByUserId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCreatedByUserId(String createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public OffsetDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(OffsetDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public OffsetDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(OffsetDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getLastModifiedByUserId() {
        return lastModifiedByUserId;
    }

    public void setLastModifiedByUserId(String lastModifiedByUserId) {
        this.lastModifiedByUserId = lastModifiedByUserId;
    }

    public void setInvitedUser(boolean invitedUser) {
        this.invitedUser = invitedUser;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setId(String id) {
        this.id = id;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserDTO{" +
            "id='" + id + '\'' +
//            ", login='" + login + '\'' +
            "}";
    }
}
