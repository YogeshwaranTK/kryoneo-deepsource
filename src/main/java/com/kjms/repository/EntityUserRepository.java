package com.kjms.repository;

import com.kjms.domain.EntityUser;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the {@link EntityUser} entity.
 */
@Repository
public interface EntityUserRepository extends JpaRepository<EntityUser, String> {

    Optional<EntityUser> findOneByEmailIgnoreCaseAndIsDeletedIsFalse(String email);

    Optional<EntityUser> findOneWithAuthoritiesByEmailIgnoreCaseAndIsDeletedIsFalse(String email);

    Page<EntityUser> findAllByIdNotNullAndActivatedIsTrue(Pageable pageable);

    Optional<EntityUser> findOneByIdAndIsDeletedIsFalse(String id);

    Page<EntityUser> findAllByIsDeletedIsFalse(Pageable pageable);

    @Query(
        "FROM EntityUser user WHERE user.isDeleted=false "
            + " AND ((LOWER(user.fullName) LIKE LOWER(CONCAT('%',:searchText, '%'))) OR (LOWER(user.email) LIKE LOWER(CONCAT('%',:searchText, '%'))))"
    )
    Page<EntityUser> findAllByIsDeletedIsFalseAndUserNameAndEmailContaining(
        @Param("searchText") String searchText, Pageable pageable);

    @Query(
        "FROM EntityUser user WHERE user.isDeleted=false AND user.id NOT IN :ids"
            + " AND ((LOWER(user.fullName) LIKE LOWER(CONCAT('%',:searchText, '%'))) OR (LOWER(user.email) LIKE LOWER(CONCAT('%',:searchText, '%'))))"
    )
    Page<EntityUser> findAllByIdNotInIsDeletedIsFalseAndUserNameAndEmailContaining(
        @Param("ids") List<String> ids, @Param("searchText") String searchText, Pageable pageable);

}
