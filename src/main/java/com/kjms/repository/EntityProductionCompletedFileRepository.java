package com.kjms.repository;

import com.kjms.domain.EntityProductionCompletedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EntityProductionCompletedFileRepository extends JpaRepository<EntityProductionCompletedFile, Long> {

    @Query("FROM EntityProductionCompletedFile pcf where pcf.submission.id =:submissionId AND pcf.submission.journal.id =:journalId")
    List<EntityProductionCompletedFile> getFiles(@Param("journalId") Long journalId, @Param("submissionId") Long submissionId);
}
