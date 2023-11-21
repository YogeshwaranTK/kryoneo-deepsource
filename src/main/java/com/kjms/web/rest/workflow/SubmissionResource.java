package com.kjms.web.rest.workflow;

import com.kjms.config.Constants;
import com.kjms.domain.SubmissionDiscussionStatus;
import com.kjms.domain.WorkflowStage;
import com.kjms.security.BadRequestAlertException;
import com.kjms.service.SubmissionService;
import com.kjms.service.dto.*;
import com.kjms.service.dto.requests.ProductionContributorAddRequest;
import com.kjms.service.dto.requests.SubmissionAuthorRequest;
import com.kjms.service.dto.requests.SubmissionCreateRequest;
import com.kjms.service.dto.requests.SubmissionUpdateRequest;
import com.kjms.service.utils.PageableUtils;
import com.kjms.web.rest.journal.JournalResource;
import com.kjms.web.rest.errors.WrongSortKeyException;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;

/**
 * REST controller for managing the Submission Article.
 */
@Tag(name = "Workflow - Submission")
@RestController
@RequestMapping("/api/v1")
public class SubmissionResource {

    private final SubmissionService submissionService;
    private final ResourceLoader resourceLoader;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final Logger log = LoggerFactory.getLogger(JournalResource.class);

    public SubmissionResource(SubmissionService submissionService, ResourceLoader resourceLoader) {
        this.submissionService = submissionService;
        this.resourceLoader = resourceLoader;
    }

    /**
     * {@code POST /api/v1/submission-article/basic-detail/create } : create Article Basic detail.
     *
     * @param submissionCreateRequest the submission article to create.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)}.
     */
    @PostMapping("/submission/basic-detail/create")
    public ResponseEntity<Map<String, Long>> createSubmissionArticle(@RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
                                                                     @Valid @RequestBody SubmissionCreateRequest submissionCreateRequest
    ) {
        log.debug("REST request to save Submission Article: {}", submissionCreateRequest);

        Long submissionArticleId = submissionService.createSubmission(
            journalId, submissionCreateRequest
        );

        Map<String, Long> map = new HashMap<>();

        map.put("submissionId", submissionArticleId);

        return ResponseEntity
            .ok()
            .body(map);
    }

    /**
     * {@code PUT /api/v1/submission-article/basic-detail/update } : update Article Basic detail.
     *
     * @param submissionUpdateRequest the submission article to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)}.
     */
    @PutMapping("/submission/basic-detail/update")
    public ResponseEntity<Submission> updateSubmissionArticleBasicDetails(@RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
                                                                          @RequestParam("submissionId") Long submissionId,
                                                                          @Valid @RequestBody SubmissionUpdateRequest submissionUpdateRequest
    ) {
        log.debug("REST request to update Submission Article: {}", submissionUpdateRequest);

        Submission submission = submissionService.updateSubmission(journalId, submissionId, submissionUpdateRequest
        );

        return ResponseEntity.ok().headers(HeaderUtil.createAlert(applicationName, "submissionArticle.updated", submission.getTitle())).build();
    }

    /**
     * {@code GET api/v1/submission-article/list} : get all submission articles.
     *
     * @param searchText searchText
     * @param pageable   for pagination
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with all submission articles.
     * @throws WrongSortKeyException {@code 400 (Bad Request)} if the sort key is wrong.
     */
    @GetMapping(path = "/submissions")
    public ResponseEntity<List<Submission>> getSubmissions(@RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
                                                           @RequestParam(value = Constants.SEARCH_TEXT, required = false) String searchText,
                                                           @RequestParam(name = "workflowStage", required = false) WorkflowStage workflowStage,
                                                           @ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get all Submission Articles");

        final Set<String> ALLOWED_ORDERED_PROPERTIES = Set.of("id", "title", "status", "lastModifiedAt", "createdAt");

        if (!PageableUtils.onlyContainsAllowedProperties(pageable, ALLOWED_ORDERED_PROPERTIES)) {
            throw new WrongSortKeyException();
        }

        final Page<Submission> submissions = submissionService.getSubmissions(
            journalId,
            searchText,
            pageable,
            workflowStage
        );

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
            ServletUriComponentsBuilder.fromCurrentRequest(),
            submissions
        );

