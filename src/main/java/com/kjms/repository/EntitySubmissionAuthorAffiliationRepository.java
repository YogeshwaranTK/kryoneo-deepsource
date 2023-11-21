package com.kjms.repository;

import com.kjms.domain.EntitySubmissionAuthor;
import com.kjms.domain.EntitySubmissionAuthorAffiliation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EntitySubmissionAuthorAffiliationRepository extends JpaRepository<EntitySubmissionAuthorAffiliation, Long> {
    Optional<EntitySubmissionAuthorAffiliation> findFirstByIsDeletedIsFalseAndAffiliationAndSubmissionArticleAuthorOrderByIdDesc(String affiliation, EntitySubmissionAuthor submissionArticleAuthor);
    List<EntitySubmissionAuthorAffiliation> findAllByIsDeletedIsFalseAndSubmissionArticleAuthorIn(List<EntitySubmissionAuthor> submissionAuthors);
}
