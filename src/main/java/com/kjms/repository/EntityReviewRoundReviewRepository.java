package com.kjms.repository;

import com.kjms.domain.EntityReviewRoundReview;
import com.kjms.domain.ReviewStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EntityReviewRoundReviewRepository extends JpaRepository<EntityReviewRoundReview, Long> {


    List<EntityReviewRoundReview> findByReviewRoundId(Long reviewRoundId);

    @Query("FROM EntityReviewRoundReview rrr WHERE rrr.reviewRound.id =:reviewRoundId AND rrr.journalReviewer.user.id=:userId")
    Optional<EntityReviewRoundReview> getReviewer(@Param("reviewRoundId") Long reviewRoundId, @Param("userId") String userId);

    @Query("FROM EntityReviewRoundReview rrr WHERE rrr.reviewStatus IN(:reviewCurrentStatuses) AND rrr.journalReviewer.user.id=:userId AND rrr.reviewRound.journal.id=:journalId")
    Page<EntityReviewRoundReview> getReviewerForPending(@Param("userId")String userId, @Param("reviewCurrentStatuses") List<ReviewStatus> reviewCurrentStatuses, @Param("journalId") Long journalId, Pageable pageable);

    @Query("FROM EntityReviewRoundReview rrr WHERE rrr.reviewStatus NOT IN(:reviewCurrentStatuses) AND rrr.journalReviewer.user.id=:userId AND rrr.reviewRound.journal.id=:journalId")
    Page<EntityReviewRoundReview> getReviewerForCompleted(@Param("userId")String userId, @Param("reviewCurrentStatuses") List<ReviewStatus> reviewCurrentStatuses, @Param("journalId") Long journalId, Pageable pageable);
}
