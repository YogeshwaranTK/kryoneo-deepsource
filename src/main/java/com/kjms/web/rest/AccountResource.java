package com.kjms.web.rest;

import com.kjms.security.BadRequestAlertException;
import com.kjms.service.UserService;
import com.kjms.service.dto.AccountUser;
import com.kjms.service.dto.User;
import com.kjms.service.dto.requests.AuthorRegistrationRequest;
import com.kjms.service.dto.requests.ForgotPasswordEmailRequest;
import com.kjms.service.dto.requests.ChangePasswordRequest;
import com.kjms.service.dto.requests.OtpVerifyRequest;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;

/**
 * REST controller for managing the current user's account.
 */
@Tag(name = "User Account")
@RestController
@RequestMapping("/api/v1")
public class AccountResource {

    private final Logger log = LoggerFactory.getLogger(AccountResource.class);

    private final UserService userService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public AccountResource(UserService userService) {
        this.userService = userService;
    }


    /**
     * {@code GET  api/v1/authenticate} : check if the user is authenticated, and return its login.
     *
     * @param request the HTTP request.
     * @return the login if the user is authenticated.
     */
    @GetMapping("/authenticate")
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");

        return request.getRemoteUser();
    }

    /**
     * {@code GET  api/v1/account} : get the current user.
     *
     * @return the current user.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be returned.
     */
    @GetMapping("/account")
    public AccountUser getAccount() {
        return userService
            .getUserWithPermissions();
    }


    /**
     * {@code POST  /api/v1/send-forgot-pass-email-otp} : request for send otp to change password
     *
     * @return status {@code 200 (OK)}
     * @throws BadRequestAlertException {@code 400 (Bad Request)} if the email user is not found.
     */
    @PostMapping(path = "/send-forgot-password-email-otp")
    public ResponseEntity<Void> sendForgotPassEmailOtp(@Valid @RequestBody ForgotPasswordEmailRequest forgotPasswordEmailRequest) {
        log.debug("Rest Request for forgot password email otp send: {}", forgotPasswordEmailRequest.getEmail());

        userService.sendForgotPasswordEmailOtp(forgotPasswordEmailRequest.getEmail());

        return ResponseEntity
            .status(HttpStatus.OK)
            .headers(HeaderUtil.createAlert(applicationName, "user.forgotPassEmailOtpSent", forgotPasswordEmailRequest.getEmail()))
            .build();
    }

    /**
     * {@code POST  /api/v1/forgot-pass-emailotp-verify} : Verify forgot password email otp
     *
     * <p>
     *
     * @return status {@code 200 (OK)}
     * @throws BadRequestAlertException {@code 400 (Bad Request)} if the email user is not found or
     *                                  otp is wrong or otp time expired or (email otp or email otp expiry time not found in db )
     *                                  </p>.
     */
    @PostMapping(path = "/verify-email-otp")
    public ResponseEntity<Object> changePasswordEmailOtpVerify(@Valid @RequestBody OtpVerifyRequest otpVerifyRequest) {
        log.debug("Rest Request for Verify forgot password email otp: {}", otpVerifyRequest.getEmail());

        String verifyKey = userService.forgotPasswordEmailOtpVerify(otpVerifyRequest.getEmail(), otpVerifyRequest.getOtp());

        HashMap<String, String> output = new HashMap<>();

        output.put("verifyKey", verifyKey);

        return ResponseEntity
            .status(HttpStatus.OK)
            .headers(HeaderUtil.createAlert(applicationName, "user.forgotPassEmailOtpVerified", otpVerifyRequest.getEmail()))
            .body(output);
    }

    /**
     * {@code POST  /api/v1/change-password} : change password of user
     *
     * @param changePasswordRequest the password to change.
     * @return status {@code 200 (OK)}
     */
    @PutMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        log.debug("Rest Request for Change Password for user: {}", changePasswordRequest.getEmail());

        userService.changePassword(
            changePasswordRequest.getPassword(),
            changePasswordRequest.getEmail(),
            changePasswordRequest.getVerifyKey()
        );

        return ResponseEntity
            .status(HttpStatus.OK)
            .headers(HeaderUtil.createAlert(applicationName, "user.passwordChanged", changePasswordRequest.getEmail()))
            .build();
    }

    @PostMapping("/register-as-author")
    public User registerAsAuthor(@RequestBody @Valid AuthorRegistrationRequest authorRegistrationRequest) {

        return userService
            .registerAuthor(authorRegistrationRequest);
    }
}
