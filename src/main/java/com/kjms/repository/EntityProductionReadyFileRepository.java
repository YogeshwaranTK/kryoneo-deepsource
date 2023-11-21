package com.kjms.repository;

import com.kjms.domain.EntityProductionReadyFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EntityProductionReadyFileRepository extends JpaRepository<EntityProductionReadyFile, Long> {

    @Query("FROM EntityProductionReadyFile prf where prf.submission.id =:submissionId AND prf.submission.journal.id =:journalId AND prf.isDeleted is false")
    List<EntityProductionReadyFile> getFiles(@Param("journalId") Long journalId, @Param("submissionId") Long submissionId);

    @Modifying
    @Query("UPDATE EntityProductionReadyFile prf SET prf.isDeleted = true WHERE  prf.submission.id =:submissionId AND prf.id=:fileId")
    void deleteProductionReadyFile(@Param("submissionId") Long submissionId, @Param("fileId") Long fileId);

    @Modifying
    @Query("UPDATE EntityCopyEditingContributor pc SET pc.isDeleted = true WHERE pc.submission.id =:submissionId AND pc.user.id =:contributorId")
    void removeContributor(@Param("submissionId") Long submissionId, @Param("contributorId") String contributorId);

    @Query("SELECT prf.filePath FROM EntityProductionReadyFile prf where prf.submission.id =:submissionId AND prf.submission.journal.id =:journalId AND prf.isDeleted is false")
    List<String> getProductionReadyFilePaths(@Param("journalId") Long journalId, @Param("submissionId") Long submissionId);

    @Query("SELECT prf.filePath FROM EntityProductionReadyFile prf where prf.submission.id =:submissionId AND prf.submission.journal.id =:journalId AND prf.isDeleted is false")
    List<String> getProductionCompletedFilePaths(@Param("journalId") Long journalId, @Param("submissionId") Long submissionId);
}
