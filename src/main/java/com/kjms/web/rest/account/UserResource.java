package com.kjms.web.rest.account;

import com.kjms.config.Constants;
import com.kjms.domain.EntityUser;

import com.kjms.service.UserService;
import com.kjms.service.dto.AccountUser;
import com.kjms.service.dto.User;
import com.kjms.service.dto.requests.UserCreateRequest;
import com.kjms.service.dto.requests.UserUpdateRequest;


import java.util.*;
import javax.validation.Valid;

import com.kjms.web.rest.errors.WrongSortKeyException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;


/**
 * REST controller for managing users.
 * <p>
 * This class accesses the {@link EntityUser} entity, and needs to fetch its collection of authorities.
 * <p>
 * For a normal use-case, it would be better to have an eager relationship between User and Authority,
 * and send everything to the client side: there would be no View Model and DTO, a lot less code, and an outer-join
 * which would be good for performance.
 * <p>
 * We use a View Model and a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities, because people will
 * quite often do relationships with the user, and we don't want them to get the authorities all
 * the time for nothing (for performance reasons). This is the #1 goal: we should not impact our users'
 * application because of this use-case.</li>
 * <li> Not having an outer join causes n+1 requests to the database. This is not a real issue as
 * we have by default a second-level cache. This means on the first HTTP call we do the n+1 requests,
 * but then all authorities come from the cache, so in fact it's much better than doing an outer join
 * (which will get lots of data from the database, for each HTTP call).</li>
 * <li> As this manages users, for security reasons, we'd rather have a DTO layer.</li>
 * </ul>
 * <p>
 * Another option would be to have a specific JPA entity graph to handle this case.
 * </p>
 */
@Tag(name = "User")
@RestController
@RequestMapping("/api/v1")
public class UserResource {

    private static final List<String> ALLOWED_ORDERED_PROPERTIES = List.of("id", "fullName", "email", "activated", "createdAt", "lastModifiedAt");

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserService userService;

    public UserResource(UserService userService
    ) {
        this.userService = userService;
    }

    @PostMapping("/user/create")
    public ResponseEntity<User> createUser(
        @Valid @RequestBody UserCreateRequest userCreateRequest) {

        log.debug("REST request to save User : {}", userCreateRequest);

        return new ResponseEntity<>(userService.createUser(userCreateRequest), HttpStatus.OK);
    }

    @PutMapping("/user/update")
    public ResponseEntity<User> updateUser(@Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        log.debug("REST request to update User : {}", userUpdateRequest);

        User user = userService.updateUser(userUpdateRequest);

        return ResponseEntity.ok().headers(
            HeaderUtil.createAlert(applicationName, "user.updated", user.getFullName())
        ).build();
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(@ParameterObject Pageable pageable,
                                                  @RequestParam(name = Constants.SEARCH_TEXT, required = false) String searchText) {
        log.debug("REST request to get all User for an admin");

        if (!onlyContainsAllowedProperties(pageable)) {
            throw new WrongSortKeyException();
        }

        final Page<User> page = userService.getAllManagedUsers(searchText, pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    private boolean onlyContainsAllowedProperties(Pageable pageable) {
        return pageable.getSort().stream().map(Sort.Order::getProperty).allMatch(ALLOWED_ORDERED_PROPERTIES::contains);
    }

    @GetMapping("/user")
    public ResponseEntity<User> getUser(@RequestParam(name = "id") String userId) {
        log.debug("REST request to get User : {}", userId);

        return ResponseEntity.ok(userService.getUserById(userId));
    }


    @PutMapping("/user/remove")
    public ResponseEntity<AccountUser> deleteUser(
        @RequestParam(name = Constants.DELETED_REMARKS) String deletedRemarks, @RequestParam(name = "id") String id) {
        log.debug("REST request to remove User : {}", id);

        String userFullName = userService.deleteUser(id, deletedRemarks);

        return ResponseEntity.ok().headers(
            HeaderUtil.createAlert(applicationName, "user.removed", userFullName)
        ).build();
    }

    @PutMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestParam(name = "userId") String userId) {
        log.debug("Rest Request for Reset Password for user: {}", userId);

        String userName = userService.resetPassword(userId);

        return ResponseEntity
            .status(HttpStatus.OK)
            .headers(HeaderUtil.createAlert(applicationName, "user.passwordResetSuccessfully", userName))
            .build();
    }

    private static final List<String> ALLOWED_JOURNAL_GROUP_ORDERED_PROPERTIES = List.of("id", "name", "createdDate");

}
