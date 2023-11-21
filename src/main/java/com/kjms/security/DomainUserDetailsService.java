package com.kjms.security;


import com.kjms.domain.*;
import com.kjms.repository.*;


import java.time.OffsetDateTime;
import java.util.*;

import com.kjms.service.errors.UserPasswordExpiredException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

    private final EntityUserRepository userRepository;
//    private final UserPermissionService userPermissionService;

    public DomainUserDetailsService(EntityUserRepository userRepository
//        , UserPermissionService userPermissionService
    ) {
        this.userRepository = userRepository;
//        this.userPermissionService = userPermissionService;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String email) {

        log.debug("Authenticating {}", email);

        Optional<EntityUser> entityUser = userRepository
            .findOneWithAuthoritiesByEmailIgnoreCaseAndIsDeletedIsFalse(email);

        if (entityUser.isPresent()) {

            EntityUser user = entityUser.get();

            if (!user.isActivated()) {
                throw new UserNotActivatedException("User " + email + " was not activated");
            } else if (user.getPasswordExpiryTime() != null && user.getPasswordExpiryTime().isBefore(OffsetDateTime.now())) {
                throw new UserPasswordExpiredException(user.getEmail());
            } else {
                return createSpringSecurityUser(user);
            }
        } else {
            throw new UsernameNotFoundException("User with email " + email + " was not found in our records");
        }
    }

    private SpringContextUser createSpringSecurityUser(EntityUser user) {

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        return new SpringContextUser(
            user.getId(),
            user.getPassword(),
            grantedAuthorities,
            user.isActivated()
        );
    }
}
