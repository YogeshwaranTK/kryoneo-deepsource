package com.kjms.repository;

import com.kjms.domain.EntityProductionDiscussionMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EntityProductionDiscussionMessageRepository extends JpaRepository<EntityProductionDiscussionMessage, Long> {
    @Query("FROM EntityProductionDiscussionMessage pdm WHERE pdm.journal.id =:journalId AND pdm.submission.id =:submissionId AND pdm.discussion.id =:discussionId ORDER BY pdm.createdAt DESC")
    List<EntityProductionDiscussionMessage> getMessages(@Param("journalId") Long journalId, @Param("submissionId") Long submissionId, @Param("discussionId") Long discussionId);
}
