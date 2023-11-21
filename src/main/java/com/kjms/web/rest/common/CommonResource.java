package com.kjms.web.rest.common;

import com.kjms.config.Constants;
import com.kjms.security.SecurityUtils;
import com.kjms.service.CommonService;
import com.kjms.service.dto.*;
import com.kjms.service.utils.PageableUtils;
import com.kjms.web.rest.errors.WrongSortKeyException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Set;

/**
 * REST controller for managing common api.
 */
@Tag(name = "Common")
@RestController
@RequestMapping("/api/v1")
public class CommonResource {
    private final Logger log = LoggerFactory.getLogger(CommonResource.class);

    private final CommonService commonService;
    private final ResourceLoader resourceLoader;

    public CommonResource(CommonService commonService, ResourceLoader resourceLoader) {
        this.commonService = commonService;
        this.resourceLoader = resourceLoader;
    }

    private static final List<String> ALLOWED_ORDERED_USER_PROPERTIES = List.of("id", "fullName", "email");

    private boolean onlyContainsAllowedUserProperties(Pageable pageable) {
        return pageable.getSort().stream().map(Sort.Order::getProperty).allMatch(ALLOWED_ORDERED_USER_PROPERTIES::contains);
    }

    private static final List<String> ALLOWED_ORDERED_PROPERTIES = List.of("langKey", "name");

    private boolean onlyContainsAllowedProperties(Pageable pageable) {
        return pageable.getSort().stream().map(Sort.Order::getProperty).allMatch(ALLOWED_ORDERED_PROPERTIES::contains);
    }

    /**
     * get a list of countries
     *
     * @param searchText searchText
     * @param pageable   pageable for a list
     * @return list of countries
     */
    @GetMapping(path = "/country/list")
    public ResponseEntity<Object> getCountryList(
        @RequestParam(name = Constants.SEARCH_TEXT, required = false) String searchText,
        @ParameterObject Pageable pageable
    ) {
        final Page<Country> countryList = commonService.countryList(pageable, searchText);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), countryList);

        return new ResponseEntity<>(countryList.getContent(), headers, HttpStatus.OK);
    }

    /**
     * {@code GET api/v1/language/list} : get all languages.
     *
     * @param searchText text to search name.
     * @param pageable   for paginating the list.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with all languages.
     * @throws WrongSortKeyException {@code 400 (Bad Request)} if the sort key is wrong.
     */
    @GetMapping(path = "/language/list")
    public ResponseEntity<List<Language>> getLanguageList(
        @RequestParam(name = Constants.SEARCH_TEXT, required = false) String searchText,
        @ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get all Languages");

        if (!onlyContainsAllowedProperties(pageable)) {
            throw new WrongSortKeyException();
        }

        final Page<Language> languages = commonService.getLanguages(SecurityUtils.getCurrentUserId(), searchText, pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), languages);

        return new ResponseEntity<>(languages.getContent(), headers, HttpStatus.OK);
    }

    /**
     * {@code GET api/v1/article-submission-file-format/list} : get all Article submission file formats.
     *
     * @param searchText Text to search.
     * @param pageable   for pagination.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with all journals.
     * @throws WrongSortKeyException {@code 400 (Bad Request)} if the sort key is wrong.
     */
    @GetMapping("/article-submission-file-format/list")
    public ResponseEntity<List<SubmissionFileFormat>> getGroupList(@RequestParam(name = Constants.SEARCH_TEXT, required = false) String searchText,
                                                                   @ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get all Article Submission File Formats.");

        if (!onlyContainsAllowedUserProperties(pageable)) {
            throw new WrongSortKeyException();
        }

        final Page<SubmissionFileFormat> allFormats = commonService.getAllFileFormats(
            searchText,
            pageable
        );

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), allFormats);

        return new ResponseEntity<>(allFormats.getContent(), headers, HttpStatus.OK);
    }

    @PostMapping(path = "/category/create")
    public ResponseEntity<Object> createCategory(@RequestParam(name = "name") String name) {

        log.debug("Rest Request to create category");

        return ResponseEntity.ok().body(commonService.createCategory(name));
    }

    @PostMapping(path = "/submission-file-type/create")
    public ResponseEntity<Object> createSubmissionFileType(@RequestParam(name = "name") String name) {

        log.debug("Rest Request to create SubmissionFileType");

        return ResponseEntity.ok().body(commonService.createFileType(name));
    }


    @GetMapping(path = "/category/list")
    public ResponseEntity<Object> getCategoryList(@RequestParam(name = Constants.SEARCH_TEXT, required = false) String searchText) {
        log.debug("Rest Request to all Categories");

        return ResponseEntity.ok().body(commonService.getCategoryList(searchText));
    }

    /**
     * {@code GET api/v1/journal-permissions} : get all Journal Permissions.
     */
    @GetMapping("/journal-permissions")
    public ResponseEntity<Object> getJournalPermissions() throws IOException {
        log.debug("Rest Request to all Journal Permissions");

        Resource resource = resourceLoader.getResource("classpath:config/permissions/journal-permissions.json");

        byte[] fileBytes = Files.readAllBytes(resource.getFile().toPath());

        return ResponseEntity
            .ok()
            .body(new String(fileBytes, StandardCharsets.UTF_8));

    }

    /**
     * {@code GET api/v1/org-permissions} : get all Organization Permissions.
     */
    @GetMapping("/org-permissions")
    public ResponseEntity<Object> getOrgPermissions() throws IOException {
        log.debug("Rest Request to all Organization Permissions");

        Resource resource = resourceLoader.getResource("classpath:config/permissions/administration-permissions.json");

        byte[] fileBytes = Files.readAllBytes(resource.getFile().toPath());

        return ResponseEntity
            .ok()
            .body(new String(fileBytes, StandardCharsets.UTF_8));

    }

    /**
     * {@code GET api/v1/journal/submission-file-type/list} : get all journal Submission File Types.
     *
     * @param searchText searchText
     * @param pageable   for pagination
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with all journal Submission File Types.
     * @throws WrongSortKeyException {@code 400 (Bad Request)} if the sort key is wrong.
     */
    @GetMapping(path = "/file-types")
    public ResponseEntity<List<FileType>> getFileTypeList(@RequestParam(name = Constants.SEARCH_TEXT, required = false)
                                                              String searchText, @ParameterObject Pageable pageable) {
        log.debug("REST request to get all File Types");

        if (!PageableUtils.onlyContainsAllowedProperties(pageable, Set.of("id", "name"))) {
            throw new WrongSortKeyException();
        }

        final Page<FileType> fileTypes = commonService.getFileTypeList(searchText, pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), fileTypes);

        return new ResponseEntity<>(fileTypes.getContent(), headers, HttpStatus.OK);
    }
}
