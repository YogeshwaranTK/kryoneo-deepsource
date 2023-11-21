package com.kjms.repository;

import com.kjms.domain.EntityCountry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link EntityCountry} entity.
 */
@Repository
public interface EntityCountryRepository extends JpaRepository<EntityCountry, Long> {
    @Query(
        "FROM EntityCountry country WHERE " +
        "LOWER(country.name) LIKE LOWER(CONCAT('%',:searchText, '%')) OR " +
        "LOWER(country.niceName) LIKE LOWER(CONCAT('%',:searchText, '%')) OR " +
        "LOWER(country.iso3) LIKE LOWER(CONCAT('%',:searchText, '%')) OR " +
        "LOWER(country.phoneCode) LIKE LOWER(CONCAT('%',:searchText, '%')) OR " +
        "LOWER(country.numberCode) LIKE LOWER(CONCAT('%',:searchText, '%')) OR " +
        "LOWER(country.iso) LIKE LOWER(CONCAT('%',:searchText, '%')) "
    )
    Page<EntityCountry> findBySearchWithPaginate(@Param("searchText") String searchText, Pageable pagingElements);
}
