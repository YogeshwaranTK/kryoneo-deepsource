package com.kjms.repository;

import com.kjms.domain.*;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link EntitySubmissionCategory} entity.
 */
@Repository
public interface EntitySubmissionCategoryRepository extends JpaRepository<EntitySubmissionCategory, Long> {

    List<EntitySubmissionCategory> findAllByIsDeletedIsFalseAndJournalAndSubmission(
        EntityJournal entityJournal, EntitySubmission submissionArticle
    );
}
