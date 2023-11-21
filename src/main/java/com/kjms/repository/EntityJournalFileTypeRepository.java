package com.kjms.repository;

import com.kjms.domain.EntityJournal;
import com.kjms.domain.EntityJournalFileType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EntityJournalFileTypeRepository extends JpaRepository<EntityJournalFileType, Long> {

    @Query("FROM EntityJournalFileType jsft WHERE jsft.journal.id=:journalId AND jsft.isDeleted = false")
    List<EntityJournalFileType> findAllByIsDeletedIsFalseAndJournal(@Param("journalId") Long journalId);

    @Query("FROM EntityJournalFileType jsft WHERE jsft.journal.id=:journalId AND jsft.isDeleted = false")
    List<EntityJournalFileType> getJournalFileTypes(@Param("journalId") Long journalId);

    @Modifying
    @Query("UPDATE EntityJournalFileType jft SET jft.isDeleted = true WHERE jft.journal.id =:journalId AND jft.fileType.id IN (:fileTypeIds)")
    void deleteFileTypes(@Param("journalId") Long journalId, @Param("fileTypeIds") List<Long> fileTypeIds);
}
