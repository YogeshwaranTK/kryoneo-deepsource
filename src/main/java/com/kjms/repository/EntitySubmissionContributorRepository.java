package com.kjms.repository;

import com.kjms.domain.EntitySubmissionContributor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EntitySubmissionContributorRepository extends JpaRepository<EntitySubmissionContributor, Long> {
    @Query("FROM EntitySubmissionContributor sc WHERE sc.user.id =:userId AND sc.submission.id =:submissionId AND sc.submission.journal.id =:journalId AND sc.isDeleted =false")
    Optional<EntitySubmissionContributor> findContributorUser(@Param("journalId") Long journalId, @Param("submissionId") Long submissionId,
                                                              @Param("userId") String userId);

    @Query("FROM EntitySubmissionContributor sc WHERE  sc.submission.id =:submissionId AND sc.submission.journal.id =:journalId AND sc.isDeleted =false")
    List<EntitySubmissionContributor> getContributors(@Param("journalId") Long journalId, @Param("submissionId") Long submissionId);

    @Modifying
    @Query("UPDATE EntitySubmissionContributor sc SET sc.isDeleted = true WHERE sc.submission.id =:submissionId AND sc.user.id =:contributorId")
    void removeContributor(@Param("submissionId") Long submissionId, @Param("contributorId") String contributorId);
}
