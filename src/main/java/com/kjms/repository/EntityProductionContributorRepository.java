package com.kjms.repository;

import com.kjms.domain.EntityProductionContributor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EntityProductionContributorRepository extends JpaRepository<EntityProductionContributor, Long> {


    @Query("FROM EntityProductionContributor pc WHERE pc.user.id =:userId AND pc.submission.id =:submissionId AND pc.submission.journal.id =:journalId AND pc.isDeleted =false")
    Optional<EntityProductionContributor> findContributorUser(@Param("journalId") Long journalId, @Param("submissionId") Long submissionId,
                                                              @Param("userId") String userId);

    @Query("FROM EntityProductionContributor pc WHERE  pc.submission.id =:submissionId AND pc.submission.journal.id =:journalId AND pc.isDeleted =false")
    List<EntityProductionContributor> getContributors(@Param("journalId") Long journalId, @Param("submissionId") Long submissionId);

    @Modifying
    @Query("UPDATE EntityProductionContributor pc SET pc.isDeleted = true WHERE pc.submission.id =:submissionId AND pc.user.id =:contributorId")
    void removeContributor(@Param("submissionId") Long submissionId, @Param("contributorId") String contributorId);

}
