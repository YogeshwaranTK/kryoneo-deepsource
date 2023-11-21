package com.kjms.repository;

import com.kjms.domain.EntityCopyEditingContributor;
import com.kjms.domain.EntityCopyEditingDraftFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EntityCopyEditingDraftFileRepository extends JpaRepository<EntityCopyEditingDraftFile, Long> {

    @Query("FROM EntityCopyEditingDraftFile cedf where cedf.journal.id =:journalId AND cedf.submission.id =:submissionId ")
    List<EntityCopyEditingDraftFile> getDraftFiles(@Param("journalId") Long journalId, @Param("submissionId") Long submissionId);

    @Query("SELECT cedf.filePath FROM EntityCopyEditingDraftFile cedf where cedf.journal.id =:journalId AND cedf.submission.id =:submissionId ")
    List<String> getCopyEditingDraftFilePaths(@Param("journalId") Long journalId, @Param("submissionId") Long submissionId);
}
