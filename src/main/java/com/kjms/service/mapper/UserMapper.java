package com.kjms.service.mapper;

import com.kjms.config.Constants;
import com.kjms.domain.EntityUser;
//import com.kjms.domain.EntityUserRole;
import com.kjms.service.dto.AccountUser;
import com.kjms.service.dto.User;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper for the entity {@link EntityUser} and its DTO called {@link User}.
 * <p>
 * Normal mappers are generated using MapStruct, this one is hand-coded as MapStruct
 * support is still in beta, and requires a manual step with an IDE.
 */
@Service
public class UserMapper {

    public User entityUserToUser(EntityUser entityUser) {
        User user = new User();

        user.setId(entityUser.getId());
        user.setEmail(entityUser.getEmail());
        user.setFullName(entityUser.getFullName());
        user.setActivated(entityUser.isActivated());
        user.setCountryId(entityUser.getCountry() != null ? entityUser.getCountry().getId() : null);
        user.setMobileNumber(entityUser.getMobileNumber());
        user.setStateProvince(entityUser.getStateProvince());
        user.setPhoneCode(entityUser.getPhoneCode());
        user.setCreatedDate(entityUser.getCreatedAt());
        user.setLastModifiedDate(entityUser.getLastModifiedAt());
        user.setCreatedByUserId(entityUser.getCreatedBy() != null ? entityUser.getCreatedBy().getId() : null);
        user.setLastModifiedByUserId(entityUser.getLastModifiedBy() != null ? entityUser.getLastModifiedBy().getId() : null);
        user.setGender(entityUser.getGender() != null ? entityUser.getGender().name() : null);
        user.setAddressLine1(entityUser.getAddressLine1());
        user.setAddressLine2(entityUser.getAddressLine2());
        user.setCity(entityUser.getCity());
        user.setCreatedByUserName((entityUser.getCreatedBy() != null && !entityUser.getId().equals(Constants.SUPER_ADMIN_USER_ID)) ? entityUser.getCreatedBy().getFullName() : Constants.SYSTEM);
        user.setLastModifiedByUserName(entityUser.getLastModifiedBy() != null ? entityUser.getLastModifiedBy().getFullName() : null);
        user.setPinCode(entityUser.getPinCode());
        user.setOrcid(entityUser.getOrcid());

        return user;
    }

    public User entityUserToUser(EntityUser entityUser, EntityUser currentUser) {
        User user = new User();

        user.setId(entityUser.getId());
        user.setEmail(entityUser.getEmail());
        user.setFullName(entityUser.getFullName());
        user.setActivated(entityUser.isActivated());
        user.setCountryId(entityUser.getCountry() != null ? entityUser.getCountry().getId() : null);
        user.setMobileNumber(entityUser.getMobileNumber());
        user.setStateProvince(entityUser.getStateProvince());
        user.setPhoneCode(entityUser.getPhoneCode());
        user.setCreatedDate(entityUser.getCreatedAt());
        user.setLastModifiedDate(entityUser.getLastModifiedAt());
        user.setCreatedByUserId(entityUser.getCreatedBy() != null ? entityUser.getCreatedBy().getId() : null);
        user.setLastModifiedByUserId(entityUser.getLastModifiedBy() != null ? entityUser.getLastModifiedBy().getId() : null);
        user.setInvitedUser(
            !(entityUser.equals(currentUser) || entityUser.getId().equals(Constants.SUPER_ADMIN_USER_ID))
        );
        user.setGender(entityUser.getGender() != null ? entityUser.getGender().name() : null);
        user.setAddressLine1(entityUser.getAddressLine1());
        user.setAddressLine2(entityUser.getAddressLine2());
        user.setCity(entityUser.getCity());
        user.setCreatedByUserName((entityUser.getCreatedBy() != null && !entityUser.getId().equals(Constants.SUPER_ADMIN_USER_ID)) ? entityUser.getCreatedBy().getFullName() : Constants.SYSTEM);
        user.setLastModifiedByUserName(entityUser.getLastModifiedBy() != null ? entityUser.getLastModifiedBy().getFullName() : null);
        user.setPinCode(entityUser.getPinCode());
        user.setOrcid(entityUser.getOrcid());

        return user;
    }

    public Page<User> entityUsersToUsers(Page<EntityUser> entityUsers, EntityUser currentUser) {
        return entityUsers.map(user -> entityUserToUser(user, currentUser));
    }

    public AccountUser entityUserToAccountUser(EntityUser entityUser) {
        AccountUser accountUser = new AccountUser();

        accountUser.setActivated(entityUser.isActivated());
        accountUser.setEmail(entityUser.getEmail());
        accountUser.setFullName(entityUser.getFullName());
        accountUser.setLangKey(entityUser.getLangKey());
        accountUser.setCreatedDate(entityUser.getCreatedAt());
        accountUser.setLastModifiedDate(entityUser.getLastModifiedAt());
        accountUser.setCreatedBy(entityUser.getCreatedBy() != null ? entityUser.getCreatedBy().getFullName() : Constants.SYSTEM);
        accountUser.setLastModifiedBy(entityUser.getLastModifiedBy() != null ? entityUser.getLastModifiedBy().getFullName() : null);

        return accountUser;
    }

//    public Page<User> entityUserRoleToUser(Page<EntityUserRole> entityUserRoles, EntityUser currentUser) {
//        return entityUserRoles
//            .map(EntityUserRole::getUser)
//            .map(entityUser -> entityUserToUser(entityUser, currentUser));
//    }

    public List<User> entityUsersToUsers(List<EntityUser> contributors) {

        List<User> users = new ArrayList<>();

        contributors.forEach(entityUser -> {
            users.add(entityUserToUser(entityUser));
        });

        return users;
    }

}
