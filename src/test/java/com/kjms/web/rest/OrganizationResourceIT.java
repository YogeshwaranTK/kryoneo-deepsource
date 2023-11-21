//package com.kjms.web.rest;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import com.kjms.IntegrationTest;
//import com.kjms.domain.EntityOrganization;
//import com.kjms.domain.EntityUser;
//import com.kjms.repository.EntityOrganizationRepository;
//import com.kjms.repository.EntityUserRepository;
//import com.kjms.service.requests.EmailOtpResendRequest;
////import com.kjms.service.requests.EmailVerifyRequest;
//import com.kjms.service.requests.OrganizationSignUpRequest;
//import java.time.Instant;
//import java.time.temporal.ChronoUnit;
//import java.util.List;
//import java.util.UUID;
//import java.util.function.Consumer;
//import org.apache.commons.lang3.RandomStringUtils;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.http.MediaType;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
///**
// * Integration tests for the {@link OrganizationResource} REST controller.
// */
//@AutoConfigureMockMvc
//@IntegrationTest
//class OrganizationResourceIT {
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
//    private EntityOrganizationRepository entityOrganizationRepository;
//
//    @Autowired
//    private EntityUserRepository entityUserRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private MockMvc restUserMockMvc;
//
//    private EntityUser entityUser;
//
//    /**
//     * Create a User.
//     * <p>
//     * This is a static method, as tests for other entities might also need it,
//     * if they test an entity which has a required relationship to the User entity.
//     */
//    public static EntityUser createUserEntity() {
//        EntityUser user = new EntityUser();
//        user.setId(UUID.randomUUID().toString());
//        user.setPassword(RandomStringUtils.randomAlphanumeric(60));
//        user.setActivated(true);
//        user.setEmail(DEFAULT_EMAIL);
//        //        user.setEmailOTP(DEFAULT_EMAIL_OTP);
//        //        user.setEmailOTPExpiryTime(Instant.now().plus(5, ChronoUnit.HOURS));
//        user.setEmailVerified(false);
//        //        user.setFirstName(DEFAULT_FIRSTNAME);
//        //        user.setLastName(DEFAULT_LASTNAME);
//        user.setImageUrl("");
//        user.setLangKey(DEFAULT_LANGUAGE_KEY);
//        //        user.setLogin(UUID.randomUUID().toString());
//        user.setDeleted(false);
//        user.setPhoneCode(DEFAULT_PHONE_CODE);
//        user.setMobileNumber(DEFAULT_MOBILE_NUMBER);
//
//        return user;
//    }
//
//    @BeforeEach
//    public void initTest() {
//        entityUser = createUserEntity();
//    }
//
//    @Test
//    void signUpOrganization() throws Exception {
//        int userDatabaseSizeBeforeCreate = entityUserRepository.findAll().size();
//
//        int organizationDatabaseSizeBeforeCreate = entityOrganizationRepository.findAll().size();
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
//        organizationSignUpRequest.setMobileNumber(DEFAULT_MOBILE_NUMBER);
//        organizationSignUpRequest.setCountryId(DEFAULT_COUNTRY_ID);
//
//        restUserMockMvc
//            .perform(
//                post("/api/v1/organization")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(organizationSignUpRequest))
//            )
//            .andExpect(status().isCreated());
//
//        // Validate the User in the database
//        assertPersistedUsers(users -> {
//            assertThat(users).hasSize(userDatabaseSizeBeforeCreate + 1);
//            EntityUser testUser = users.get(users.size() - 1);
//            //            assertThat(testUser.getFirstName()).isEqualTo(DEFAULT_ORGANIZATION_NAME);
//            assertThat(testUser.getStateProvince()).isEqualTo(DEFAULT_STATE_PROVINCE);
//            assertThat(testUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
//            assertThat(testUser.getPhoneCode()).isEqualTo(DEFAULT_PHONE_CODE);
//            assertThat(testUser.getMobileNumber()).isEqualTo(DEFAULT_MOBILE_NUMBER);
//            assertThat(testUser.getCountryId().getId()).isEqualTo(DEFAULT_COUNTRY_ID);
//            assertThat(passwordEncoder.matches(DEFAULT_PASSWORD, testUser.getPassword())).isEqualTo(true);
//        });
//
//        // Validate the Organization in the database
//        assertPersistedOrganizations(organizations -> {
//            assertThat(organizations).hasSize(organizationDatabaseSizeBeforeCreate + 1);
//            EntityOrganization testOrganization = organizations.get(organizations.size() - 1);
//            assertThat(testOrganization.getName()).isEqualTo(DEFAULT_ORGANIZATION_NAME);
//            assertThat(testOrganization.getAddressLine1()).isEqualTo(DEFAULT_ADDRESS_LINE1);
//            assertThat(testOrganization.getAddressLine2()).isEqualTo(DEFAULT_ADDRESS_LINE2);
//            assertThat(testOrganization.getAddressLine3()).isEqualTo(DEFAULT_ADDRESS_LINE3);
//        });
//    }
//
//    @Test
//    @Transactional
//    void signUpOrganizationWithExistingEmail() throws Exception {
//        entityUserRepository.saveAndFlush(entityUser);
//
//        int userDatabaseSizeBeforeCreate = entityUserRepository.findAll().size();
//
//        int organizationDatabaseSizeBeforeCreate = entityOrganizationRepository.findAll().size();
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
//        organizationSignUpRequest.setMobileNumber("1234567890");
//        organizationSignUpRequest.setCountryId(DEFAULT_COUNTRY_ID);
//
//        restUserMockMvc
//            .perform(
//                post("/api/v1/organization")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(organizationSignUpRequest))
//            )
//            .andExpect(status().isBadRequest())
//            .andExpect(content().string(org.hamcrest.Matchers.containsString("Email Already Registered")));
//
//        // Validate the User & Organization in the database
//        assertPersistedUsers(users -> assertThat(users).hasSize(userDatabaseSizeBeforeCreate));
//        assertPersistedOrganizations(organizations -> assertThat(organizations).hasSize(organizationDatabaseSizeBeforeCreate));
//    }
//
//    @Test
//    @Transactional
//    void signUpOrganizationWithExistingMobile() throws Exception {
//        entityUserRepository.saveAndFlush(entityUser);
//
//        int userDatabaseSizeBeforeCreate = entityUserRepository.findAll().size();
//
//        int organizationDatabaseSizeBeforeCreate = entityOrganizationRepository.findAll().size();
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
//        restUserMockMvc
//            .perform(
//                post("/api/v1/organization")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(organizationSignUpRequest))
//            )
//            .andExpect(status().isBadRequest())
//            .andExpect(content().string(org.hamcrest.Matchers.containsString("Mobile Number Already Registered")));
//
//        // Validate the User & Organization in the database
//        assertPersistedUsers(users -> assertThat(users).hasSize(userDatabaseSizeBeforeCreate));
//        assertPersistedOrganizations(organizations -> assertThat(organizations).hasSize(organizationDatabaseSizeBeforeCreate));
//    }
//
//    //    @Test
//    //    @Transactional
//    //    void emailOtpVerify() throws Exception {
//    //        entityUserRepository.saveAndFlush(entityUser);
//    //
//    //        EmailVerifyRequest emailVerifyRequest = new EmailVerifyRequest();
//    //
//    //        emailVerifyRequest.setOtp(DEFAULT_EMAIL_OTP);
//    //        emailVerifyRequest.setEmail(DEFAULT_EMAIL);
//    //
//    //        restUserMockMvc
//    //            .perform(
//    //                post("/api/v1/organization/email-verify")
//    //                    .contentType(MediaType.APPLICATION_JSON)
//    //                    .content(TestUtil.convertObjectToJsonBytes(emailVerifyRequest))
//    //            )
//    //            .andExpect(status().isOk());
//    //    }
//
//    @Test
//    @Transactional
//    void resendEmailOtp() throws Exception {
//        entityUserRepository.saveAndFlush(entityUser);
//
//        EmailOtpResendRequest emailOtpResendRequest = new EmailOtpResendRequest();
//
//        emailOtpResendRequest.setEmail(DEFAULT_EMAIL);
//
//        restUserMockMvc
//            .perform(
//                post("/api/v1/organization/resend-email-otp")
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .content(TestUtil.convertObjectToJsonBytes(emailOtpResendRequest))
//            )
//            .andExpect(status().isOk());
//    }
//
//    private void assertPersistedUsers(Consumer<List<EntityUser>> userAssertion) {
//        userAssertion.accept(entityUserRepository.findAll());
//    }
//
//    private void assertPersistedOrganizations(Consumer<List<EntityOrganization>> userAssertion) {
//        userAssertion.accept(entityOrganizationRepository.findAll());
//    }
//}
