package com.kjms.repository;

import com.kjms.domain.EntityJournal;

import java.util.List;
import java.util.Optional;

import com.kjms.domain.EntityUser;
import com.kjms.domain.EntityProductionContributor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link EntityJournal} entity.
 */
@Repository
public interface EntityJournalRepository extends JpaRepository<EntityJournal, Long> {
    Optional<EntityJournal> findOneByTitleIgnoreCaseAndIsDeletedIsFalse(String title);

    Optional<EntityJournal> findOneByIdNotAndTitleIgnoreCaseAndIsDeletedIsFalse(Long id, String title);

    Optional<EntityJournal> findOneByKeyAndIsDeletedIsFalse(String key);

    Optional<EntityJournal> findOneByIdNotAndKeyAndIsDeletedIsFalse(Long id, String key);

    Optional<EntityJournal> findOneByIdAndIsDeletedIsFalse(Long journalId);
    List<EntityJournal> findAllByIsDeletedIsFalse();

    @Query("FROM EntityJournal journal WHERE journal.isDeleted=false " +
        "AND" +
        " ( (LOWER(journal.title) LIKE LOWER(CONCAT('%',:searchText, '%'))) " +
        "OR" +
        " (LOWER(journal.key) LIKE LOWER(CONCAT('%',:searchText, '%')) ) ) " +
        "AND" +
        " ( " +
        "journal.id IN ( SELECT DISTINCT(journalAuthor.journal.id) FROM EntityJournalAuthor as journalAuthor WHERE journalAuthor.user.id =:userId) " +
        "OR" +
        " journal.id IN (SELECT DISTINCT(journalReviewer.journal.id) FROM EntityJournalReviewer as journalReviewer WHERE journalReviewer.user.id=:userId )" +
        "OR" +
        " journal.id IN (SELECT DISTINCT(journalEditorialUser.entityJournalEditorialRole.journal.id) FROM EntityJournalEditorialUser as journalEditorialUser WHERE journalEditorialUser.user.id=:userId )" +
        ") "
    )
    Page<EntityJournal> findAllByTitleAndKeyContainingAndIsDeletedIsFalse(@Param("userId") String userId, @Param("searchText") String searchText, Pageable pageable);
}
