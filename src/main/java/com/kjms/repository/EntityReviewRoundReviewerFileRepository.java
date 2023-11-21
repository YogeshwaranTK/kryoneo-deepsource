package com.kjms.repository;

import com.kjms.domain.EntityReviewRoundReviewerFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EntityReviewRoundReviewerFileRepository extends JpaRepository<EntityReviewRoundReviewerFile, Long> {

    @Query("FROM EntityReviewRoundReviewerFile rrrf WHERE rrrf.reviewRoundReviewer.id=:reviewRoundReviewerId")
    List<EntityReviewRoundReviewerFile> getFiles(@Param("reviewRoundReviewerId") Long reviewRoundReviewerId);
}
