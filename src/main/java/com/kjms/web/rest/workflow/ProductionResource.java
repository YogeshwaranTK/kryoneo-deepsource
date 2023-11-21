package com.kjms.web.rest.workflow;

import com.kjms.config.Constants;
import com.kjms.domain.ProductionDiscussionStatus;
import com.kjms.domain.WorkflowStage;
import com.kjms.security.BadRequestAlertException;
import com.kjms.service.ProductionService;
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

@Tag(name = "Workflow - Production")
@RestController
@RequestMapping("/api/v1/production")
public class ProductionResource {

    private final ProductionService productionService;

    private final Logger log = LoggerFactory.getLogger(ProductionResource.class);

    public ProductionResource(ProductionService productionService) {
        this.productionService = productionService;
    }

    @PostMapping("/move-to-production")
    public ResponseEntity<Void> moveToProduction(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId,
        @RequestBody WorkflowTransferRequest workflowTransferRequest
    ) {
        log.debug("REST request to move to production: {} in journal {}", submissionId, journalId);

        if (workflowTransferRequest.getFromWorkflowStage().equals(WorkflowStage.SUBMISSION)) {

            productionService.moveFromSubmissionToProduction(journalId, submissionId, workflowTransferRequest.getFileIds());

        } else if (workflowTransferRequest.getFromWorkflowStage().equals(WorkflowStage.REVIEW)) {

            if (workflowTransferRequest.getReviewRoundId() == null) {

                throw new RuntimeException("reviewRoundId not present");
            }

            productionService.moveFromReviewToProduction(journalId, submissionId, workflowTransferRequest.getFileIds(), workflowTransferRequest.getReviewRoundId());

        } else if (workflowTransferRequest.getFromWorkflowStage().equals(WorkflowStage.COPY_EDITING)) {

            productionService.moveFromCopyEditingToProduction(journalId, submissionId, workflowTransferRequest.getFileIds());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/add-contributors")
    public ResponseEntity<List<ProductionContributor>> requestForAddContributor(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId,
        @RequestBody ProductionContributorAddRequest productionContributorAddRequest) {

        log.debug("REST request to add contributor: {} in journal {}", submissionId, journalId);


        return new ResponseEntity<>(productionService.addContributor(journalId, submissionId, productionContributorAddRequest.getUserIds(), productionContributorAddRequest.getDesc()), HttpStatus.OK);
    }

    @DeleteMapping("/remove-contributor")
    public ResponseEntity<Void> requestForRemoveContributor(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId,
        @RequestParam(name = "contributorId") String contributorId) {

        log.debug("REST request to remove contributor: {} in journal {}", submissionId, journalId);

        productionService.removeContributor(journalId, submissionId, contributorId);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping("/contributors")
    public ResponseEntity<Object> getContributors(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId) {

        log.debug("REST request to add contributor: {} in journal {}", submissionId, journalId);

        return new ResponseEntity<>(productionService.getContributors(journalId, submissionId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Production> getProductionDetails(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId) {

        log.debug("REST request to add contributor: {} in journal {}", submissionId, journalId);

        return new ResponseEntity<>(productionService.getProductionDetail(journalId, submissionId), HttpStatus.OK);
    }

    @PostMapping(value = "/production-ready-file/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<ProductionReadyFile>> uploadProductionReadyFile(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId,
        MultipartHttpServletRequest httpServletRequest
    ) {
        log.debug("REST request to upload Production Ready File: {} in journal {}", submissionId, journalId);

        return new ResponseEntity<>(productionService.uploadProductionReadyFiles(journalId, submissionId, httpServletRequest.getFileMap()), HttpStatus.OK);
    }

    @GetMapping(value = "/production-ready-files")
    public ResponseEntity<List<ProductionReadyFile>> getProductionReadyFiles(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId
    ) {

        log.debug("REST request to upload Production Ready File: {} in journal {}", submissionId, journalId);

        return new ResponseEntity<>(productionService.getProductionReadyFiles(journalId, submissionId), HttpStatus.OK);
    }

    @DeleteMapping(value = "/production-ready-files")
    public ResponseEntity<Void> getProductionReadyFiles(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId,
        @RequestParam(name = "fileId") Long fileId
    ) {

        log.debug("REST request to upload Production Ready File: {} in journal {}", submissionId, journalId);

        productionService.deleteProductionReadyFile(journalId, submissionId, fileId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/production-completed-file/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<ProductionCompletedFile>> uploadProductionCompletedFile(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId,
        MultipartHttpServletRequest httpServletRequest
    ) {

        log.debug("REST request to upload Production Ready File: {} in journal {}", submissionId, journalId);

        return new ResponseEntity<>(productionService.uploadProductionCompletedFile(journalId, submissionId, httpServletRequest.getFileMap()), HttpStatus.OK);
    }

    @GetMapping(value = "/production-ready/files/download-as-zip")
    public ResponseEntity<Object> downloadProductionReadyFilesAsZip(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId
    ) throws IOException {

        log.debug("REST Request for download copy editing draft files as zip: {}", submissionId);

        Path path = Path.of(productionService.downloadCProductionReadyFilesAsZip(journalId,submissionId).getAbsolutePath());

        byte[] file = Files.readAllBytes(path);

        HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLDecoder.decode(path.getFileName().toString(), StandardCharsets.UTF_8)); // Set the file name

        return ResponseEntity.ok()
            .headers(headers)
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(file);
    }

    @GetMapping(value = "/production-completed-files")
    public ResponseEntity<List<ProductionCompletedFile>> getProductionCompletedFiles(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId
    ) {

        log.debug("REST request to upload Production Ready File: {} in journal {}", submissionId, journalId);

        return new ResponseEntity<>(productionService.getProductionCompletedFiles(journalId, submissionId), HttpStatus.OK);
    }


    @GetMapping(value = "/production-completed/files/download-as-zip")
    public ResponseEntity<Object> downloadProductionCompletedFilesAsZip(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId
    ) throws IOException {

        log.debug("REST Request for download copy editing draft files as zip: {}", submissionId);

        Path path = Path.of(productionService.downloadCProductionCompletedFilesAsZip(journalId,submissionId).getAbsolutePath());

        byte[] file = Files.readAllBytes(path);

        HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLDecoder.decode(path.getFileName().toString(), StandardCharsets.UTF_8)); // Set the file name

        return ResponseEntity.ok()
            .headers(headers)
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(file);
    }


    @PostMapping(value = "/discussion", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductionDiscussion> createDiscussion(
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

            return new ResponseEntity<>(productionService.createDiscussion(journalId, submissionId, topic, description, httpServletRequest.getFileMap(), discussionMembers), HttpStatus.OK);

        } else {

            throw new BadRequestAlertException("No Members", null, "member.needed");
        }
    }

    @PutMapping(value = "/discussion/status")
    public ResponseEntity<ProductionDiscussion> createDiscussion(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId,
        @RequestParam(name = "discussionId") Long discussionId,
        @RequestParam(name = "status") ProductionDiscussionStatus productionDiscussionStatus) {

        log.debug("REST request to create discussion: {} in journal {}", submissionId, journalId);

        return new ResponseEntity<>(productionService.updateDiscussionStatus(journalId, submissionId, discussionId, productionDiscussionStatus), HttpStatus.OK);
    }

    @GetMapping("/discussions")
    public ResponseEntity<List<ProductionDiscussion>> getAllDiscussions(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId) {

        log.debug("REST request to create discussion: {} in journal {}", submissionId, journalId);

        return new ResponseEntity<>(productionService.getDiscussions(journalId, submissionId), HttpStatus.OK);
    }

    @PostMapping(value = "/discussion/chat", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductionDiscussionMessage> createDiscussionChatMessage(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId,
        @RequestParam(name = "discussionId") Long discussionId,
        @RequestPart(name = "message") String message,
        MultipartHttpServletRequest httpServletRequest) {

        log.debug("REST request to create discussion: {} in journal {}", submissionId, journalId);

        return new ResponseEntity<>(productionService.createDiscussionChatMessage(journalId, submissionId, discussionId, httpServletRequest.getFileMap(), message), HttpStatus.OK);
    }

    @GetMapping("/discussion/chat")
    public ResponseEntity<List<ProductionDiscussionMessage>> createDiscussionChatMessage(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId,
        @RequestParam(name = "discussionId") Long discussionId) {

        log.debug("REST request to create discussion: {} in journal {}", submissionId, journalId);
        return new ResponseEntity<>(productionService.getDiscussionChatMessages(journalId, submissionId, discussionId), HttpStatus.OK);
    }
}
