package com.kjms.repository;

import com.kjms.domain.EntityLanguage;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link EntityLanguage} entity.
 */
@Repository
public interface EntityLanguageRepository extends JpaRepository<EntityLanguage, String> {
    Page<EntityLanguage> findAllByNameContaining(String searchText, Pageable pageable);
}
