package com.kjms.service.dto.requests;

import com.kjms.config.Constants;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Represents a request object for Verify forgot password email otp
 */
public class OtpVerifyRequest {

    @NotNull
    @Email
    private String email;

    @NotNull
    @Pattern(regexp = Constants.OTP_REGEX, message = "Six digits only allowed")
    private String otp;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
