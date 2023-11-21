package com.kjms.web.rest.workflow;

import com.kjms.config.Constants;
import com.kjms.domain.CopyEditingDiscussionStatus;
import com.kjms.domain.WorkflowStage;
import com.kjms.security.BadRequestAlertException;
import com.kjms.service.CopyEditingService;
import com.kjms.service.dto.*;
import com.kjms.service.dto.requests.ProductionContributorAddRequest;
import com.kjms.service.dto.requests.WorkflowTransferRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@Tag(name = "Workflow - CopyEditing")
@RestController
@RequestMapping("/api/v1/copy-editing")
public class CopyEditingResource {

    private final CopyEditingService copyEditingService;

    private final Logger log = LoggerFactory.getLogger(CopyEditingResource.class);

    public CopyEditingResource(CopyEditingService copyEditingService) {
        this.copyEditingService = copyEditingService;
    }


    @PostMapping("/move-to-copy-editing")
    public ResponseEntity<Void> moveToCopyEditing(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId,
        @RequestBody WorkflowTransferRequest workflowTransferRequest
    ) {
        log.debug("REST request to move to copy editing: {} in journal {}", submissionId, journalId);

        if (workflowTransferRequest.getFromWorkflowStage().equals(WorkflowStage.SUBMISSION)) {

            copyEditingService.moveFromSubmissionToCopyEditing(journalId, submissionId, workflowTransferRequest.getFileIds());

        } else if (workflowTransferRequest.getFromWorkflowStage().equals(WorkflowStage.REVIEW)) {

            if(workflowTransferRequest.getReviewRoundId() ==null){

                throw new RuntimeException("reviewRoundId not present");
            }

            copyEditingService.moveFromReviewToCopyEditing(journalId, submissionId, workflowTransferRequest.getFileIds(),workflowTransferRequest.getReviewRoundId());

        }


        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/draft-files/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<CopyEditingDraftFile>> draftFilesUpload(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId,
        MultipartHttpServletRequest httpServletRequest
    ) {
        log.debug("REST request to upload a copy-editing draft file: {} in journal {}", submissionId, journalId);

        return new ResponseEntity<>(copyEditingService.uploadCopyEditingDraftFiles(journalId, submissionId, httpServletRequest.getFileMap()), HttpStatus.OK);
    }

    @GetMapping(value = "/copy-editing-draft/files/download-as-zip")
    public ResponseEntity<Object> downloadCopyEditingDraftFilesAsZip(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId
    ) throws IOException {

        log.debug("REST Request for download copy editing draft files as zip: {}", submissionId);

        Path path = Path.of(copyEditingService.downloadCopyEditingDraftFilesAsZip(journalId,submissionId).getAbsolutePath());

        byte[] file = Files.readAllBytes(path);

        HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLDecoder.decode(path.getFileName().toString(), StandardCharsets.UTF_8)); // Set the file name

        return ResponseEntity.ok()
            .headers(headers)
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(file);
    }

    @GetMapping(value = "/draft-files")
    public ResponseEntity<Object> getDraftFiles(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId
    ) {

        log.debug("REST request to get Draft Files: {} in journal {}", submissionId, journalId);

        return new ResponseEntity<>(copyEditingService.getDraftFiles(journalId, submissionId), HttpStatus.OK);
    }

    @PostMapping(value = "/copy-edited-files/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<CopyEditedFile>> copyEditedFilesUpload(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId,
        MultipartHttpServletRequest httpServletRequest
    ) {
        log.debug("REST request to upload a copy-edited files : {} in journal {}", submissionId, journalId);

        return new ResponseEntity<>(copyEditingService.uploadCopyEditedFiles(journalId, submissionId, httpServletRequest.getFileMap()), HttpStatus.OK);
    }
    @GetMapping(value = "/copy-edited/files/download-as-zip")
    public ResponseEntity<Object> downloadCopyEditedFilesAsZip(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId
    ) throws IOException {

        log.debug("REST Request for download review round files as zip: {}", submissionId);

        Path path = Path.of(copyEditingService.downloadCopyEditedFilesAsZip(journalId,submissionId).getAbsolutePath());

        byte[] file = Files.readAllBytes(path);

        HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLDecoder.decode(path.getFileName().toString(), StandardCharsets.UTF_8)); // Set the file name

