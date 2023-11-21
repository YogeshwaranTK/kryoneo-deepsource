package com.kjms.repository;

import com.kjms.domain.EntityReviewRoundDiscussion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EntityReviewRoundDiscussionRepository extends JpaRepository<EntityReviewRoundDiscussion, Long> {

    @Query("FROM EntityReviewRoundDiscussion rrd WHERE rrd.reviewRound.id =:reviewRoundId AND rrd.id = :discussionId")
    Optional<EntityReviewRoundDiscussion> findDiscussion(@Param("discussionId") Long discussionId, @Param("reviewRoundId") Long reviewRoundId);

    @Query("FROM EntityReviewRoundDiscussion rrd WHERE rrd.reviewRound.id =:reviewRoundId")
    List<EntityReviewRoundDiscussion> getDiscussions(@Param("reviewRoundId") Long reviewRoundId);

    @Query("FROM EntityReviewRoundDiscussion rrd LEFT JOIN EntityReviewRoundDiscussionMember rrdm ON rrdm.discussion.id = rrd.id WHERE rrd.reviewRound.id =:reviewRoundId AND rrdm.user.id=:userId")
    List<EntityReviewRoundDiscussion> getReviewerDiscussions(@Param("reviewRoundId") Long reviewRoundId, @Param("userId") String userId);
}
