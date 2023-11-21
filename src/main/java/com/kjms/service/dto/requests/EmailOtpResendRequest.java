package com.kjms.service.dto.requests;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * Represents a request object for send a otp to email
 */
public class EmailOtpResendRequest {

    @NotNull
    @Email
    private String email;

    public EmailOtpResendRequest() {
        // Empty constructor needed for Jackson.
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
