package com.kjms.repository;

import com.kjms.domain.EntityCopyEditingContributor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EntityCopyEditingContributorRepository extends JpaRepository<EntityCopyEditingContributor, Long> {
    @Query("FROM EntityCopyEditingContributor pc WHERE pc.user.id =:userId AND pc.submission.id =:submissionId AND pc.submission.journal.id =:journalId AND pc.isDeleted =false")
    Optional<EntityCopyEditingContributor> findContributorUser(@Param("journalId") Long journalId, @Param("submissionId") Long submissionId,
                                                              @Param("userId") String userId);
    @Query("FROM EntityCopyEditingContributor pc WHERE  pc.submission.id =:submissionId AND pc.submission.journal.id =:journalId AND pc.isDeleted =false")
    List<EntityCopyEditingContributor> getContributors(@Param("journalId") Long journalId, @Param("submissionId") Long submissionId);


    @Modifying
    @Query("UPDATE EntityCopyEditingContributor pc SET pc.isDeleted = true WHERE pc.submission.id =:submissionId AND pc.user.id =:contributorId")
    void removeContributor( @Param("submissionId") Long submissionId, @Param("contributorId") String contributorId);

}
