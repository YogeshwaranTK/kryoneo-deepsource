package com.kjms.domain;

import java.time.OffsetDateTime;
import java.util.Locale;
import javax.persistence.*;

/**
 * A user.
 */
@Entity
@Table(name = "jm_user")
public class EntityUser {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private String id;
    @Column(name = "password_hash", length = 60)
    private String password;

    @Column(name = "full_name", length = 100, nullable = false)
    private String fullName;

    // TODO: 24-Oct-23 @vargesh check length
    @Column(name = "email", length = 254, nullable = false)
    private String email;

    @Column(name = "activated", nullable = false)
    private boolean activated;

    @Column(name = "lang_key", length = 3, nullable = false)
    private String langKey;

    @Column(name = "image_url", length = 256)
    private String imageUrl;

    @Column(name = "state_province")
    private String stateProvince;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", foreignKey = @ForeignKey(name = "fk_jm_user_country_id"))
    private EntityCountry country;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @Column(name = "deleted_remarks", columnDefinition = "TEXT")
    private String deletedRemarks;

    @Column(name = "otp", length = 6)
    private String otp;

    @Column(name = "otp_expiry_dt")
    private OffsetDateTime otpExpDt;

    @Column(name = "otp_verification_key")
    private String otpVerificationKey;

    @Column(name = "is_email_verified", nullable = false)
    private Boolean emailVerified;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", foreignKey = @ForeignKey(name = "fk_jm_user_created_by"), updatable = false)
    private EntityUser createdBy;

    @Column(name = "password_expiry_time", nullable = false)
    private OffsetDateTime passwordExpiryTime;
    @Column(name = "created_at", updatable = false, nullable = false)
    private OffsetDateTime createdAt;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "last_modified_by", foreignKey = @ForeignKey(name = "fk_jm_user_last_modified_by"))
    private EntityUser lastModifiedBy;

    @Column(name = "last_modified_at")
    private OffsetDateTime lastModifiedAt;

    @Column(name = "mobile_number", length = 30)
    private String mobileNumber;

    @Column(name = "phone_code", length = 11)
    private String phoneCode;

    @Column(name = "otp_count", length = 15)
    private Integer otpCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 10)
    private Gender gender;

    @Column(name = "city", length = 200)
    private String city;
    @Column(name = "address_line1", columnDefinition = "TEXT")
    private String addressLine1;
    @Column(name = "address_line2", columnDefinition = "TEXT")
    private String addressLine2;
    @Column(name = "pin_code", length = 20)
    private String pinCode;
    // TODO: 13-Nov-23 @varghesh (check length)
    @Column(name = "orcid", length = 20)
    private String orcid;

    public Locale getLocale() {
        return Locale.forLanguageTag(this.getLangKey());
    }

    public String getOrcid() {
        return orcid;
    }

    public void setOrcid(String orcid) {
        this.orcid = orcid;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
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

    public Integer getOtpCount() {
        return otpCount;
    }

    public void setOtpCount(Integer otpCount) {
        this.otpCount = otpCount;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    public EntityCountry getCountry() {
        return country;
    }

    public void setCountry(EntityCountry country) {
        this.country = country;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public String getDeletedRemarks() {
        return deletedRemarks;
    }

    public void setDeletedRemarks(String deletedRemarks) {
        this.deletedRemarks = deletedRemarks;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public EntityUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(EntityUser createdBy) {
        this.createdBy = createdBy;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public EntityUser getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(EntityUser lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public OffsetDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }

    public void setLastModifiedAt(OffsetDateTime lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public OffsetDateTime getOtpExpDt() {
        return otpExpDt;
    }

    public void setOtpExpDt(OffsetDateTime otpExpDt) {
        this.otpExpDt = otpExpDt;
    }

    public String getOtpVerificationKey() {
        return otpVerificationKey;
    }

    public void setOtpVerificationKey(String otpVerificationKey) {
        this.otpVerificationKey = otpVerificationKey;
    }

    public OffsetDateTime getPasswordExpiryTime() {
        return passwordExpiryTime;
    }

    public void setPasswordExpiryTime(OffsetDateTime passwordExpiryTime) {
        this.passwordExpiryTime = passwordExpiryTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EntityUser)) {
            return false;
        }
        return id != null && id.equals(((EntityUser) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }
    // prettier-ignore

}
