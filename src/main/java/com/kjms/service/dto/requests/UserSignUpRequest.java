package com.kjms.service.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Represents a request object for creating a new Company
 */
@Schema(title = "Organization SignUp", description = "Request Body for Organization SignUp")
public class UserSignUpRequest {

    @Schema(required = true)
    @NotNull
    @NotBlank
    private String name;

    @Schema(required = true)
    @NotNull
    @NotBlank
    private String addressLine1;

    @Schema(required = true)
    @NotNull
    @NotBlank
    private String addressLine2;

    @Schema(required = true)
    @NotNull
    private String addressLine3;

    @Schema(required = true)
    @NotNull
    @NotBlank
    private String phoneCode;

    @Schema(required = true)
    @NotNull
    private Long countryId;

    @Schema(required = true)
    @NotNull
    private String stateProvince;

    @Schema(required = true)
    @NotNull
    @NotBlank
//    @Pattern(regexp = Constants.MOBILE_NUMBER_REGEX)
    private String mobileNumber;

    @Schema(required = true)
    @NotNull
    @Email
    private String email;
    @Schema(required = true)
    @NotNull
//    @Pattern(regexp = Constants.PASSWORD_REGEX)
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getAddressLine3() {
        return addressLine3;
    }

    public void setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }
}
