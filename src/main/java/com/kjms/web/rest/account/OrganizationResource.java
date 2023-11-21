//package com.kjms.web.rest.account;
//
//import com.kjms.service.OrganizationService;
//import com.kjms.service.requests.EmailOtpResendRequest;
//import com.kjms.service.requests.UserSignUpRequest;
//import com.kjms.service.requests.OtpVerifyRequest;
//import com.microsoft.azure.storage.StorageException;
//import io.swagger.v3.oas.annotations.tags.Tag;
//
//import java.net.URISyntaxException;
//import java.security.InvalidKeyException;
//import javax.validation.Valid;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import tech.jhipster.web.util.HeaderUtil;
//
///**
// * REST controller for managing the Organization.
// */
//@Tag(name = "Organization")
//@RestController
//@RequestMapping("/api/v1/organization")
//public class OrganizationResource {
//
//    private final Logger log = LoggerFactory.getLogger(OrganizationResource.class);
//    private final OrganizationService organizationService;
//
//    public OrganizationResource(OrganizationService organizationService) {
//        this.organizationService = organizationService;
//    }
//
//    @Value("${jhipster.clientApp.name}")
//    private String applicationName;
//
//    /**
//     * {@code POST /api/v1/organization } : signup organization.
//     *
//     * @param userSignUpRequest to save details
//     * @return status{@code 200 (OK)} with a success message
//     */
//    @PostMapping("/create")
//    public ResponseEntity<Object> signUpOrganization(@Valid @RequestBody UserSignUpRequest userSignUpRequest)
//        throws URISyntaxException, InvalidKeyException, StorageException {
//        log.debug("REST request to save Organization {}:", userSignUpRequest);
//
//        organizationService.createOrganization(userSignUpRequest);
//
//        return ResponseEntity
//            .status(HttpStatus.CREATED)
//            .headers(HeaderUtil.createAlert(applicationName, "organization.registered", userSignUpRequest.getName()))
//            .build();
//    }
//
//    /**
//     * Verify OTP, which is sent to email
//     *
//     * @param otpVerifyRequest to check otp and email
//     * @return the {@link ResponseEntity} with status {@code 200 (OK)}
//     */
//    @PostMapping("/email-verify")
//    public ResponseEntity<Object> emailOtpVerify(@Valid @RequestBody OtpVerifyRequest otpVerifyRequest) {
//        log.debug("REST request to Verify Email OTP {}:", otpVerifyRequest);
//
//        organizationService.verifyEmailOtp(otpVerifyRequest);
//
//        return ResponseEntity
//            .status(HttpStatus.OK)
//            .headers(HeaderUtil.createAlert(applicationName, "organization.emailVerified", otpVerifyRequest.getEmail()))
//            .build();
//    }
//
//    /**
//     * Resend otp to email
//     *
//     * @param emailOtpResendRequest to send otp
//     * @return the {@link ResponseEntity} with status {@code 200 (OK)}
//     */
//    @PostMapping("/resend-email-otp")
//    public ResponseEntity<Object> resendEmailOtp(@Valid @RequestBody EmailOtpResendRequest emailOtpResendRequest) {
//        log.debug("REST request to Resend Email OTP {}:", emailOtpResendRequest.getEmail());
//
//        organizationService.resendEmailOtp(emailOtpResendRequest);
//
//        return ResponseEntity
//            .status(HttpStatus.OK)
//            .headers(HeaderUtil.createAlert(applicationName, "organization.emailOtpResendSuccess", emailOtpResendRequest.getEmail()))
//            .build();
//    }
//}
