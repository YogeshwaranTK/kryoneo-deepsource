package com.kjms.repository;

import com.kjms.domain.EntityReviewerReplyFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EntityReviewerReplyFileRepository extends JpaRepository<EntityReviewerReplyFile, Long> {

    @Query("FROM EntityReviewerReplyFile rrf WHERE rrf.reviewRound.id=:reviewRoundId AND rrf.reviewRoundReviewer.id = :reviewRoundReviewerId AND rrf.reviewRound.journal.id=:journalId")
    List<EntityReviewerReplyFile> getReviewerReplyFiles(@Param("reviewRoundReviewerId") Long reviewRoundReviewerId,@Param("reviewRoundId") Long reviewRoundId, @Param("journalId") Long journalId);
}
