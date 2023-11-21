package com.kjms.repository;


import com.kjms.domain.EntityJournal;
import com.kjms.domain.EntityJournalCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;


/**
 * Spring Data JPA repository for the {@link EntityJournalCategory} entity.
 */
public interface EntityJournalCategoryRepository extends JpaRepository<EntityJournalCategory, Long> {
    @Query(
        "SELECT journalCategory.categoryId.id FROM EntityJournalCategory journalCategory WHERE journalCategory.isDeleted=false "
            + " AND journalCategory.journal = :journalId "
    )
    Stream<String> findAllCategoryId_IdByIsDeletedIsFalseAndJournalId(@Param("journalId") EntityJournal journalId);

    Page<EntityJournalCategory> findAllByIsDeletedIsFalseAndJournalId(EntityJournal journalId, Pageable pageable);

    Page<EntityJournalCategory> findAllByIsDeletedIsFalseAndJournalIdAndCategoryId_NameContaining(EntityJournal journalId, String searchText, Pageable pageable);

    Optional<EntityJournalCategory> findOneByIdAndIsDeletedIsFalse(Long id);



    @Modifying
    @Query("UPDATE EntityJournalCategory jc SET jc.isDeleted = true WHERE" +
        " jc.journal.id =:journalId AND jc.categoryId.id IN (:categoryIds)")
    void deleteCategories(@Param("journalId") Long journalId,
                          @Param("categoryIds")  List<String> categoryIds);
}
