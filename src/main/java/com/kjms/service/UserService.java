package com.kjms.service;

import com.kjms.config.Constants;
import com.kjms.domain.*;
import com.kjms.repository.*;
import com.kjms.security.*;
import com.kjms.security.jwt.TokenProvider;
import com.kjms.service.dto.AccountUser;
import com.kjms.service.dto.User;
import com.kjms.service.dto.requests.*;
import com.kjms.service.errors.*;
import com.kjms.service.mail.CommonMailActionId;
import com.kjms.service.mail.CommonMailActionVariable;
import com.kjms.service.mail.MailService;
import com.kjms.service.mail.MailToVariable;
import com.kjms.service.mapper.UserMapper;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import com.kjms.service.utils.MailUtils;
import com.kjms.service.utils.PasswordUtils;
import com.kjms.web.rest.errors.ErrorConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);
    private final EntityUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityCountryRepository countryRepository;
    private final UserMapper userMapper;
    private final EntityJournalRepository journalRepository;
    private final TokenProvider tokenProvider;
    private final EntityJournalAuthorRepository entityJournalAuthorRepository;
    private final EntityJournalReviewerRepository entityJournalReviewerRepository;
    private final EntityJournalEditorialRoleRepository entityJournalEditorialRoleRepository;
    private final EntityJournalEditorialUserRepository entityJournalEditorialUserRepository;

    private final MailService mailService;

    public UserService(
        EntityUserRepository userRepository,
        PasswordEncoder passwordEncoder,
        EntityCountryRepository countryRepository,
        UserMapper userMapper,
        EntityJournalRepository journalRepository,
        TokenProvider tokenProvider,
        EntityJournalAuthorRepository entityJournalAuthorRepository,
        EntityJournalReviewerRepository entityJournalReviewerRepository,
        EntityJournalEditorialRoleRepository entityJournalEditorialRoleRepository,
        EntityJournalEditorialUserRepository entityJournalEditorialUserRepository, MailService mailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.countryRepository = countryRepository;
        this.userMapper = userMapper;
        this.journalRepository = journalRepository;
        this.tokenProvider = tokenProvider;
        this.entityJournalAuthorRepository = entityJournalAuthorRepository;
        this.entityJournalReviewerRepository = entityJournalReviewerRepository;
        this.entityJournalEditorialRoleRepository = entityJournalEditorialRoleRepository;
        this.entityJournalEditorialUserRepository = entityJournalEditorialUserRepository;
        this.mailService = mailService;
    }

    public User createUser(UserCreateRequest userCreateRequest) {

        userRepository.findOneByEmailIgnoreCaseAndIsDeletedIsFalse(userCreateRequest.getEmail())
            .ifPresent(existingUser -> {
                throw new EmailAlreadyUsedException();
            });

        EntityUser entityUser = new EntityUser();

        entityUser.setId(UUID.randomUUID().toString());

        entityUser.setFullName(userCreateRequest.getFullName());

        entityUser.setEmail(userCreateRequest.getEmail().toLowerCase());

        entityUser.setMobileNumber(userCreateRequest.getMobileNumber());

        entityUser.setPhoneCode(userCreateRequest.getPhoneCode());

        entityUser.setStateProvince(userCreateRequest.getStateProvince());

        entityUser.setLangKey(Constants.DEFAULT_LANGUAGE);

        entityUser.setCreatedAt(OffsetDateTime.now());

        entityUser.setEmailVerified(true);

        entityUser.setOtpCount(0);

        entityUser.setGender(userCreateRequest.getGender());

        entityUser.setAddressLine1(userCreateRequest.getAddressLine1());

        entityUser.setAddressLine2(userCreateRequest.getAddressLine2());

        entityUser.setCity(userCreateRequest.getCity());

        entityUser.setPinCode(userCreateRequest.getPinCode());

        entityUser.setOrcid(userCreateRequest.getOrcid());

        entityUser.setPasswordExpiryTime(OffsetDateTime.now());

        entityUser.setPassword(userCreateRequest.getPassword());

        countryRepository.findById(userCreateRequest.getCountryId()).ifPresent(entityUser::setCountry);

        entityUser.setActivated(true);

        entityUser.setDeleted(false);

        assignJournalRoles(entityUser, userCreateRequest.getRoles());

        userRepository.save(entityUser);

        return userMapper.entityUserToUser(entityUser);
    }

    private void assignJournalRoles(EntityUser entityUser, List<JournalRoleRequest> roles) {

        roles.forEach(journalRoleRequest -> {

            if (journalRoleRequest.getRoleType().equals(RoleType.AUTHOR)) {

                EntityJournalAuthor entityJournalAuthor = new EntityJournalAuthor();

                journalRepository.findById(journalRoleRequest.getJournalId()).ifPresent(entityJournalAuthor::setJournal);

                entityJournalAuthor.setUser(entityUser);

                entityJournalAuthor.setDeleted(false);

                entityJournalAuthor.setCreatedBy(entityUser);

                entityJournalAuthor.setCreatedAt(Instant.now());

                entityJournalAuthorRepository.save(entityJournalAuthor);

            } else if (journalRoleRequest.getRoleType().equals(RoleType.REVIEWER)) {

                EntityJournalReviewer entityJournalReviewer = new EntityJournalReviewer();

                journalRepository.findById(journalRoleRequest.getJournalId()).ifPresent(entityJournalReviewer::setJournal);

                entityJournalReviewer.setUser(entityUser);

                entityJournalReviewer.setDeleted(false);

                entityJournalReviewer.setCreatedAt(Instant.now());

                entityJournalReviewerRepository.save(entityJournalReviewer);

            } else if (journalRoleRequest.getRoleType().equals(RoleType.EDITORIAL_USER)) {

                EntityJournalEditorialUser entityJournalEditorialUser = new EntityJournalEditorialUser();

                entityJournalEditorialRoleRepository.findJournalEditorialRoleByJournalIdAndId(journalRoleRequest.getRoleId(), journalRoleRequest.getJournalId()).ifPresent(entityJournalEditorialUser::setEntityJournalEditorialRole);

                entityJournalEditorialUser.setUser(entityUser);

                entityJournalEditorialUser.setDeleted(false);

                entityJournalEditorialUser.setCreatedAt(Instant.now());

                entityJournalEditorialUserRepository.save(entityJournalEditorialUser);
            }
        });

    }


    public User updateUser(UserUpdateRequest userUpdateRequest) {

        EntityUser currentUser = getCurrentUser();

        Optional<EntityUser> existingUser = userRepository.findOneByEmailIgnoreCaseAndIsDeletedIsFalse(userUpdateRequest.getEmail());

        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userUpdateRequest.getId()))) {
            throw new EmailAlreadyUsedException();
        }

        EntityUser user = userRepository.findById(userUpdateRequest.getId())
            .orElseThrow(UserNotFoundException::new);

        user.setFullName(userUpdateRequest.getFullName());

        user.setEmail(userUpdateRequest.getEmail().toLowerCase());

        user.setMobileNumber(userUpdateRequest.getMobileNumber());

        user.setPhoneCode(userUpdateRequest.getPhoneCode());

        user.setStateProvince(userUpdateRequest.getStateProvince());

        user.setActivated(userUpdateRequest.getActivated());

        user.setAddressLine1(userUpdateRequest.getAddressLine1());

        user.setAddressLine2(userUpdateRequest.getAddressLine2());

        user.setCity(userUpdateRequest.getCity());

        user.setGender(userUpdateRequest.getGender());

        user.setPinCode(userUpdateRequest.getPinCode());

        user.setOrcid(userUpdateRequest.getOrcid());

        if (userUpdateRequest.getCountryId() != null) {
            Optional<EntityCountry> entityCountry = countryRepository
                .findById(userUpdateRequest.getCountryId());

            if (entityCountry.isEmpty()) {
                throw new BadRequestAlertException("Country Not Found", BadRequestEntityConstants.USER, "countryNotFound");
            }

            user.setCountry(entityCountry.get());
        }

        user.setLastModifiedBy(currentUser);

        user.setLastModifiedAt(OffsetDateTime.now());

        userRepository.save(user);

        log.debug("Changed Information for User: {}", user);

        return userMapper.entityUserToUser(user);
    }

    @Transactional
    public void changePassword(String password, String email, String verifyKey) {
        Optional<EntityUser> entityUser = userRepository.findOneByEmailIgnoreCaseAndIsDeletedIsFalse(email);

        if (entityUser.isEmpty()) {
            throw new UserNotFoundException();
        }

        if (!PasswordUtils.validate(password)) {
            throw new BadRequestAlertException(ErrorConstants.PASSWORD_REGEX_ERROR_MESSAGE, BadRequestEntityConstants.USER, "invalidPassword");
        }

        if (entityUser.get().getOtpVerificationKey().equals(verifyKey)) {
            entityUser.get().setPassword(passwordEncoder.encode(password));
            entityUser.get().setPasswordExpiryTime(OffsetDateTime.now().plus(6, ChronoUnit.MONTHS));
            entityUser.get().setActivated(true);
            entityUser.get().setEmailVerified(true);
        } else {
            throw new BadRequestAlertException("Wrong Verify Key", BadRequestEntityConstants.USER, "wrongVerifyKey");
        }

        log.debug("Changed password for User: {}", email);
    }

    /**
     * send email otp for forgotten password
     *
     * @param email to send otp
     */
    @Transactional
    public void sendForgotPasswordEmailOtp(String email) {

        Optional<EntityUser> entityUser = userRepository.findOneByEmailIgnoreCaseAndIsDeletedIsFalse(email);

        if (entityUser.isEmpty()) {
            throw new UserNotFoundException();
        }

//        if (!OffsetDateTime.now().isAfter(entityUser.get().getPasswordExpiryTime())) {
//            MailUtils.checkResendEmailOtpTime(entityUser.get());
//        }

        String otp = MailUtils.createOTP();

        entityUser.get().setOtp(otp);

        entityUser.get().setOtpCount((entityUser.get().getOtpCount() != null ? entityUser.get().getOtpCount() : 0) + 1);

        OffsetDateTime expiryTime = OffsetDateTime.now().plus(Constants.EMAIL_OTP_EXPIRY_MINUTES, ChronoUnit.MINUTES);

        entityUser.get().setOtpExpDt(expiryTime);

        mailService.triggerMail(null, CommonMailActionId.USER_LOGIN_FORGOT_PASSWORD,
            Map.of(CommonMailActionVariable.OTP, otp, CommonMailActionVariable.USER_FULL_NAME, entityUser.get().getFullName()),
            Map.of(MailToVariable.USER_EMAIL, entityUser.get().getEmail()));

        log.debug("Forgot Password Email Otp send to : {}", email);
    }

    /**
     * Verify Forgot password email otp
     *
     * @param email of the user
     * @param otp   to verify email
     * @return verifyKey to change password
     */
    @Transactional
    public String forgotPasswordEmailOtpVerify(String email, String otp) {
        Optional<EntityUser> entityUser = userRepository.findOneByEmailIgnoreCaseAndIsDeletedIsFalse(email);

        if (entityUser.isEmpty()) {
            throw new UserNotFoundException();
        }

        if (entityUser.get().getOtp() == null) {
            throw new BadRequestAlertException("Email OTP Not Found", BadRequestEntityConstants.USER, "emailOtpNotFound");
        }

        if (entityUser.get().getOtpExpDt() == null) {
            throw new BadRequestAlertException("Email Expiry Time Not Found", BadRequestEntityConstants.USER, "emailExpiryTimeNotFound");
        }

        if (entityUser.get().getOtp().equals(otp)) {
            OffsetDateTime instantBefore = entityUser.get().getOtpExpDt();

            long noOfMinutes = instantBefore.until(OffsetDateTime.now(), ChronoUnit.MINUTES);

            if (noOfMinutes > 5) {
                throw new BadRequestAlertException("OTP Expired", BadRequestEntityConstants.USER, "otpExpired");
            } else {
                String verifyKey = UUID.randomUUID().toString();

                entityUser.get().setOtpVerificationKey(verifyKey);

                log.debug("Password changed for user: {}", email);

                return verifyKey;
            }
        } else {
            throw new BadRequestAlertException("Wrong OTP", BadRequestEntityConstants.USER, "wrongOTP");
        }
    }

    @Transactional(readOnly = true)
    public Page<User> getAllManagedUsers(String searchText, Pageable pageable) {
        EntityUser currentUser = getCurrentUser();

        Page<EntityUser> entityUsers;

        if (searchText == null) {
            entityUsers = userRepository.findAllByIsDeletedIsFalse(pageable);
        } else {
            entityUsers = userRepository.findAllByIsDeletedIsFalseAndUserNameAndEmailContaining(searchText, pageable);
        }

        return userMapper.entityUsersToUsers(entityUsers, currentUser);
    }

    @Transactional(readOnly = true)
    public Page<User> getAllPublicUsers(Pageable pageable) {
        return userRepository.findAllByIdNotNullAndActivatedIsTrue(pageable).map(User::new);
    }

    @Transactional(readOnly = true)
    public User getUserById(String userId) {

        EntityUser entityUser = userRepository.findOneByIdAndIsDeletedIsFalse(userId).orElseThrow(UserNotFoundException::new);

        return userMapper.entityUserToUser(entityUser);
    }

    @Transactional(readOnly = true)
    public AccountUser getUserWithPermissions() {

        EntityUser user = getCurrentUser();

//        List<String> administrationRolePermissions = userPermissionService.getUserAdministrationPermissions(user);
//
//        Map<Long, List<String>> journalPermissions = userPermissionService.getUserJournalPermissions(user);

        String token = tokenProvider.createToken(user.getId(), SecurityUtils.getJwtExpirationDate());

        AccountUser accountUser = userMapper.entityUserToAccountUser(user);

//        accountUser.setAdministrationPermissions(administrationRolePermissions);
//        accountUser.setJournalPermissions(journalPermissions);
        accountUser.setAuthorities(Set.of("ROLE_ADMIN", "ROLE_USER"));
        accountUser.setIdToken(token);

        return accountUser;
    }

    /**
     * Gets a list of all the authorities.
     *
     * @return a list of all the authorities.
     */
    @Transactional(readOnly = true)
    public List<String> getAuthorities() {
        return new ArrayList<>();
    }

    public String deleteUser(String userId, String deletedRemarks) {
        EntityUser currentUser = getCurrentUser();

        EntityUser user = userRepository.findOneByIdAndIsDeletedIsFalse(userId)
            .orElseThrow(UserNotFoundException::new);

        if (user.equals(currentUser) || user.getId().equals(Constants.SUPER_ADMIN_USER_ID)) {
            throw new BadRequestAlertException("Invalid Request", BadRequestEntityConstants.COMMON, "invalidRequest");
        }

        user.setDeleted(true);
        user.setDeletedRemarks(deletedRemarks);
        user.setLastModifiedBy(currentUser);
        user.setLastModifiedAt(OffsetDateTime.now());

        userRepository.save(user);

//        userRoleRepository.findAllByIsDeletedIsFalseAndUser(user)
//            .forEach(entityUserRole -> {
//                entityUserRole.setDeleted(true);
//                entityUserRole.setRemovedAt(OffsetDateTime.now());
//                entityUserRole.setRemovedBy(currentUser);
//            });

        log.debug("User Deleted: {}", user.getEmail());

        return user.getFullName();
    }

    /**
     * To get Login Entity User and validate an account is active & email is verified.
     *
     * @return {@link EntityUser}
     */
    @Transactional(readOnly = true)
    public EntityUser getCurrentUser() {

        String userId = SecurityUtils.getCurrentUserId();

        if (userId != null) {

            EntityUser currentUser = userRepository.findOneByIdAndIsDeletedIsFalse(userId)
                .orElseThrow(InvalidUserException::new);

            isValidateUser(currentUser);

            return currentUser;
        } else {
            throw new InvalidUserException();
        }
    }

    /**
     * To Validate Login UserId user, exist or not, and an account is active & email is verified.
     */
    @Transactional(readOnly = true)
    public void validateCurrentUser() {
        String userId = SecurityUtils.getCurrentUserId();

        if (userId != null) {
            EntityUser currentUser = userRepository.findOneByIdAndIsDeletedIsFalse(userId)
                .orElseThrow(InvalidUserException::new);

            isValidateUser(currentUser);

        } else {
            throw new InvalidUserException();
        }
    }

    // This method is validating a user account is active & email is verified or not.
    private void isValidateUser(EntityUser entityUser) {
        if (!entityUser.isActivated()) {
            throw new UserNotActivatedException("User " + entityUser.getEmail() + " was not activated");
        }

        if (!entityUser.getEmailVerified()) {
            throw new UserEmailNotVerifiedException(entityUser.getEmail());
        }

        if (OffsetDateTime.now().isAfter(entityUser.getPasswordExpiryTime())) {
            throw new UserPasswordExpiredException(entityUser.getEmail());
        }
    }

    // Reset password for user and send mail to user with username & temporary password
    public String resetPassword(String userId) {
        EntityUser entityUser = userRepository.findOneByIdAndIsDeletedIsFalse(userId)
            .orElseThrow(UserNotFoundException::new);

        String tempPassword = PasswordUtils.generateTempPassword();

        entityUser.setPassword(passwordEncoder.encode(tempPassword));
        entityUser.setPasswordExpiryTime(OffsetDateTime.now());

        userRepository.save(entityUser);

//        mailService.sendCreationEmail(entityUser, tempPassword);

        log.debug("Password Reset for User: {}", entityUser);

        return entityUser.getFullName();
    }

    public User registerAuthor(AuthorRegistrationRequest authorRegistrationRequest) {

        userRepository.findOneByEmailIgnoreCaseAndIsDeletedIsFalse(authorRegistrationRequest.getEmail())
            .ifPresent(existingUser -> {
                throw new EmailAlreadyUsedException();
            });

        EntityUser entityUser = new EntityUser();

        entityUser.setId(UUID.randomUUID().toString());

        entityUser.setFullName(authorRegistrationRequest.getFullName());

        entityUser.setEmail(authorRegistrationRequest.getEmail().toLowerCase());

        entityUser.setMobileNumber(authorRegistrationRequest.getMobileNumber());

        entityUser.setPhoneCode(authorRegistrationRequest.getPhoneCode());

        entityUser.setStateProvince(authorRegistrationRequest.getStateProvince());

        entityUser.setLangKey(Constants.DEFAULT_LANGUAGE);

        entityUser.setCreatedAt(OffsetDateTime.now());

        entityUser.setEmailVerified(true);

        entityUser.setOtpCount(0);

        entityUser.setGender(authorRegistrationRequest.getGender());

        entityUser.setAddressLine1(authorRegistrationRequest.getAddressLine1());

        entityUser.setAddressLine2(authorRegistrationRequest.getAddressLine2());

        entityUser.setCity(authorRegistrationRequest.getCity());

        entityUser.setPinCode(authorRegistrationRequest.getPinCode());

        entityUser.setOrcid(authorRegistrationRequest.getOrcid());

        entityUser.setPasswordExpiryTime(OffsetDateTime.now());

        entityUser.setPassword(authorRegistrationRequest.getPassword());

        countryRepository.findById(authorRegistrationRequest.getCountryId()).ifPresent(entityUser::setCountry);

        entityUser.setActivated(true);

        entityUser.setDeleted(false);

        JournalRoleRequest journalRoleRequest = new JournalRoleRequest();

        journalRoleRequest.setRoleType(RoleType.AUTHOR);

        journalRoleRequest.setJournalId(authorRegistrationRequest.getJournalId());

        assignJournalRoles(entityUser, List.of(journalRoleRequest));

        userRepository.save(entityUser);

//        sendEmailForRegisteredUser(entityUser.getEmail(),entityUser.getEmail(),entityUser.getPassword());

        return userMapper.entityUserToUser(entityUser);
    }
}
