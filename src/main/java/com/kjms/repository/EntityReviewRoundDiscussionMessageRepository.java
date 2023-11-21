package com.kjms.repository;

import com.kjms.domain.EntityReviewRoundDiscussionMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EntityReviewRoundDiscussionMessageRepository extends JpaRepository<EntityReviewRoundDiscussionMessage, Long> {
    @Query("FROM EntityReviewRoundDiscussionMessage rrdm WHERE rrdm.reviewRound.id =:reviewRoundId AND rrdm.discussion.id =:discussionId ORDER BY rrdm.createdAt DESC")
    List<EntityReviewRoundDiscussionMessage> getMessages(@Param("reviewRoundId") Long reviewRoundId,@Param("discussionId") Long discussionId);
}
