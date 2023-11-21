package com.kjms.repository;

import com.kjms.domain.EntitySubmissionFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the {@link EntitySubmissionFile} entity.
 */
public interface EntitySubmissionFileRepository extends JpaRepository<EntitySubmissionFile, Long> {

    @Query("FROM EntitySubmissionFile sf WHERE sf.journal.id =:journalId AND sf.submission.id=:submissionId AND sf.isDeleted is false")
    List<EntitySubmissionFile> getSubmissionFiles(@Param("journalId") Long journalId, @Param("submissionId") Long submissionId);

    @Query("SELECT sf.id FROM EntitySubmissionFile sf WHERE sf.journal.id =:journalId AND sf.submission.id=:submissionId")
    List<Long> getSubmissionFileIds(@Param("journalId") Long journalId, @Param("submissionId") Long submissionId);


    @Modifying
    @Query("UPDATE EntitySubmissionFile pc SET pc.isDeleted = true WHERE pc.submission.id =:submissionId AND pc.journal.id =:journalId AND pc.id IN (:fileIds)")
    void deleteSubmissionFile(@Param("submissionId") Long submissionId, @Param("journalId") Long journalId, @Param("fileIds") List<Long> fileIds);

    @Query("FROM EntitySubmissionFile sf WHERE sf.journal.id =:journalId AND sf.submission.id=:submissionId AND sf.id =:originalFileId")
    Optional<EntitySubmissionFile> getSubmissionFile(@Param("journalId") Long journalId, @Param("submissionId") Long submissionId, @Param("originalFileId") Long originalFileId);

    @Query("SELECT sf.filePath FROM EntitySubmissionFile sf WHERE sf.journal.id =:journalId AND sf.submission.id=:submissionId AND sf.isDeleted is false")
    List<String> getSubmissionFilePaths(@Param("journalId") Long journalId, @Param("submissionId") Long submissionId);
}