        return new ResponseEntity<>(submissions.getContent(), headers, HttpStatus.OK);
    }

    /**
     * {@code GET api/v1/submission-article} : get the submission article.
     *
     * @param submissionId the submission article to get.
     * @return the {@link ResponseEntity} with {@link Submission} in body with status {@code 200 (OK)} .
     */
    @GetMapping("/submission")
    public ResponseEntity<Submission> getSubmission(@RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
                                                    @RequestParam(name = "submissionId") Long submissionId
    ) {
        log.debug("REST request to get Submission Article: {}", submissionId);

        return ResponseEntity
            .ok()
            .body(submissionService.getSubmission(journalId, submissionId));
    }

    /**
     * {@code PUT api/v1/submission-article/author/update} : Update submission author.
     * <p>
     * //     * @param submissionAuthorUpdateRequest the submission author to update.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)}.
     */
    @PutMapping("/submission/author/update")
    public ResponseEntity<Object> updateSubmissionAuthor(@RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
                                                         @RequestParam("submissionId") Long submissionId,
                                                         @Valid @RequestBody List<SubmissionAuthorRequest> authorRequests
    ) {
        log.debug("REST request to update Submission Article category:  {}", authorRequests);

        return ResponseEntity.ok().body(submissionService.updateSubmissionAuthor(
            journalId, submissionId,
            authorRequests
        ));
    }

    @PostMapping(value = "/submission/files/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<SubmissionFile>> reviewFileUpload(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId,
        @RequestPart(name = "oldFileIds", required = false) String oldFileIds,
        MultipartHttpServletRequest httpServletRequest
    ) {

        log.debug("REST request to upload a review round file: {} in journal {}", submissionId, journalId);

        return new ResponseEntity<>(submissionService.uploadSubmissionFile(journalId, submissionId, oldFileIds, httpServletRequest.getFileMap()), HttpStatus.OK);
    }

    @DeleteMapping(value = "/submission/file")
    public ResponseEntity<Void> deleteSubmissionFile(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId,
        @RequestParam(name = "submissionFileId") Long submissionFileId
    ) {

        submissionService.deleteSubmissionFile(journalId, submissionId, submissionFileId);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PutMapping(value = "/submission/revision/file/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<SubmissionFile> revisionFileUpload(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId,
        @RequestPart(name = "originalFileId") String originalFileId,
        @RequestPart(name = "file") MultipartFile file
    ) {

        log.debug("REST request to upload a review round file: {} in journal {}", submissionId, journalId);

        return new ResponseEntity<>(submissionService.uploadSubmissionRevisionFile(journalId, submissionId, Long.valueOf(originalFileId), file), HttpStatus.OK);
    }

    @GetMapping(value = "/submission/files/download-as-zip")
    public ResponseEntity<Object> downloadSubmissionFilesAsZip(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId
    ) throws IOException {

        log.debug("REST Request for download submission files as zip: {}", submissionId);

        Path path = Path.of(submissionService.downloadSubmissionFilesAsZip(journalId, submissionId).getAbsolutePath());

        byte[] file = Files.readAllBytes(path);

        HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLDecoder.decode(path.getFileName().toString(), StandardCharsets.UTF_8)); // Set the file name

        return ResponseEntity.ok()
            .headers(headers)
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(file);
    }


    @PutMapping("submission/final")
    public ResponseEntity<Object> finishFinalSubmission(@RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
                                                        @RequestParam Long submissionId
    ) {
        log.debug("REST request to finish final Submission:  {}", submissionId);


        return new ResponseEntity<>(submissionService.finishSubmission(
            journalId,
            submissionId
        ), HttpStatus.OK);
    }

    /**
     * {@code GET api/v1/submission-article/journal-category/list} : get all journal Categories.
     *
     * @param searchText searchText
     * @param pageable   for pagination
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with all journal Categories.
     * @throws WrongSortKeyException {@code 400 (Bad Request)} if the sort key is wrong.
     */
    @GetMapping(path = "/submission/journal-categories")
    public ResponseEntity<List<SubmissionCategory>> getJournalCategoryList(@RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
                                                                           @RequestParam(value = Constants.SEARCH_TEXT, required = false) String searchText,
                                                                           @ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get all Journal Category list");

        if (!PageableUtils.onlyContainsAllowedProperties(pageable, Set.of("id"))) {
            throw new WrongSortKeyException();
        }

        final Page<SubmissionCategory> journalCategories = submissionService.getJournalCategories(
            journalId,
            searchText,
            pageable
        );

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
            ServletUriComponentsBuilder.fromCurrentRequest(),
            journalCategories
        );

        return new ResponseEntity<>(journalCategories.getContent(), headers, HttpStatus.OK);
    }

    @PostMapping(value = "/submission/read-author-excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<SubmissionArticleAuthor>> readAuthorExcel(@RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
                                                                         @RequestPart("file") MultipartFile multipartFile) {

        return ResponseEntity
            .ok()
            .body(submissionService.readSubArticleAuthorExcel(journalId, multipartFile));
    }

    /**
     * {@code GET api/v1/submission-article/author-sample-excel} : Get Submission Article Contributor Author Sample file.
     */
    @GetMapping("/submission/author-sample-excel")
    public ResponseEntity<byte[]> downloadSubArticleSampleExcel(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId
    ) throws IOException {
        log.debug("Rest Request to Get Submission Article Author Sample Excel.");

        Resource resource = resourceLoader.getResource("classpath:sample/contributor-add-sample-file.xlsx");

        byte[] fileBytes = Files.readAllBytes(resource.getFile().toPath());

        HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLDecoder.decode("contributor-add-sample-file.xlsx", StandardCharsets.UTF_8)); // Set the file name

        return ResponseEntity
            .ok()
            .headers(headers)
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(fileBytes);
    }

    @PostMapping(value = "/submission/discussion", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> createDiscussion(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId,
        @RequestPart(name = "topic") String topic,
        @RequestPart(name = "members") String members,
        @RequestPart(name = "description") String description,
        MultipartHttpServletRequest httpServletRequest) {

        log.debug("REST request to create discussion: {} in journal {}", submissionId, journalId);

        if (members != null) {

            List<String> discussionMembers = Arrays.asList(members.split("\\s*,\\s*"));

            if (discussionMembers.isEmpty()) {

                throw new BadRequestAlertException("No Members", null, "member.needed");
            }

            return new ResponseEntity<>(submissionService.createDiscussion(journalId, submissionId, topic, description, httpServletRequest.getFileMap(), discussionMembers), HttpStatus.OK);

        } else {

            throw new BadRequestAlertException("No Members", null, "member.needed");
        }
    }


    @PutMapping(value = "/submission/discussion/status")
    public ResponseEntity<Object> createDiscussion(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId,
        @RequestParam(name = "discussionId") Long discussionId,
        @RequestParam(name = "status") SubmissionDiscussionStatus productionDiscussionStatus) {

        log.debug("REST request to create discussion: {} in journal {}", submissionId, journalId);

        return new ResponseEntity<>(submissionService.updateDiscussionStatus(journalId, submissionId, discussionId, productionDiscussionStatus), HttpStatus.OK);
    }

    @GetMapping("/submission/discussions")
    public ResponseEntity<Object> getAllDiscussions(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId) {

        log.debug("REST request to create discussion: {} in journal {}", submissionId, journalId);

        return new ResponseEntity<>(submissionService.getDiscussions(journalId, submissionId), HttpStatus.OK);
    }

    @PostMapping(value = "/submission/discussion/chat", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> createDiscussionChatMessage(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId,
        @RequestParam(name = "discussionId") Long discussionId,
        @RequestPart(name = "message") String message,
        MultipartHttpServletRequest httpServletRequest) {

        log.debug("REST request to create discussion: {} in journal {}", submissionId, journalId);

        return new ResponseEntity<>(submissionService.createDiscussionChatMessage(journalId, submissionId, discussionId, httpServletRequest.getFileMap(), message), HttpStatus.OK);
    }

    @GetMapping("/submission/discussion/chat")
    public ResponseEntity<Object> createDiscussionChatMessage(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId,
        @RequestParam(name = "discussionId") Long discussionId) {

        log.debug("REST request to create discussion: {} in journal {}", submissionId, journalId);
        return new ResponseEntity<>(submissionService.getDiscussionChatMessages(journalId, submissionId, discussionId), HttpStatus.OK);
    }

    @PostMapping("/submission/add-contributors")
    public ResponseEntity<List<SubmissionContributor>> requestForAddContributor(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId,
        @RequestBody ProductionContributorAddRequest productionContributorAddRequest) {

        log.debug("REST request to add contributor: {} in journal {}", submissionId, journalId);


        return new ResponseEntity<>(submissionService.addContributor(journalId, submissionId, productionContributorAddRequest.getUserIds(), productionContributorAddRequest.getDesc()), HttpStatus.OK);
    }

    @GetMapping("/submission/contributors")
    public ResponseEntity<Object> getContributors(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId) {

        log.debug("REST request to add contributor: {} in journal {}", submissionId, journalId);

        return new ResponseEntity<>(submissionService.getContributors(journalId, submissionId), HttpStatus.OK);
    }

    @DeleteMapping("/submission/remove-contributor")
    public ResponseEntity<Void> requestForRemoveContributor(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId,
        @RequestParam(name = "contributorId") String contributorId) {

        log.debug("REST request to remove contributor: {} in journal {}", submissionId, journalId);

        submissionService.removeContributor(submissionId, contributorId);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
