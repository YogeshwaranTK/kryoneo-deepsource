package com.kjms.web.rest.workflow;

import com.kjms.config.Constants;
import com.kjms.domain.*;
import com.kjms.security.BadRequestAlertException;
import com.kjms.service.ReviewService;
import com.kjms.service.dto.*;
import com.kjms.service.dto.requests.ProductionContributorAddRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@Tag(name = "Workflow - Review")
@RestController
@RequestMapping("/api/v1/review")
public class ReviewResource {

    private final ReviewService reviewService;
    private final Logger log = LoggerFactory.getLogger(ReviewResource.class);

    public ReviewResource(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping(value = "/move-to-review")
    public ResponseEntity<Void> moveToReview(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId,
        @RequestBody List<Long> fileIds
    ) {
        log.debug("REST request to move to peer review: {} in journal {}", submissionId, journalId);
        reviewService.moveToReview(journalId, submissionId, fileIds);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/create-review-round")
    public ResponseEntity<ReviewRound> createReviewRound(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId
    ) {
        log.debug("REST request to create a review round: {} in journal {}", submissionId, journalId);

        return new ResponseEntity<>(reviewService.createReviewRound(journalId, submissionId), HttpStatus.OK);
    }

    @GetMapping(value = "/rounds")
    public ResponseEntity<List<ReviewRound>> getReviews(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId
    ) {
        log.debug("REST request to create a review round: {} in journal {}", submissionId, journalId);

        return new ResponseEntity<>(reviewService.getReviews(journalId, submissionId), HttpStatus.OK);
    }

    @GetMapping(value = "/round")
    public ResponseEntity<ReviewRound> getReviews(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId,
        @RequestParam(name = "reviewRoundId") Long reviewRoundId
    ) {
        log.debug("REST request to get a review round: {} in journal {}", submissionId, journalId);

        return new ResponseEntity<>(reviewService.getReviewRound(journalId, submissionId, reviewRoundId), HttpStatus.OK);
    }

    @PostMapping(value = "/review-round-files/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<ReviewRoundFile>> reviewFileUpload(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "reviewRoundId") Long reviewRoundId,
        @RequestParam(name = "submissionId") Long submissionId,
        MultipartHttpServletRequest httpServletRequest
    ) {
        log.debug("REST request to upload a review round file: {} in journal {}", submissionId, journalId);

        return new ResponseEntity<>(reviewService.uploadReviewRoundFile(journalId, submissionId, reviewRoundId, httpServletRequest.getFileMap()), HttpStatus.OK);
    }

    @PostMapping(value = "/review-round/discussion", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> createReviewRoundDiscussion(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId,
        @RequestParam(name = "reviewRoundId") Long reviewRoundId,
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

            return new ResponseEntity<>(reviewService.createReviewRoundDiscussion(journalId, submissionId, reviewRoundId, topic, description, httpServletRequest.getFileMap(), discussionMembers), HttpStatus.OK);

        } else {

            throw new BadRequestAlertException("No Members", null, "member.needed");
        }
    }

    @PutMapping(value = "/review-round/discussion/status")
    public ResponseEntity<Object> updateReviewRoundDiscussionStatus(
        @RequestParam(name = "discussionId") Long discussionId,
        @RequestParam(name = "reviewRoundId") Long reviewRoundId,
        @RequestParam(name = "status") ReviewRoundDiscussionStatus reviewRoundDiscussionStatus) {

        log.debug("REST request to update discussion: {}", discussionId);

        return new ResponseEntity<>(reviewService.updateReviewRoundDiscussionStatus(discussionId, reviewRoundId, reviewRoundDiscussionStatus), HttpStatus.OK);
    }

    @GetMapping(value = "/review-round/discussion")
    public ResponseEntity<Object> getReviewRoundDiscussions(
        @RequestParam(name = "reviewRoundId") Long reviewRoundId) {

        log.debug("REST request to get discussions: {}", reviewRoundId);

        return new ResponseEntity<>(reviewService.getReviewRoundDiscussions(reviewRoundId), HttpStatus.OK);
    }

    @PostMapping(value = "/review-round/discussion/chat", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> createReviewRoundDiscussionChatMessage(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId,
        @RequestParam(name = "discussionId") Long discussionId,
        @RequestParam(name = "reviewRoundId") Long reviewRoundId,
        @RequestPart(name = "message") String message,
        MultipartHttpServletRequest httpServletRequest) {

        log.debug("REST request to create discussion: {} in journal {}", discussionId, journalId);

        return new ResponseEntity<>(reviewService.createReviewRoundDiscussionChatMessage(journalId, submissionId, discussionId, reviewRoundId, httpServletRequest.getFileMap(), message), HttpStatus.OK);
    }

    @GetMapping("/review-round/discussion/chat")
    public ResponseEntity<Object> getReviewRoundDiscussionChatMessage(
        @RequestParam(name = "reviewRoundId") Long reviewRoundId,
        @RequestParam(name = "discussionId") Long discussionId) {

        log.debug("REST request to create discussion: {}", discussionId);
        return new ResponseEntity<>(reviewService.getReviewRoundDiscussionChatMessages(reviewRoundId, discussionId), HttpStatus.OK);
    }

    //// TODO: 14-11-2023 ProductionContributorAddRequest to ReviewContributorAddRequest
    @PostMapping("/review-round/add-contributors")
    public ResponseEntity<List<ReviewRoundContributor>> requestForAddContributor(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId,
        @RequestParam(name = "reviewRoundId") Long reviewRoundId,
        @RequestBody ProductionContributorAddRequest productionContributorAddRequest) {

        log.debug("REST request to add contributor: {} in journal {}", submissionId, journalId);

        return new ResponseEntity<>(reviewService.addReviewRoundContributor(journalId, submissionId, reviewRoundId, productionContributorAddRequest.getUserIds(), productionContributorAddRequest.getDesc()), HttpStatus.OK);
    }

    @DeleteMapping("/review-round/remove-contributor")
    public ResponseEntity<Void> requestForRemoveContributor(
        @RequestParam(name = "contributorId") String contributorId,
        @RequestParam(name = "reviewRoundId") Long reviewRoundId) {

        reviewService.removeContributor(contributorId, reviewRoundId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/review-round/add-reviewer")
    public ResponseEntity<ReviewRoundReviewerReview> requestForAddReviewer(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestBody ReviewerRequest reviewerRequest) {

        log.debug("REST request to add reviewer: {}", reviewerRequest.getReviewRoundId());

        return new ResponseEntity<>(reviewService.addReviewer(reviewerRequest, journalId), HttpStatus.OK);
    }

    @GetMapping("/review-round/contributors")
    public ResponseEntity<Object> getContributors(
        @RequestParam(name = "reviewRoundId") Long reviewRoundId) {

        log.debug("REST request to add contributor: {}", reviewRoundId);

        return new ResponseEntity<>(reviewService.getReviewRoundContributors(reviewRoundId), HttpStatus.OK);
    }

    @GetMapping("/review-round/reviewers")
    public ResponseEntity<Object> getReviewers(
        @RequestParam(name = "reviewRoundId") Long reviewRoundId) {

        log.debug("REST request to add contributor: {}", reviewRoundId);

        return new ResponseEntity<>(reviewService.getReviewers(reviewRoundId), HttpStatus.OK);
    }

    @GetMapping("/reviewer/reply/files")
    public ResponseEntity<List<ReviewerReplyFile>> getReviewerReplyFiles(@RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
                                                                         @RequestParam(name = "reviewRoundReviewerId") Long reviewRoundReviewerId,
                                                                         @RequestParam(name = "reviewRoundId") Long reviewRoundId) {

        log.debug("REST request to add reviewer: {}", reviewRoundReviewerId);

        return new ResponseEntity<>(reviewService.getReviewerReplayFiles(reviewRoundReviewerId, reviewRoundId, journalId), HttpStatus.OK);
    }

    @PostMapping("/reviewer/reply/file-upload")
    public ResponseEntity<List<ReviewerReplyFile>> reviewerFileUpload(@RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
                                                                      @RequestParam(name = "reviewRoundReviewerId") Long reviewRoundReviewerId,
                                                                      @RequestParam(name = "reviewRoundId") Long reviewRoundId,
                                                                      MultipartHttpServletRequest httpServletRequest) {

        log.debug("REST request to add reviewer: {}", reviewRoundReviewerId);

        return new ResponseEntity<>(reviewService.reviewerReplayFileUpload(reviewRoundReviewerId, reviewRoundId, journalId, httpServletRequest.getFileMap()), HttpStatus.OK);
    }

    @GetMapping("/review-round/files")
    public ResponseEntity<List<ReviewRoundFile>> getReviewRoundFiles(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "reviewRoundId") Long reviewRoundId) {

        return new ResponseEntity<>(reviewService.getReviewRoundFiles(reviewRoundId, journalId), HttpStatus.OK);
    }

    @GetMapping(value = "/review-round/files/download-as-zip")
    public ResponseEntity<Object> downloadSubmissionFilesAsZip(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId,
        @RequestParam(name = "reviewRoundId") Long reviewRoundId
    ) throws IOException {

        log.debug("REST Request for download review round files as zip: {}", reviewRoundId);

        Path path = Path.of(reviewService.downloadReviewRoundFilesAsZip(journalId,submissionId, reviewRoundId).getAbsolutePath());

        byte[] file = Files.readAllBytes(path);

        HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + URLDecoder.decode(path.getFileName().toString(), StandardCharsets.UTF_8)); // Set the file name

        return ResponseEntity.ok()
            .headers(headers)
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(file);
    }

    @PutMapping("/review-round/reviewer/un-assign")
    public ResponseEntity<Void> unAssignReviewer(
        @RequestParam(name = "reviewRoundId") Long reviewRoundId,
        @RequestParam(name = "userId") String userId) {

        reviewService.unAssignReviewer(userId, reviewRoundId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/review-round/reviewer/cancel-review")
    public ResponseEntity<Void> cancelReview(
        @RequestParam(name = "reviewRoundId") Long reviewRoundId,
        @RequestParam(name = "userId") String userId,
        @RequestParam(name = "desc") String desc) {

        reviewService.cancelReviewer(userId, reviewRoundId, desc);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/reviewer/assigned-reviews")
    public ResponseEntity<List<ReviewRoundReviewerReview>> getAssignedReviews(@RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
                                                                              @RequestParam(name = "status") ReviewType ReviewType,
                                                                              @RequestParam(name = Constants.SEARCH_TEXT, required = false) String searchText,
                                                                              @ParameterObject Pageable pageable) {


        final Page<ReviewRoundReviewerReview> submissions = reviewService.getAssignedReviews(journalId, ReviewType, searchText, pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(
            ServletUriComponentsBuilder.fromCurrentRequest(),
            submissions
        );


        return new ResponseEntity<>(submissions.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/reviewer/assigned-review")
    public ResponseEntity<ReviewRoundReviewerReview> getAssignedReviewerReview(@RequestParam(name = "reviewRoundReviewId") Long reviewRoundReviewId) {

        return new ResponseEntity<>(reviewService.getAssignedReview(reviewRoundReviewId), HttpStatus.OK);
    }

    @PutMapping("/reviewer/reject-request")
    public ResponseEntity<ReviewRoundReviewerReview> rejectRequest(@RequestParam(name = "reviewRoundReviewerId") Long reviewRoundReviewerId,
                                                                   @RequestParam(name = "rejectReason") String rejectReason) {

        return new ResponseEntity<>(reviewService.rejectReviewRequest(reviewRoundReviewerId, rejectReason), HttpStatus.OK);
    }

    @PutMapping("/reviewer/complete-review")
    public ResponseEntity<ReviewRoundReviewerReview> reviewComplete(@RequestParam(name = "reviewRoundReviewerId") Long reviewRoundReviewerId,
                                                                    @RequestParam(name = "commentToEditorAndAuthor", required = false) String commentToEditorAndAuthor,
                                                                    @RequestParam(name = "commentToEditor", required = false) String commentToEditor,
                                                                    @RequestParam(name = "reviewerRecommendation") ReviewerRecommendation reviewerRecommendation) {

        return new ResponseEntity<>(reviewService.reviewComplete(reviewRoundReviewerId, reviewerRecommendation, commentToEditorAndAuthor, commentToEditor), HttpStatus.OK);
    }

    @PutMapping("/reviewer/update-review")
    public ResponseEntity<ReviewRoundReviewerReview> updateComment(@RequestParam(name = "reviewRoundReviewerId") Long reviewRoundReviewerId,
                                                                   @RequestParam(name = "editorAndAuthorComment") String editorAndAuthorComment,
                                                                   @RequestParam(name = "editorComment") String editorComment,
                                                                   @RequestParam(name = "reviewerRecommendation") ReviewerRecommendation reviewerRecommendation) {

        return new ResponseEntity<>(reviewService.updateComment(reviewRoundReviewerId, editorAndAuthorComment, editorComment, reviewerRecommendation), HttpStatus.OK);
    }

    @PutMapping("/reviewer/accept-request")
    public ResponseEntity<ReviewRoundReviewerReview> acceptRequest(@RequestParam(name = "reviewRoundReviewerId") Long reviewRoundReviewerId) {

        return new ResponseEntity<>(reviewService.acceptRequest(reviewRoundReviewerId), HttpStatus.OK);
    }

    @PutMapping("/review-round/reviewer/update")
    public ResponseEntity<ReviewRoundReviewerReview> rateReviewer(@RequestParam(name = "reviewRoundReviewerId") Long reviewRoundReviewerId,
                                                                  @RequestParam(name = "rating") Short rating,
                                                                  @RequestParam(name = "reviewerRecommendation") ReviewerRecommendation reviewerRecommendation) {

        return new ResponseEntity<>(reviewService.rateReviewer(reviewRoundReviewerId, rating, reviewerRecommendation), HttpStatus.OK);
    }


    @PostMapping(value = "/review-round/reviewer/discussion", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> createReviewRoundReviewerDiscussion(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId,
        @RequestParam(name = "reviewRoundId") Long reviewRoundId,
        @RequestParam(name = "reviewRoundReviewerId") Long reviewRoundReviewerId,
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

            return new ResponseEntity<>(reviewService.createReviewRoundReviewerDiscussion(reviewRoundReviewerId, journalId, submissionId, reviewRoundId, topic, description, httpServletRequest.getFileMap(), discussionMembers), HttpStatus.OK);

        } else {

            throw new BadRequestAlertException("No Members", null, "member.needed");
        }
    }

    @PutMapping(value = "/review-round/reviewer/discussion/status")
    public ResponseEntity<Object> updateReviewRoundReviewerDiscussionStatus(
        @RequestParam(name = "discussionId") Long discussionId,
        @RequestParam(name = "reviewRoundId") Long reviewRoundId,
        @RequestParam(name = "reviewRoundReviewerId") Long reviewRoundReviewerId,
        @RequestParam(name = "status") ReviewRoundReviewerDiscussionStatus reviewRoundReviewerDiscussionStatus) {

        log.debug("REST request to update discussion: {}", discussionId);

        return new ResponseEntity<>(reviewService.updateReviewRoundReviewerDiscussionStatus(reviewRoundReviewerId, discussionId, reviewRoundId, reviewRoundReviewerDiscussionStatus), HttpStatus.OK);
    }

    @GetMapping(value = "/review-round/reviewer/discussion")
    public ResponseEntity<Object> getReviewRoundReviewerDiscussions(
        @RequestParam(name = "reviewRoundReviewerId") Long reviewRoundReviewerId,
        @RequestParam(name = "reviewRoundId") Long reviewRoundId) {

        log.debug("REST request to get discussions: {}", reviewRoundId);

        return new ResponseEntity<>(reviewService.getReviewRoundReviewerDiscussions(reviewRoundReviewerId, reviewRoundId), HttpStatus.OK);
    }

    @PostMapping(value = "/review-round/reviewer/discussion/chat", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> createReviewRoundReviewerDiscussionChatMessage(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId,
        @RequestParam(name = "submissionId") Long submissionId,
        @RequestParam(name = "discussionId") Long discussionId,
        @RequestParam(name = "reviewRoundId") Long reviewRoundId,
        @RequestPart(name = "message") String message,
        MultipartHttpServletRequest httpServletRequest) {

        log.debug("REST request to create discussion: {} in journal {}", discussionId, journalId);

        return new ResponseEntity<>(reviewService.createReviewRoundReviewerDiscussionChatMessage(journalId, submissionId, discussionId, reviewRoundId, httpServletRequest.getFileMap(), message), HttpStatus.OK);
    }

    @GetMapping("/review-round/reviewer/discussion/chat")
    public ResponseEntity<Object> createReviewRoundReviewerDiscussionChatMessage(
        @RequestParam(name = "reviewRoundId") Long reviewRoundId,
        @RequestParam(name = "discussionId") Long discussionId) {

        log.debug("REST request to create discussion: {}", discussionId);
        return new ResponseEntity<>(reviewService.getReviewRoundReviewerDiscussionChatMessages(reviewRoundId, discussionId), HttpStatus.OK);
    }
}
