package com.kjms.web.rest.account;

import com.kjms.config.Constants;
import com.kjms.service.EditorialUserService;
import com.kjms.service.dto.requests.EditorialUserCreateRequest;
import com.kjms.service.dto.RoleUser;
import com.kjms.web.rest.errors.WrongSortKeyException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

import java.util.List;

@Tag(name = "Role - EditorialUser")
@RestController
@RequestMapping("/api/v1")
public class EditorialUserResource {

    private final Logger log = LoggerFactory.getLogger(EditorialUserResource.class);

    private final EditorialUserService editorialUserService;

    private static final List<String> ALLOWED_ORDERED_PROPERTIES = List.of("user.fullName", "id");

    private boolean onlyContainsAllowedProperties(Pageable pageable) {
        return !pageable.getSort().stream()
            .map(Sort.Order::getProperty)
            .allMatch(ALLOWED_ORDERED_PROPERTIES::contains);
    }

    public EditorialUserResource(EditorialUserService editorialUserService) {
        this.editorialUserService = editorialUserService;
    }

    @PostMapping("/editorial-users")
    public ResponseEntity<List<RoleUser>> createEditorialUser(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestBody EditorialUserCreateRequest editorialUserCreateRequest) {

        log.debug("REST request to create Editorial Users: {} in journal {}", journalId);

        return new ResponseEntity<>(editorialUserService.createEditorialUsers(editorialUserCreateRequest, journalId), HttpStatus.OK);
    }

    @GetMapping("/editorial-users")
    public ResponseEntity<List<RoleUser>> getEditorialUsers(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "searchText", required = false) String searchText,
        @ParameterObject Pageable pageable) {

        log.debug("REST request to get Editorial Users: {} in journal {}", journalId);

        if (onlyContainsAllowedProperties(pageable)) {
            throw new WrongSortKeyException();
        }

        final Page<RoleUser> editorialUsersList = editorialUserService.getEditorialUsers(journalId,searchText, pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), editorialUsersList);

        return new ResponseEntity<>(editorialUsersList.getContent(), headers, HttpStatus.OK);
    }

    @DeleteMapping("/editorial-users")
    public ResponseEntity<Void> deleteEditorialUsers(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "userId") String userId,
        @RequestParam(name = "deletedRemarks") String deletedRemarks) {

        log.debug("REST request to delete Editorial Users: {} in journal {}", journalId);

        editorialUserService.removeEditorialUsers(userId, journalId, deletedRemarks);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
