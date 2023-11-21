//package com.kjms.service.mapper;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import com.kjms.domain.EntityUser;
//import com.kjms.service.dto.AccountUser;
//import com.kjms.service.dto.User;
//import java.util.*;
//import org.apache.commons.lang3.RandomStringUtils;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
///**
// * Unit tests for {@link UserMapper}.
// */
//class UserMapperTest {
//
//    private static final String DEFAULT_LOGIN = "johndoe";
//    private static final String DEFAULT_ID = UUID.randomUUID().toString();
//
//    private UserMapper userMapper;
//    private EntityUser user;
//    private AccountUser userDto;
//
//    @BeforeEach
//    public void init() {
//        userMapper = new UserMapper();
//        user = new EntityUser();
//        user.setLogin(DEFAULT_LOGIN);
//        user.setPassword(RandomStringUtils.randomAlphanumeric(60));
//        user.setActivated(true);
//        user.setEmail("johndoe@localhost");
//        user.setFirstName("john");
//        user.setLastName("doe");
//        user.setImageUrl("image_url");
//        user.setLangKey("en");
//
//        userDto = new AccountUser(user);
//    }
//
//    @Test
//    void usersToUserDTOsShouldMapOnlyNonNullUsers() {
//        List<EntityUser> users = new ArrayList<>();
//        users.add(user);
//        users.add(null);
//
//        List<User> userDTOS = userMapper.usersToUserDTOs(users);
//
//        assertThat(userDTOS).isNotEmpty().size().isEqualTo(1);
//    }
//
//    @Test
//    void userDTOsToUsersShouldMapOnlyNonNullUsers() {
//        List<AccountUser> usersDto = new ArrayList<>();
//        usersDto.add(userDto);
//        usersDto.add(null);
//
//        List<EntityUser> users = userMapper.userDTOsToUsers(usersDto);
//
//        assertThat(users).isNotEmpty().size().isEqualTo(1);
//    }
//
//    @Test
//    void userDTOsToUsersWithAuthoritiesStringShouldMapToUsersWithAuthoritiesDomain() {
//        Set<String> authoritiesAsString = new HashSet<>();
//        authoritiesAsString.add("ADMIN");
//        userDto.setAuthorities(authoritiesAsString);
//
//        List<AccountUser> usersDto = new ArrayList<>();
//        usersDto.add(userDto);
//
//        List<EntityUser> users = userMapper.userDTOsToUsers(usersDto);
//
//        assertThat(users).isNotEmpty().size().isEqualTo(1);
//        assertThat(users.get(0).getAuthorities()).isNotNull();
//        assertThat(users.get(0).getAuthorities()).isNotEmpty();
//        assertThat(users.get(0).getAuthorities().iterator().next().getName()).isEqualTo("ADMIN");
//    }
//
//    @Test
//    void userDTOsToUsersMapWithNullAuthoritiesStringShouldReturnUserWithEmptyAuthorities() {
//        userDto.setAuthorities(null);
//
//        List<AccountUser> usersDto = new ArrayList<>();
//        usersDto.add(userDto);
//
//        List<EntityUser> users = userMapper.userDTOsToUsers(usersDto);
//
//        assertThat(users).isNotEmpty().size().isEqualTo(1);
//        assertThat(users.get(0).getAuthorities()).isNotNull();
//        assertThat(users.get(0).getAuthorities()).isEmpty();
//    }
//
//    @Test
//    void userDTOToUserMapWithAuthoritiesStringShouldReturnUserWithAuthorities() {
//        Set<String> authoritiesAsString = new HashSet<>();
//        authoritiesAsString.add("ADMIN");
//        userDto.setAuthorities(authoritiesAsString);
//
//        EntityUser user = userMapper.userDTOToUser(userDto);
//
//        assertThat(user).isNotNull();
//        assertThat(user.getAuthorities()).isNotNull();
//        assertThat(user.getAuthorities()).isNotEmpty();
//        assertThat(user.getAuthorities().iterator().next().getName()).isEqualTo("ADMIN");
//    }
//
//    @Test
//    void userDTOToUserMapWithNullAuthoritiesStringShouldReturnUserWithEmptyAuthorities() {
//        userDto.setAuthorities(null);
//
//        EntityUser user = userMapper.userDTOToUser(userDto);
//
//        assertThat(user).isNotNull();
//        assertThat(user.getAuthorities()).isNotNull();
//        assertThat(user.getAuthorities()).isEmpty();
//    }
//
//    @Test
//    void userDTOToUserMapWithNullUserShouldReturnNull() {
//        assertThat(userMapper.userDTOToUser(null)).isNull();
//    }
//
//    @Test
//    void testUserFromId() {
//        assertThat(userMapper.userFromId(DEFAULT_ID).getId()).isEqualTo(DEFAULT_ID);
//        assertThat(userMapper.userFromId(null)).isNull();
//    }
//}
