package com.kjms.repository;

import com.kjms.domain.EntityNotification;
import com.kjms.domain.EntityUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;


/**
 * Spring Data JPA repository for the {@link EntityNotification} entity.
 */
@Repository
public interface EntityNotificationRepository extends JpaRepository<EntityNotification, Long> {

    Page<EntityNotification> findAllByIsReadIsFalseAndNotifyTo(EntityUser entityUser, Pageable pageable);

    Set<EntityNotification> findAllByIsReadIsFalseAndNotifyToAndIdIn(EntityUser entityUser, Set<Long> ids);
}
