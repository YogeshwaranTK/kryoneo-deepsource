package com.kjms.web.rest.account;

import com.kjms.config.Constants;
import com.kjms.service.JournalAuthorService;
import com.kjms.service.dto.requests.UsersRequest;
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

@Tag(name = "Role - Author")
@RestController
@RequestMapping("/api/v1")
public class JournalAuthorResource {


    private final JournalAuthorService journalAuthorService;
    private final Logger log = LoggerFactory.getLogger(JournalAuthorResource.class);

    private static final List<String> ALLOWED_ORDERED_PROPERTIES = List.of("user.fullName", "id");

    private boolean onlyContainsAllowedProperties(Pageable pageable) {
        return !pageable.getSort().stream()
            .map(Sort.Order::getProperty)
            .allMatch(ALLOWED_ORDERED_PROPERTIES::contains);
    }

    public JournalAuthorResource(JournalAuthorService journalAuthorService) {
        this.journalAuthorService = journalAuthorService;
    }

    @PostMapping("/author")
    public ResponseEntity<List<RoleUser>> addAsAuthor(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestBody UsersRequest usersRequest) {

        log.debug("REST request to make a user as author: {} in journal {}", usersRequest.getUserIds(), journalId);

        return new ResponseEntity<>(journalAuthorService.makeAsAuthor(journalId, usersRequest.getUserIds()), HttpStatus.OK);
    }

    @DeleteMapping("/author")
    public ResponseEntity<Void> removeAuthor(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "userId") String userId,
        @RequestParam(name = "deletedRemarks") String deletedRemarks) {

        log.debug("REST request to make a user as author: {} in journal {}", userId, journalId);

        journalAuthorService.removeAuthor(journalId, userId, deletedRemarks);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/authors")
    public ResponseEntity<List<RoleUser>> addAsAuthor(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @ParameterObject Pageable pageable) {

        if (onlyContainsAllowedProperties(pageable)) {
            throw new WrongSortKeyException();
        }

        log.debug("REST request to get all author in journal {}", journalId);

        final Page<RoleUser> roleUsers = journalAuthorService.getAuthors(journalId, pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), roleUsers);

        return new ResponseEntity<>(roleUsers.getContent(), headers, HttpStatus.OK);
    }
}
