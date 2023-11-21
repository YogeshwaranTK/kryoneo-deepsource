package com.kjms.repository;

import com.kjms.domain.EntityCopyEditedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EntityCopyEditedFileRepository extends JpaRepository<EntityCopyEditedFile, Long> {

    @Query("FROM EntityCopyEditedFile cef where cef.journal.id =:journalId AND cef.submission.id =:submissionId ")
    List<EntityCopyEditedFile> getCopyEditedFiles(@Param("journalId") Long journalId, @Param("submissionId") Long submissionId);

    @Query("SELECT cef.filePath FROM EntityCopyEditedFile cef where cef.journal.id =:journalId AND cef.submission.id =:submissionId ")
    List<String> getCopyEditedFilePaths(@Param("journalId") Long journalId, @Param("submissionId") Long submissionId);
}
