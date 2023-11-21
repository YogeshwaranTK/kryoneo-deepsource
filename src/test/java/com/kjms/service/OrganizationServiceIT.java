//package com.kjms.service;
//
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//import com.kjms.IntegrationTest;
//import com.kjms.domain.EntityUser;
//import com.kjms.repository.EntityUserRepository;
//import com.kjms.service.requests.EmailOtpResendRequest;
//import com.kjms.service.requests.EmailVerifyRequest;
//import com.kjms.service.requests.OrganizationSignUpRequest;
//import java.time.Instant;
//import java.time.temporal.ChronoUnit;
//import java.util.UUID;
//import org.apache.commons.lang3.RandomStringUtils;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.transaction.annotation.Transactional;
//
///**
// * Integration tests for {@link OrganizationService}.
// */
//@IntegrationTest
//@Transactional
//class OrganizationServiceIT {
//
//    private static final String DEFAULT_ORGANIZATION_NAME = "New Company";
//    private static final String DEFAULT_PASSWORD = "password";
//    private static final String DEFAULT_ADDRESS_LINE1 = "DEFAULT_ADDRESS_LINE1";
//    private static final String DEFAULT_ADDRESS_LINE2 = "DEFAULT_ADDRESS_LINE2";
//    private static final String DEFAULT_ADDRESS_LINE3 = "DEFAULT_ADDRESS_LINE3";
//    private static final String DEFAULT_EMAIL = "email@mail.com";
//    private static final String DEFAULT_PHONE_CODE = "+91";
//
//    private static final String DEFAULT_MOBILE_NUMBER = "9876543210";
//    private static final String DEFAULT_STATE_PROVINCE = "New State";
//
//    private static final Long DEFAULT_COUNTRY_ID = 1L;
//    private static final String DEFAULT_EMAIL_OTP = "123456";
//    private static final String DEFAULT_FIRSTNAME = "john";
//    private static final String DEFAULT_LASTNAME = "doe";
//    private static final String DEFAULT_LANGUAGE_KEY = "en";
//
//    @Autowired
//    private EntityUserRepository entityUserRepository;
//
//    @Autowired
//    private OrganizationService organizationService;
//
//    private EntityUser entityUser;
//
//    /**
//     * Setups the database with one user.
//     */
//    @BeforeEach
//    public void initTest() {
//        entityUser = new EntityUser();
//        entityUser.setId(UUID.randomUUID().toString());
//        entityUser.setLogin(UUID.randomUUID().toString());
//        entityUser.setPassword(RandomStringUtils.randomAlphanumeric(60));
//        entityUser.setActivated(true);
//        entityUser.setEmail(DEFAULT_EMAIL);
//        entityUser.setEmailVerified(false);
//        entityUser.setEmailOTP(DEFAULT_EMAIL_OTP);
//        entityUser.setEmailOTPExpiryTime(Instant.now().plus(5, ChronoUnit.MINUTES));
//        entityUser.setFirstName(DEFAULT_FIRSTNAME);
//        entityUser.setLastName(DEFAULT_LASTNAME);
//        entityUser.setImageUrl("");
//        entityUser.setLangKey(DEFAULT_LANGUAGE_KEY);
//        entityUser.setDeleted(false);
//        entityUser.setMobileNumber(DEFAULT_MOBILE_NUMBER);
//        entityUser.setPhoneCode(DEFAULT_PHONE_CODE);
//    }
//
//    @Test
//    @Transactional
//    void assertThatEmailOtpNotValidAfterTimeExpired() {
//        // Initialize the database
//        entityUser.setEmailOTPExpiryTime(Instant.now().minus(10, ChronoUnit.MINUTES));
//
//        entityUserRepository.saveAndFlush(entityUser);
//
//        EmailVerifyRequest emailVerifyRequest = new EmailVerifyRequest();
//
//        emailVerifyRequest.setEmail(DEFAULT_EMAIL);
//        emailVerifyRequest.setOtp(DEFAULT_EMAIL_OTP);
//
//        Exception exception = assertThrows(BadRequestException.class, () -> organizationService.verifyEmailOtp(emailVerifyRequest));
//
//        String expectedMessage = "OTP Expired";
//        String actualMessage = exception.getMessage();
//
//        assertTrue(actualMessage.contains(expectedMessage));
//    }
//
//    @Test
//    @Transactional
//    void assertThatCorrectEmailOtpToVerify() {
//        // Initialize the database
//        entityUserRepository.saveAndFlush(entityUser);
//
//        EmailVerifyRequest emailVerifyRequest = new EmailVerifyRequest();
//
//        emailVerifyRequest.setEmail(DEFAULT_EMAIL);
//        emailVerifyRequest.setOtp("654321");
//
//        Exception exception = assertThrows(BadRequestException.class, () -> organizationService.verifyEmailOtp(emailVerifyRequest));
//
//        String expectedMessage = "Wrong OTP";
//        String actualMessage = exception.getMessage();
//
//        assertTrue(actualMessage.contains(expectedMessage));
//    }
//
//    @Test
//    @Transactional
//    void assertThatEmailMustExistToVerifyEmail() {
//        // Initialize the database
//        entityUserRepository.saveAndFlush(entityUser);
//
//        EmailVerifyRequest emailVerifyRequest = new EmailVerifyRequest();
//
//        emailVerifyRequest.setEmail("wrong@mail.com");
//        emailVerifyRequest.setOtp(DEFAULT_EMAIL_OTP);
//
//        Exception exception = assertThrows(BadRequestException.class, () -> organizationService.verifyEmailOtp(emailVerifyRequest));
//
//        String expectedMessage = "Email Not Found";
//        String actualMessage = exception.getMessage();
//
//        assertTrue(actualMessage.contains(expectedMessage));
//    }
//
//    @Test
//    @Transactional
//    void assertThatEmailShouldNotVerifiedBefore() {
//        // Initialize the database
//        entityUser.setEmailVerified(true);
//        entityUserRepository.saveAndFlush(entityUser);
//
//        EmailVerifyRequest emailVerifyRequest = new EmailVerifyRequest();
//
//        emailVerifyRequest.setEmail(DEFAULT_EMAIL);
//        emailVerifyRequest.setOtp(DEFAULT_EMAIL_OTP);
//
//        Exception exception = assertThrows(BadRequestException.class, () -> organizationService.verifyEmailOtp(emailVerifyRequest));
//
//        String expectedMessage = "Email Already Verified";
//        String actualMessage = exception.getMessage();
//
//        assertTrue(actualMessage.contains(expectedMessage));
//    }
//
//    @Test
//    @Transactional
//    void assertThatEmailMustExistForResendEmailOtp() {
//        // Initialize the database
//        entityUserRepository.saveAndFlush(entityUser);
//
//        EmailOtpResendRequest emailOtpResendRequest = new EmailOtpResendRequest();
//
//        emailOtpResendRequest.setEmail("wrong@mail.com");
//
//        Exception exception = assertThrows(BadRequestException.class, () -> organizationService.resendEmailOtp(emailOtpResendRequest));
//
//        String expectedMessage = "Email Not Found";
//        String actualMessage = exception.getMessage();
//
//        assertTrue(actualMessage.contains(expectedMessage));
//    }
//
//    @Test
//    @Transactional
//    void assertThatEmailShouldNotBeVerifiedBeforeForResendEmailOtp() {
//        // Initialize the database
//        entityUser.setEmailVerified(true);
//        entityUserRepository.saveAndFlush(entityUser);
//
//        EmailOtpResendRequest emailOtpResendRequest = new EmailOtpResendRequest();
//
//        emailOtpResendRequest.setEmail(DEFAULT_EMAIL);
//
//        Exception exception = assertThrows(BadRequestException.class, () -> organizationService.resendEmailOtp(emailOtpResendRequest));
//
//        String expectedMessage = "Email Already Verified";
//        String actualMessage = exception.getMessage();
//
//        assertTrue(actualMessage.contains(expectedMessage));
//    }
//
//    @Test
//    @Transactional
//    void assertThatOrganizationNotRegisterWithExistingEmail() {
//        // Initialize the database
//        entityUserRepository.saveAndFlush(entityUser);
//
//        OrganizationSignUpRequest organizationSignUpRequest = new OrganizationSignUpRequest();
//
//        organizationSignUpRequest.setStateProvince(DEFAULT_STATE_PROVINCE);
//        organizationSignUpRequest.setName(DEFAULT_ORGANIZATION_NAME);
//        organizationSignUpRequest.setEmail(DEFAULT_EMAIL);
//        organizationSignUpRequest.setPassword(DEFAULT_PASSWORD);
//        organizationSignUpRequest.setAddressLine1(DEFAULT_ADDRESS_LINE1);
//        organizationSignUpRequest.setAddressLine2(DEFAULT_ADDRESS_LINE2);
//        organizationSignUpRequest.setAddressLine3(DEFAULT_ADDRESS_LINE3);
//        organizationSignUpRequest.setPhoneCode(DEFAULT_PHONE_CODE);
//        organizationSignUpRequest.setMobileNumber("1236547890");
//        organizationSignUpRequest.setCountryId(DEFAULT_COUNTRY_ID);
//
//        Exception exception = assertThrows(
//            BadRequestException.class,
//            () -> organizationService.signUpOrganization(organizationSignUpRequest)
//        );
//
//        String expectedMessage = "Email Already Registered";
//        String actualMessage = exception.getMessage();
//
//        assertTrue(actualMessage.contains(expectedMessage));
//    }
//
//    @Test
//    @Transactional
//    void assertThatOrganizationNotRegisterWithExistingMobile() {
//        // Initialize the database
//        entityUserRepository.saveAndFlush(entityUser);
//
//        OrganizationSignUpRequest organizationSignUpRequest = new OrganizationSignUpRequest();
//
//        organizationSignUpRequest.setStateProvince(DEFAULT_STATE_PROVINCE);
//        organizationSignUpRequest.setName(DEFAULT_ORGANIZATION_NAME);
//        organizationSignUpRequest.setEmail("new@mail.com");
//        organizationSignUpRequest.setPassword(DEFAULT_PASSWORD);
//        organizationSignUpRequest.setAddressLine1(DEFAULT_ADDRESS_LINE1);
//        organizationSignUpRequest.setAddressLine2(DEFAULT_ADDRESS_LINE2);
//        organizationSignUpRequest.setAddressLine3(DEFAULT_ADDRESS_LINE3);
//        organizationSignUpRequest.setPhoneCode(DEFAULT_PHONE_CODE);
//        organizationSignUpRequest.setMobileNumber(DEFAULT_MOBILE_NUMBER);
//        organizationSignUpRequest.setCountryId(DEFAULT_COUNTRY_ID);
//
//        Exception exception = assertThrows(
//            BadRequestException.class,
//            () -> organizationService.signUpOrganization(organizationSignUpRequest)
//        );
//
//        String expectedMessage = "Mobile Number Already Registered";
//        String actualMessage = exception.getMessage();
//
//        assertTrue(actualMessage.contains(expectedMessage));
//    }
//}