        return ResponseEntity.ok()
            .headers(headers)
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(file);
    }

    @PostMapping(value = "/discussion", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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

            return new ResponseEntity<>(copyEditingService.createDiscussion(journalId, submissionId, topic, description, httpServletRequest.getFileMap(), discussionMembers), HttpStatus.OK);

        } else {

            throw new BadRequestAlertException("No Members", null, "member.needed");
        }
    }

    @PutMapping(value = "/discussion/status")
    public ResponseEntity<Object> createDiscussion(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId,
        @RequestParam(name = "discussionId") Long discussionId,
        @RequestParam(name = "status") CopyEditingDiscussionStatus copyEditingDiscussionStatus) {

        log.debug("REST request to create discussion: {} in journal {}", submissionId, journalId);

        return new ResponseEntity<>(copyEditingService.updateDiscussionStatus(journalId, submissionId, discussionId, copyEditingDiscussionStatus), HttpStatus.OK);
    }

    @GetMapping("/discussions")
    public ResponseEntity<Object> getAllDiscussions(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId) {

        log.debug("REST request to create discussion: {} in journal {}", submissionId, journalId);

        return new ResponseEntity<>(copyEditingService.getDiscussions(journalId, submissionId), HttpStatus.OK);
    }

    @PostMapping(value = "/discussion/chat", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CopyEditingDiscussionMessage> createDiscusssionChatMessage(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId,
        @RequestParam(name = "discussionId") Long discussionId,
        @RequestPart(name = "message") String message,
        MultipartHttpServletRequest httpServletRequest) {

        log.debug("REST request to create discussion: {} in journal {}", submissionId, journalId);

        return new ResponseEntity<>(copyEditingService.createDiscussionChatMessage(journalId, submissionId, discussionId, httpServletRequest.getFileMap(), message), HttpStatus.OK);
    }

    @GetMapping("/discussion/chat")
    public ResponseEntity<List<CopyEditingDiscussionMessage>> createDiscusssionChatMessage(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId,
        @RequestParam(name = "discussionId") Long discussionId) {

        log.debug("REST request to create discussion: {} in journal {}", submissionId, journalId);

        return new ResponseEntity<>(copyEditingService.getDiscussionChatMessages(journalId, submissionId, discussionId), HttpStatus.OK);
    }


    @PostMapping("/add-contributors")
    public ResponseEntity<List<CopyEditingContributor>> requestForAddContributor(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId,
        @RequestBody ProductionContributorAddRequest productionContributorAddRequest) {

        log.debug("REST request to add contributor: {} in journal {}", submissionId, journalId);

        return new ResponseEntity<>(copyEditingService.addContributor(journalId, submissionId, productionContributorAddRequest.getUserIds(), productionContributorAddRequest.getDesc()), HttpStatus.OK);
    }

    @DeleteMapping("/remove-contributor")
    public ResponseEntity<Void> requestForRemoveContributor(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId,
        @RequestParam(name = "contributorId") String contributorId) {

        log.debug("REST request to remove contributor: {} in journal {}", submissionId, journalId);

        copyEditingService.removeContributor(journalId, submissionId, contributorId);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/contributors")
    public ResponseEntity<Object> getContributors(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId) {

        log.debug("REST request to add contributor: {} in journal {}", submissionId, journalId);

        return new ResponseEntity<>(copyEditingService.getContributors(journalId, submissionId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> getCopyEditingDetails(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId) {

        return new ResponseEntity<>(copyEditingService.getCopyEditingDetail(journalId, submissionId), HttpStatus.OK);
    }

    @GetMapping(value = "/copy-edited-files")
    public ResponseEntity<Object> getCopyEditedFiles(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId
    ) {

        log.debug("REST request to get Copy Edited File: {} in journal {}", submissionId, journalId);

        return new ResponseEntity<>(copyEditingService.getCopyEditedFiles(journalId, submissionId), HttpStatus.OK);
    }
}
