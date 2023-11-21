package com.kjms.repository;

import com.kjms.domain.EntityJournalEditorialRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EntityJournalEditorialRoleRepository extends JpaRepository<EntityJournalEditorialRole, Long> {

    @Query("FROM EntityJournalEditorialRole jer WHERE jer.journal.id =:journalId AND jer.id=:roleId AND jer.isDeleted IS false")
    Optional<EntityJournalEditorialRole> findJournalEditorialRoleByJournalIdAndId(@Param("roleId") Long roleId, @Param("journalId") Long journalId);

    @Query("FROM EntityJournalEditorialRole jer WHERE jer.journal.id =:journalId AND jer.isDeleted IS false")
    List<EntityJournalEditorialRole> getJournalEditorialRoles(@Param("journalId") Long journalId);
}
