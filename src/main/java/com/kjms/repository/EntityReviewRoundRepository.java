package com.kjms.repository;

import com.kjms.domain.EntityReviewRound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EntityReviewRoundRepository extends JpaRepository<EntityReviewRound, Long> {

    @Query("SELECT rr.round FROM EntityReviewRound as rr WHERE rr.journal.id =:journalId AND " +
        "rr.submission.id =:submissionId and rr.isDeleted IS false ORDER BY rr.round DESC")
    List<Integer> getReviewRoundNumber(@Param("journalId") Long journalId, @Param("submissionId") Long submissionId);

    @Query("FROM EntityReviewRound as rr WHERE rr.journal.id =:journalId AND " +
        "rr.submission.id =:submissionId and rr.isDeleted IS false ORDER BY rr.round ASC")
    List<EntityReviewRound> getReviewRounds(@Param("journalId") Long journalId, @Param("submissionId") Long submissionId);

    @Query("FROM EntityReviewRound as rr WHERE rr.journal.id =:journalId AND " +
        "rr.submission.id =:submissionId AND rr.id =:reviewRoundId AND rr.isDeleted IS false ORDER BY rr.round ASC")
    EntityReviewRound getReviewRound(@Param("journalId") Long journalId, @Param("submissionId") Long submissionId,@Param("reviewRoundId") Long reviewRoundId);
}
