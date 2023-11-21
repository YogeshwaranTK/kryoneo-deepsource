package com.kjms.repository;

import com.kjms.domain.EntityReviewRoundReviewerDiscussion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EntityReviewRoundReviewerDiscussionRepository extends JpaRepository<EntityReviewRoundReviewerDiscussion, Long> {
    @Query("FROM EntityReviewRoundReviewerDiscussion rrd WHERE rrd.reviewRound.id =:reviewRoundId AND rrd.id = :discussionId")
    Optional<EntityReviewRoundReviewerDiscussion> findDiscussion(@Param("discussionId") Long discussionId, @Param("reviewRoundId") Long reviewRoundId);

    @Query("FROM EntityReviewRoundReviewerDiscussion rrd WHERE rrd.reviewRound.id =:reviewRoundId AND rrd.reviewRoundReview.id=:reviewRoundReviewerId")
    List<EntityReviewRoundReviewerDiscussion> getDiscussions(@Param("reviewRoundReviewerId") Long reviewRoundReviewerId, @Param("reviewRoundId") Long reviewRoundId);
}
