package com.kjms.repository;

import com.kjms.domain.EntityReviewRoundContributor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EntityReviewRoundContributorRepository extends JpaRepository<EntityReviewRoundContributor, Long> {

    @Query("FROM EntityReviewRoundContributor rrc WHERE rrc.user.id =:userId AND rrc.reviewRound.id =:reviewRoundId AND rrc.isDeleted =false")
    Optional<EntityReviewRoundContributor> findContributorUser(@Param("reviewRoundId") Long reviewRoundId, @Param("userId") String userId);

    @Query("FROM EntityReviewRoundContributor rrc WHERE rrc.reviewRound.id =:reviewRoundId AND rrc.isDeleted =false")
    List<EntityReviewRoundContributor> getContributors(@Param("reviewRoundId") Long reviewRoundId);

    @Modifying
    @Query("UPDATE EntityReviewRoundContributor rrc SET rrc.isDeleted = true WHERE  rrc.user.id =:contributorId AND rrc.reviewRound.id=:reviewRoundId")
    void removeContributor(@Param("contributorId")String contributorId, @Param("reviewRoundId") Long reviewRoundId);
}
