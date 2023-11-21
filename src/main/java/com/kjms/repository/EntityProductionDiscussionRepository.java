package com.kjms.repository;


import com.kjms.domain.EntityProductionDiscussion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EntityProductionDiscussionRepository extends JpaRepository<EntityProductionDiscussion, Long> {

    @Query("FROM EntityProductionDiscussion pd WHERE pd.submission.id =:submissionId AND pd.journal.id =:journalId")
    List<EntityProductionDiscussion> getDiscussions(@Param("journalId") Long journalId, @Param("submissionId") Long submissionId);

    @Query("FROM EntityProductionDiscussion pd WHERE pd.submission.id =:submissionId AND pd.journal.id =:journalId AND pd.id = :discussionId")
    Optional<EntityProductionDiscussion> findDiscussion(@Param("discussionId") Long discussionId, @Param("journalId") Long journalId, @Param("submissionId") Long submissionId);
}
