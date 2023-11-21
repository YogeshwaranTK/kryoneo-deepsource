package com.kjms.repository;

import com.kjms.domain.EntityRole;
import com.kjms.domain.EntityRolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;


/**
 * Spring Data JPA repository for the {@link EntityRolePermission} entity.
 */
@Repository
public interface EntityRolePermissionRepository extends JpaRepository<EntityRolePermission, Long> {
    List<EntityRolePermission> findAllByIsDeletedIsFalseAndRole(EntityRole orgRole);

    @Query("SELECT DISTINCT rolePermission.permission FROM EntityRolePermission rolePermission WHERE rolePermission.isDeleted=false AND rolePermission.role In ?1")
    List<String> findAllDistinctPermissionByIsDeletedIsFalseAndRoleIn(Set<EntityRole> orgRole);

    Optional<EntityRolePermission> findOneByRoleAndPermissionAndIsDeletedIsFalse(
        EntityRole entityRole, String permission
    );
}
