package com.kjms.repository;

import com.kjms.domain.EntityJournal;
import com.kjms.domain.EntityJournalLanguage;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link EntityJournalLanguage} entity.
 */
@Repository
public interface EntityJournalSubmissionLanguageRepository extends JpaRepository<EntityJournalLanguage, Long> {
    Optional<EntityJournalLanguage> findOneByIdAndIsDeletedIsFalse(Long id);

    Page<EntityJournalLanguage> findAllByJournalIdAndIsDeletedIsFalse(Long journalId, Pageable pageable);

    List<EntityJournalLanguage> findAllByJournalAndIsDeletedIsFalse(EntityJournal journalId);


    @Query(
        "FROM EntityJournalLanguage journalLang WHERE journalLang.isDeleted=false AND journalLang.journal=:journalId" +
            " AND ((LOWER(journalLang.language.name) LIKE LOWER(CONCAT('%',:searchText, '%'))) OR (LOWER(journalLang.language.langKey) LIKE LOWER(CONCAT('%',:searchText, '%'))))"
    )
    Page<EntityJournalLanguage> findAllByJournalIdAndIsDeletedIsFalseAndTextContaining(
        @Param("journalId") EntityJournal journalId,
        @Param("searchText") String searchText,
        Pageable pageable
    );

    Optional<EntityJournalLanguage> findOneByIsDeletedIsFalseAndJournalIdAndLanguage_LangKey(EntityJournal entityJournal, String langId);

    @Query("SELECT jl.language.langKey FROM EntityJournalLanguage jl WHERE jl.isDeleted=false AND jl.journal.id = :journalId")
    List<String> findAllJournalIdsByJournalAndIsDeletedIsFalse(@Param("journalId") Long journalId);


    @Modifying
    @Query("UPDATE EntityJournalLanguage jl SET jl.isDeleted = true WHERE" +
        " jl.journal.id =:journalId AND jl.language.langKey IN (:languageIds)")
    void deleteLanguages(@Param("journalId") Long journalId,
                         @Param("languageIds") List<String> languageIds);
}
