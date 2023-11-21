package com.kjms.repository;

import com.kjms.domain.EntityReviewRoundFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EntityReviewRoundFileRepository extends JpaRepository<EntityReviewRoundFile, Long> {

    @Query("FROM EntityReviewRoundFile rrf WHERE rrf.reviewRound.id =:reviewRoundId AND rrf.reviewRound.journal.id=:journalId")
    List<EntityReviewRoundFile> getFiles(@Param("reviewRoundId") Long reviewRoundId, @Param("journalId") Long journalId);

    @Query("SELECT rrf.filePath FROM EntityReviewRoundFile rrf WHERE rrf.reviewRound.id =:reviewRoundId AND rrf.reviewRound.journal.id=:journalId AND rrf.reviewRound.submission.id=:submissionId")
    List<String> getSubmissionFilePaths(@Param("journalId") Long journalId, @Param("submissionId") Long submissionId, @Param("reviewRoundId") Long reviewRoundId);
}
