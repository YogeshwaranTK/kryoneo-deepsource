package com.kjms.repository;

import com.kjms.domain.*;

import java.util.List;
import java.util.Optional;

import com.kjms.service.dto.JournalsReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link EntitySubmission} entity.
 */
@Repository
public interface EntitySubmissionRepository extends JpaRepository<EntitySubmission, Long> {

    @Query("FROM EntitySubmission es WHERE es.journal.id =:journalId AND es.id=:submissionId AND es.isDeleted is false")
    Optional<EntitySubmission> getSubmission(@Param("submissionId") Long submissionId, @Param("journalId") Long journalId);

//    Optional<EntitySubmission> findOneByIsDeletedIsFalseAndIdAndReviewStatus(Long id, ReviewStatus reviewStatus);

    @Query(
        "FROM EntitySubmission submissionArticle WHERE "
            + " submissionArticle.journal=:journalId "
            + "  AND (submissionArticle.workflowStage=:positionType)"
            + " AND ((submissionArticle.status = :draftStatus AND submissionArticle.createdBy = :createdBy) "
            + " OR (submissionArticle.status != :draftStatus AND (submissionArticle.createdBy = :createdBy OR submissionArticle.createdBy != :createdBy)))"
    )
    Page<EntitySubmission> findAllByJournalIdAndIsDeletedIsFalse(
        @Param("journalId") EntityJournal journalId,
        @Param("draftStatus") SubmissionStatus draftStatus,
        @Param("createdBy") EntityUser createdBy,
        @Param("positionType") WorkflowStage workflowStage,
        Pageable pageable
    );

    @Query(
        "FROM EntitySubmission submissionArticle  "
            + " WHERE submissionArticle.journal=:journalId  AND submissionArticle.createdBy=:createdBy AND submissionArticle.workflowStage=:positionType "
    )
    Page<EntitySubmission> findAllByJournalIdAndIsDeletedIsFalse(
        @Param("journalId") EntityJournal journalId,
        @Param("createdBy") EntityUser createdBy,
        @Param("positionType") WorkflowStage workflowStage,
        Pageable pageable
    );

    @Query(
        "FROM EntitySubmission as submission WHERE submission.isDeleted=false AND " +
            " submission.journal=:journalId AND submission.workflowStage=:positionType " +
            " AND (LOWER(submission.title) LIKE LOWER(CONCAT('%',:searchText, '%'))) "
            + " AND submission.createdBy = :createdBy"
    )
    Page<EntitySubmission> findAllByJournalIdAndIsDeletedIsFalseAndTextContaining(
        @Param("journalId") EntityJournal journalId,
        @Param("searchText") String searchText,
        @Param("createdBy") EntityUser createdBy,
        @Param("positionType") WorkflowStage workflowStage,
        Pageable pageable
    );

    @Query(
        "SELECT NEW com.kjms.service.dto.JournalsReport(journal.id, " +
            "COUNT(submission), " +
            "SUM(CASE WHEN submission.workflowStage = com.kjms.domain.WorkflowStage.REVIEW THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN submission.workflowStage = com.kjms.domain.WorkflowStage.SUBMISSION THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN submission.workflowStage = com.kjms.domain.WorkflowStage.COPY_EDITING THEN 1 ELSE 0 END), " +
            "SUM(CASE WHEN submission.workflowStage = com.kjms.domain.WorkflowStage.PRODUCTION THEN 1 ELSE 0 END), journal.published, journal.publishedAt, journal.createdAt, journal.title) " +
            "FROM EntityJournal journal LEFT JOIN EntitySubmission submission ON journal.id = submission.journal.id AND submission.isDeleted = false " +
            "WHERE journal.isDeleted = false " +
            "GROUP BY journal.id"
    )
    List<JournalsReport> findJournalSubmissionsCount();
}
