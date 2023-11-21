package com.kjms.repository;

import com.kjms.domain.EntityReviewerFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EntityReviewerFileRepository extends JpaRepository<EntityReviewerFile, Long> {

    @Query("FROM EntityReviewerFile rf WHERE rf.reviewRoundReviewer.id=:reviewRoundReviewerId")
    List<EntityReviewerFile> getFiles(@Param("reviewRoundReviewerId") Long reviewRoundReviewerId);
}
