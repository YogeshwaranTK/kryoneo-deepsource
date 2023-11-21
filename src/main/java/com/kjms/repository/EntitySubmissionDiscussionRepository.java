package com.kjms.repository;

import com.kjms.domain.EntitySubmissionDiscussion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EntitySubmissionDiscussionRepository extends JpaRepository<EntitySubmissionDiscussion, Long> {

    @Query("FROM EntitySubmissionDiscussion sd WHERE sd.submission.id =:submissionId AND sd.journal.id =:journalId")
    List<EntitySubmissionDiscussion> getDiscussions(@Param("journalId") Long journalId, @Param("submissionId") Long submissionId);

    @Query("FROM EntitySubmissionDiscussion sd WHERE sd.submission.id =:submissionId AND sd.journal.id =:journalId AND sd.id = :discussionId")
    Optional<EntitySubmissionDiscussion> findDiscussion(@Param("discussionId") Long discussionId, @Param("journalId") Long journalId, @Param("submissionId") Long submissionId);

}
