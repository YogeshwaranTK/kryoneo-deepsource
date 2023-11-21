package com.kjms.repository;

import com.kjms.domain.EntityJournalAuthor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EntityJournalAuthorRepository extends JpaRepository<EntityJournalAuthor, Long> {
    @Query("FROM EntityJournalAuthor ja WHERE ja.journal.id =:journalId AND ja.isDeleted is false ")
    Page<EntityJournalAuthor> getAuthorsById(@Param("journalId") Long journalId, Pageable pageable);

    @Query("FROM EntityJournalAuthor ja WHERE ja.journal.id =:journalId AND ja.user.id =:userId AND ja.isDeleted is false ")
    Optional<EntityJournalAuthor> getAuthorById(@Param("journalId") Long journalId, @Param("userId") String userId);

    @Modifying
    @Query("UPDATE EntityJournalAuthor ja SET ja.isDeleted = true, ja.deletedRemarks=:deletedRemarks " +
        "WHERE ja.journal.id =:journalId  AND ja.user.id =:userId")
    void deleteAuthorRoleFromUser(@Param("journalId") Long journalId, @Param("userId") String userId, @Param("deletedRemarks") String deletedRemarks);

    @Query("select case when (count(ja.id) > 0) then true else false end FROM EntityJournalAuthor ja WHERE ja.journal.id =:journalId AND ja.user.id =:userId AND ja.isDeleted is false")
    Boolean isAuthorRoleExist(@Param("journalId") Long journalId, @Param("userId") String userId);

    @Query("FROM EntityJournalAuthor ja WHERE ja.journal.id =:journalId And ja.isDeleted is false ")
    List<EntityJournalAuthor> getAuthors(@Param("journalId") Long journalId);
}
