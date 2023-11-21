package com.kjms.repository;

import com.kjms.domain.EntityReviewRoundDiscussionMessage;
import com.kjms.domain.EntityReviewRoundReviewerDiscussionMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EntityReviewRoundReviewerDiscussionMessageRepository extends JpaRepository<EntityReviewRoundReviewerDiscussionMessage, Long> {

    @Query("FROM EntityReviewRoundReviewerDiscussionMessage rrdm WHERE rrdm.reviewRound.id =:reviewRoundId AND rrdm.discussion.id =:discussionId ORDER BY rrdm.createdAt DESC")
    List<EntityReviewRoundReviewerDiscussionMessage> getMessages(@Param("reviewRoundId") Long reviewRoundId, @Param("discussionId") Long discussionId);

}
