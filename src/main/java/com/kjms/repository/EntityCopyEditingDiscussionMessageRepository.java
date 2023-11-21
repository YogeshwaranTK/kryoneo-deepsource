package com.kjms.repository;

import com.kjms.domain.EntityCopyEditingDiscussionMessage;
import com.kjms.domain.EntityProductionDiscussionMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EntityCopyEditingDiscussionMessageRepository extends JpaRepository<EntityCopyEditingDiscussionMessage, Long> {

    @Query("FROM EntityCopyEditingDiscussionMessage pdm WHERE pdm.journal.id =:journalId AND pdm.submission.id =:submissionId AND pdm.discussion.id =:discussionId ORDER BY pdm.createdAt DESC")
    List<EntityCopyEditingDiscussionMessage> getMessages(@Param("journalId") Long journalId, @Param("submissionId") Long submissionId, @Param("discussionId") Long discussionId);
}
