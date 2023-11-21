//package com.kjms.service;
//
//import com.kjms.config.Constants;
//import com.kjms.domain.*;
//import com.kjms.repository.*;
//import com.kjms.security.BadRequestAlertException;
//import com.kjms.security.BadRequestEntityConstants;
//import com.kjms.service.errors.EmailAlreadyUsedException;
//import com.kjms.service.mail.MailService;
//import com.kjms.service.requests.EmailOtpResendRequest;
//import com.kjms.service.requests.UserSignUpRequest;
//import com.kjms.service.requests.OtpVerifyRequest;
//import com.kjms.service.utils.MailUtils;
//import com.microsoft.azure.storage.StorageException;
//
//import java.net.URISyntaxException;
//import java.security.InvalidKeyException;
//import java.time.OffsetDateTime;
//import java.time.temporal.ChronoUnit;
//import java.util.*;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
///**
// * Service class for managing Companies.
// */
//@Service
//@Transactional
//public class OrganizationService {
//    private final MailService mailService;
//
//    private final EntityUserRepository userRepository;
//
//    private final EntityCountryRepository countryRepository;
//
//    private final PasswordEncoder passwordEncoder;
//
//    private final Logger log = LoggerFactory.getLogger(OrganizationService.class);
//
//    public OrganizationService(MailService mailService,
//                               EntityUserRepository userRepository,
//                               EntityCountryRepository entityCountryRepository,
//                               PasswordEncoder passwordEncoder) {
//        this.mailService = mailService;
//        this.userRepository = userRepository;
//        this.countryRepository = entityCountryRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    /**
//     * signUp Organization.
//     *
//     * @param userSignUpRequest request body to update Organization.
//     * @throws BadRequestAlertException if the email is already registered or Mobile Number Already Registered or Country Not Found
//     */
//    public void createOrganization(UserSignUpRequest userSignUpRequest)
//        throws URISyntaxException, InvalidKeyException, StorageException {
//
//        //check email if already registered for user.
//        EntityUser existingEmailUser = getUserByEmail(userSignUpRequest.getEmail(), false);
//
//        if (existingEmailUser != null) {
//            throw new EmailAlreadyUsedException();
//        }
//
//        EntityUser user = new EntityUser();
//
//        if (userSignUpRequest.getCountryId() != null) {
//            Optional<EntityCountry> country = countryRepository.findById(userSignUpRequest.getCountryId());
//
//            if (country.isEmpty()) {
//                throw new BadRequestAlertException("Country Not Found", BadRequestEntityConstants.ORGANIZATION, "countryNotFound");
//            }
//
//            user.setCountry(country.get());
//        }
//
//        user.setId(UUID.randomUUID().toString());
//        user.setCreatedAt(OffsetDateTime.now());
//        user.setEmail(userSignUpRequest.getEmail());
//        user.setMobileNumber(userSignUpRequest.getMobileNumber());
//        user.setDeleted(false);
//        user.setPhoneCode(userSignUpRequest.getPhoneCode());
//        user.setActivated(true);
//        user.setPassword(passwordEncoder.encode(userSignUpRequest.getPassword()));
//        user.setFullName(userSignUpRequest.getName());
//        user.setLangKey(Constants.DEFAULT_LANGUAGE);
//        user.setEmailVerified(false);
//        user.setOtpCount(0);
//
//        String otp = MailUtils.createOTP();
//
//        user.setOtp(otp);
//
//        user.setOtpExpDt(OffsetDateTime.now().plus(Constants.EMAIL_OTP_EXPIRY_MINUTES, ChronoUnit.MINUTES));
//
//        userRepository.save(user);
//
//        // sent the otp to email for verify
//        mailService.sendEmailOtp(userSignUpRequest.getEmail(), otp);
//
//        log.debug("Created Information for Organization: {}", user);
//    }
//
//    /**
//     * Verify OTP, which is sent to email
//     *
//     * @param otpVerifyRequest to check email otp
//     * @throws BadRequestAlertException if the Email User Not Found or Email Already Verified or Email, OTP Not Found
//     *                                  or Wrong OTP or OTP Expired or Email Expiry Time Not Found
//     */
//    public void verifyEmailOtp(OtpVerifyRequest otpVerifyRequest) {
//        EntityUser user = getUserByEmail(otpVerifyRequest.getEmail(), true);
//
//        if (user.getEmailVerified()) {
//            throw new BadRequestAlertException("Email Already Verified", BadRequestEntityConstants.ORGANIZATION, "emailAlreadyVerified");
//        }
//
//        if (user.getOtp() == null) {
//            throw new BadRequestAlertException("Email OTP Not Found", BadRequestEntityConstants.ORGANIZATION, "emailOtpNotFound");
//        }
//
//        if (user.getOtpExpDt() == null) {
//            throw new BadRequestAlertException(
//                "Email Expiry Time Not Found",
//                BadRequestEntityConstants.ORGANIZATION,
//                "emailExpiryTimeNotFound"
//            );
//        }
//
//        if (user.getOtp().equals(otpVerifyRequest.getOtp())) {
//            OffsetDateTime instantBefore = user.getOtpExpDt();
//
//            long noOfMinutes = instantBefore.until(OffsetDateTime.now(), ChronoUnit.MINUTES);
//
//            if (noOfMinutes > Constants.EMAIL_OTP_EXPIRY_MINUTES) {
//                throw new BadRequestAlertException("OTP Expired", BadRequestEntityConstants.ORGANIZATION, "otpExpired");
//            } else {
//                user.setEmailVerified(true);
//
//                userRepository.save(user);
//            }
//        } else {
//            throw new BadRequestAlertException("Wrong OTP", BadRequestEntityConstants.ORGANIZATION, "wrongOTP");
//        }
//    }
//
//    /**
//     * Resend OTP to email
//     *
//     * @param emailOtpResendRequest to send OTP
//     */
//    public void resendEmailOtp(EmailOtpResendRequest emailOtpResendRequest) {
//
//        EntityUser entityUser = getUserByEmail(emailOtpResendRequest.getEmail(), true);
//
//        if (entityUser.getEmailVerified()) {
//            throw new BadRequestAlertException("Email Already Verified", BadRequestEntityConstants.ORGANIZATION, "emailAlreadyVerified");
//        }
//
//        MailUtils.checkResendEmailOtpTime(entityUser);
//
//        String otp = MailUtils.createOTP();
//
//        entityUser.setOtp(otp);
//
//        entityUser.setOtpExpDt(OffsetDateTime.now().plus(Constants.EMAIL_OTP_EXPIRY_MINUTES, ChronoUnit.MINUTES));
//
//        entityUser.setOtpCount((entityUser.getOtpCount() != null ? entityUser.getOtpCount() : 0) + 1);
//
//        userRepository.save(entityUser);
//
//        // sent the otp to email
//        mailService.sendEmailOtp(emailOtpResendRequest.getEmail(), otp);
//    }
//
//    private EntityUser getUserByEmail(String email, boolean validate) {
//        Optional<EntityUser> entityUser = userRepository.findOneByEmailIgnoreCaseAndIsDeletedIsFalse(
//            email
//        );
//
//        if (validate) {
//            if (entityUser.isEmpty()) {
//                throw new BadRequestAlertException("Email Not Found", BadRequestEntityConstants.ORGANIZATION, "emailNotFound");
//            } else {
//                return entityUser.get();
//            }
//        } else {
//            return entityUser.orElse(null);
//        }
//    }
//}
