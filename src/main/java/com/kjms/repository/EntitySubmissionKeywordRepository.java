package com.kjms.repository;

import com.kjms.domain.EntitySubmission;
import com.kjms.domain.EntitySubmissionKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EntitySubmissionKeywordRepository extends JpaRepository<EntitySubmissionKeyword, Long> {


    @Query("SELECT sk.keyword FROM EntitySubmissionKeyword sk WHERE sk.submission.id=:submissionId AND sk.journal.id=:journalId AND sk.isDeleted is false")
    List<String> getSubmissionKeyWords(@Param("submissionId") Long submissionId, @Param("journalId") Long journalId);

    @Modifying
    @Query("UPDATE EntitySubmissionKeyword sk SET sk.isDeleted = true WHERE sk.submission.id =:submissionId AND sk.journal.id=:journalId")
    void removeSubmissionKeyWords(@Param("submissionId") Long submissionId, @Param("journalId") Long journalId);
}
