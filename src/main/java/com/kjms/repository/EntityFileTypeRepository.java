package com.kjms.repository;

import com.kjms.domain.EntityFileType;
import com.kjms.domain.EntityJournalFileType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EntityFileTypeRepository extends JpaRepository<EntityFileType, Long> {
    Optional<EntityFileType> findOneByIdAndIsDeletedIsFalse(Long id);

    Set<EntityFileType> findAllByIdInAndIsDeletedIsFalse(List<Long> ids);

    Page<EntityFileType> findAllByIsDeletedIsFalseAndIsDefaultIsFalse(Pageable pageable);

    Page<EntityFileType> findAllByIsDeletedIsFalseAndNameContainingIgnoreCaseAndIsDefaultIsFalse(String name, Pageable pageable);

    @Query("FROM EntityJournalFileType ft WHERE ft.journal.id =:journalId AND ft.fileType.id=:fileTypeId AND ft.isDeleted IS false ")
    Optional<EntityJournalFileType> findFileTypeByJournalIdAndFileTypeId(@Param("journalId") Long journalId, @Param("fileTypeId") Long fileTypeId);

    @Query("SELECT ft.id FROM EntityFileType ft WHERE  ft.isDefault IS true AND ft.isDeleted IS false ")
    List<Long> findAllByIsDeletedFalseAndIsDefaultIsTrue();
}
