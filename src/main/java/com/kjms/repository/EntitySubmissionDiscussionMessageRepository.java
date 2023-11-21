package com.kjms.repository;

import com.kjms.domain.EntitySubmissionDiscussionMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EntitySubmissionDiscussionMessageRepository extends JpaRepository<EntitySubmissionDiscussionMessage, Long> {

    @Query("FROM EntitySubmissionDiscussionMessage sdm WHERE sdm.journal.id =:journalId AND sdm.submission.id =:submissionId AND sdm.discussion.id =:discussionId ORDER BY sdm.createdAt DESC")
    List<EntitySubmissionDiscussionMessage> getMessages(@Param("journalId") Long journalId, @Param("submissionId") Long submissionId, @Param("discussionId") Long discussionId);


}
