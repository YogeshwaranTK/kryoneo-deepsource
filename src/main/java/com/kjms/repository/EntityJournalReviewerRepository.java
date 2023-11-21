package com.kjms.repository;

import com.kjms.domain.EntityJournalReviewer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EntityJournalReviewerRepository extends JpaRepository<EntityJournalReviewer, Long> {


    @Query("FROM EntityJournalReviewer ja WHERE ja.journal.id =:journalId AND ja.isDeleted is false ")
    Page<EntityJournalReviewer> getReviewersById(@Param("journalId") Long journalId, Pageable pageable);

    @Query("FROM EntityJournalReviewer ja WHERE ja.journal.id =:journalId AND ja.user.id =:userId AND ja.isDeleted is false ")
    Optional<EntityJournalReviewer> getReviewerById(@Param("journalId") Long journalId, @Param("userId") String userId);

    @Modifying
    @Query("UPDATE EntityJournalReviewer jr SET jr.isDeleted = true, jr.deletedRemarks=:deletedRemarks " +
        "WHERE jr.journal.id =:journalId  AND jr.user.id =:userId")
    void deleteReviewerRoleFromUser(@Param("journalId") Long journalId, @Param("userId") String userId, @Param("deletedRemarks") String deletedRemarks);

    @Query("select case when (count(ja.id) > 0) then true else false end FROM EntityJournalReviewer ja WHERE ja.journal.id =:journalId AND ja.user.id =:userId AND ja.isDeleted is false")
    boolean isReviewerRoleExist(@Param("journalId") Long journalId, @Param("userId") String userId);

    @Query("FROM EntityJournalReviewer ja WHERE ja.journal.id =:journalId AND ja.user.id =:userId AND ja.isDeleted is false")
    Optional<EntityJournalReviewer> getReviewer(@Param("journalId") Long journalId, @Param("userId") String userId);

    @Query("FROM EntityJournalReviewer ja WHERE ja.journal.id =:journalId And ja.isDeleted is false ")
    List<EntityJournalReviewer> getReviewers(@Param("journalId") Long journalId);
}
