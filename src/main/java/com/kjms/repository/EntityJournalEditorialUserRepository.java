package com.kjms.repository;

import com.kjms.domain.EditorialRoleType;
import com.kjms.domain.EntityJournalEditorialRole;
import com.kjms.domain.EntityJournalEditorialUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EntityJournalEditorialUserRepository extends JpaRepository<EntityJournalEditorialUser, Long> {

    @Query("FROM EntityJournalEditorialUser jeu WHERE jeu.entityJournalEditorialRole.journal.id=:journalId AND jeu.isDeleted is false  AND jeu.entityJournalEditorialRole.isDeleted is false ")
    Page<EntityJournalEditorialUser> getEditorialUsers(@Param("journalId") Long journalId, Pageable pageable);

    @Modifying
    @Query("UPDATE EntityJournalEditorialUser jeu SET jeu.isDeleted = true " +
        ", jeu.deletedRemarks=:deletedRemarks WHERE jeu.entityJournalEditorialRole.id IN " +
        "(SELECT jer.id FROM EntityJournalEditorialRole jer " +
        "WHERE jer.journal.id =:journalId) AND jeu.user.id =:userId")
    void deleteEditorialRoleFromUser(@Param("userId") String userId, @Param("journalId") Long journalId, @Param("deletedRemarks") String deletedRemarks);

    @Query("SELECT jeu.entityJournalEditorialRole FROM EntityJournalEditorialUser jeu WHERE jeu.entityJournalEditorialRole.journal.id=:journalId AND jeu.user.id =:userId AND jeu.isDeleted is false  AND jeu.entityJournalEditorialRole.isDeleted is false")
    Optional<EntityJournalEditorialRole> getUserEditorialRole(@Param("journalId") Long journalId, @Param("userId") String userId);

    @Query("FROM EntityJournalEditorialUser jeu WHERE jeu.entityJournalEditorialRole.journal.id=:journalId AND LOWER(jeu.user.fullName) LIKE LOWER(CONCAT('%',:searchText, '%')) AND jeu.isDeleted is false  AND jeu.entityJournalEditorialRole.isDeleted is false ")
    Page<EntityJournalEditorialUser> getEditorialUsersWithSearch(@Param("journalId") Long journalId, @Param("searchText") String searchText, Pageable pageable);

    @Query("FROM EntityJournalEditorialUser jeu WHERE jeu.entityJournalEditorialRole.journal.id=:journalId AND jeu.entityJournalEditorialRole.editorialRoleType= :roleType  AND jeu.isDeleted is false AND jeu.entityJournalEditorialRole.isDeleted is false ")
    List<EntityJournalEditorialUser> getJournalManagers(@Param("journalId") Long journalId,@Param("roleType")EditorialRoleType editorialRoleType);
}
