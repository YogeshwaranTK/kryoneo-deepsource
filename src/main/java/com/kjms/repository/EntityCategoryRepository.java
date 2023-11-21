package com.kjms.repository;

import com.kjms.domain.EntityCategory;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link EntityCategory} entity.
 */
@Repository
public interface EntityCategoryRepository extends JpaRepository<EntityCategory, String> {

    @Query(
        "FROM EntityCategory category WHERE "
            + "  ( (LOWER(category.id) LIKE LOWER(CONCAT('%',:searchText, '%'))) " +
            "OR (LOWER(category.name) LIKE LOWER(CONCAT('%',:searchText, '%')))) "
    )
    List<EntityCategory> findAllByIdContainingIgnoreCaseAndNameContainingIgnoreCase(@Param("searchText") String searchText);


    Set<EntityCategory> findAllByIdIn(List<String> ids);
}
