package com.kjms.repository;

import com.kjms.domain.EntityFileFormat;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link EntityFileFormat} entity.
 */
@Repository
public interface EntityFileFormatRepository extends JpaRepository<EntityFileFormat, Long> {
    Page<EntityFileFormat> findAllByIsDeletedIsFalse(Pageable pageable);

    Page<EntityFileFormat> findAllByIsDeletedIsFalseAndNameContainingIgnoreCase(String name, Pageable pageable);

    Optional<EntityFileFormat> findOneByIdAndIsDeletedIsFalse(Long id);
    Set<EntityFileFormat> findAllByIdInAndIsDeletedIsFalse(List<Long> id);
}
