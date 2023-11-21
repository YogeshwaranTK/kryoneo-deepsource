package com.kjms.service.dto.requests;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * A DTO representing a password change required data - current and new password.
 */
public class ForgotPasswordEmailRequest {

    @NotNull
    @Email(message = "user,notValidEmail,Please Provide Valid Email")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
