package com.kjms.repository;

import com.kjms.domain.EntityCopyEditingDiscussion;
import com.kjms.domain.EntityProductionDiscussion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EntityCopyEditingDiscussionRepository extends JpaRepository<EntityCopyEditingDiscussion, Long> {

    @Query("FROM EntityCopyEditingDiscussion pd WHERE pd.submission.id =:submissionId AND pd.journal.id =:journalId AND pd.id = :discussionId")
    Optional<EntityCopyEditingDiscussion> findDiscussion(@Param("discussionId") Long discussionId, @Param("journalId") Long journalId, @Param("submissionId") Long submissionId);

    @Query("FROM EntityCopyEditingDiscussion pd WHERE pd.submission.id =:submissionId AND pd.journal.id =:journalId")
    List<EntityCopyEditingDiscussion> getDiscussions(@Param("journalId") Long journalId, @Param("submissionId") Long submissionId);

}
