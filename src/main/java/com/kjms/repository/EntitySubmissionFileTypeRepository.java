package com.kjms.repository;

import com.kjms.domain.EntitySubmissionFileType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EntitySubmissionFileTypeRepository extends JpaRepository<EntitySubmissionFileType, Long> {
    Optional<EntitySubmissionFileType> findAllByIsDeletedIsFalseAndId(Long id);

    Page<EntitySubmissionFileType> findAllByIsDeletedIsFalseAndNameContaining(String name, Pageable pageable);

    Page<EntitySubmissionFileType> findAllByIsDeletedIsFalse(Pageable pageable);
}
