package com.kjms.repository;

import com.kjms.domain.EntityJournal;
import com.kjms.domain.EntitySubmission;
import com.kjms.domain.EntitySubmissionAuthor;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link EntitySubmissionAuthor} entity.
 */
@Repository
public interface EntitySubmissionAuthorRepository extends JpaRepository<EntitySubmissionAuthor, Long> {

    @Query("FROM EntitySubmissionAuthor sa WHERE sa.journal.id =:journalId AND sa.submission.id =:submissionId AND sa.isDeleted is FALSE ")
    List<EntitySubmissionAuthor> getJournalSubmissionAuthors(@Param("journalId") Long journalId, @Param("submissionId") Long submissionId);


    @Query("SELECT sa.id FROM EntitySubmissionAuthor sa WHERE sa.journal.id =:journalId AND sa.submission.id =:submissionId AND sa.isDeleted is FALSE ")
    List<Long> getJournalSubmissionAuthorIds(@Param("journalId") Long journalId, @Param("submissionId") Long submissionId);

    @Modifying
    @Query("UPDATE EntitySubmissionAuthor sa SET sa.isDeleted = true WHERE sa.id IN(:authorIds) ")
    void deleteSubmissionAuthor(@Param("authorIds") List<Long> authorIds);
}
