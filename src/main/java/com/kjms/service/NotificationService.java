package com.kjms.service;

import com.kjms.config.Constants;
import com.kjms.domain.*;
import com.kjms.repository.EntityNotificationRepository;
import com.kjms.repository.EntityUserRepository;
import com.kjms.service.dto.Notification;
import com.kjms.service.mapper.NotificationMapper;
import com.kjms.service.dto.requests.NotificationUpdateReadRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.OffsetDateTime;
import java.util.*;

@Service
@Transactional
public class NotificationService {
    private final Logger log = LoggerFactory.getLogger(NotificationService.class);
    private final EntityNotificationRepository entityNotificationRepository;
    private final UserService userService;
    private final NotificationMapper notificationMapper;
    private final EntityUserRepository userRepository;

    public NotificationService(EntityNotificationRepository entityNotificationRepository, UserService userService, NotificationMapper notificationMapper, EntityUserRepository userRepository) {
        this.entityNotificationRepository = entityNotificationRepository;
        this.userService = userService;
        this.notificationMapper = notificationMapper;
        this.userRepository = userRepository;
    }

    @Async
    void storeNotification(final String titleKey, final String descKey, final String titleValue, final String descValue, final String userId) {

        userRepository.findOneByIdAndIsDeletedIsFalse(userId)
            .ifPresent(notifyTo -> {
                EntityNotification entityNotification = new EntityNotification();

                entityNotification.setTitleKey(titleKey);
                entityNotification.setDescKey(descKey);
                entityNotification.setTitleValues(titleValue);
                entityNotification.setDescValues(descValue);
                entityNotification.setCreatedBy(Constants.SYSTEM);
                entityNotification.setCreatedAt(OffsetDateTime.now());
                entityNotification.setNotifyTo(notifyTo);
                entityNotification.setRead(false);

                entityNotificationRepository.save(entityNotification);

                log.debug("Notification Added to User : {} for title :{}", entityNotification.getNotifyTo().getFullName(), titleKey);

            });
    }

    /**
     * Compose a notification for Permission having users in a journal.
     */
    @Async
    public void composeSubmissionFlowNotification(final Set<String> userIds,
                                                  final String titleKey, final String titleValue, final String descKey, final String descValue) {
        userIds.forEach(userId -> storeNotification(titleKey, descKey, titleValue, descValue, userId));
    }

    /**
     * Compose a notification for specific user.
     */
    @Async
    public void composeSubmissionFlowNotification(String titleKey, String titleValue, String descKey, String descValue, EntityUser user) {
        storeNotification(titleKey, descKey, titleValue, descValue, user.getId());
    }

    /**
     * Get All Unread Notifications for the Current User.
     */
    public Page<Notification> getNotifications(Pageable pageable) {
        EntityUser currentUser = userService.getCurrentUser();

        Page<EntityNotification> entityNotifications = entityNotificationRepository.findAllByIsReadIsFalseAndNotifyTo(currentUser, pageable);

        return notificationMapper.entityNotificationsToNotifications(entityNotifications, Locale.forLanguageTag(currentUser.getLangKey()));
    }

    /**
     * Set Read for users' notification.
     *
     * @param notificationUpdateReadRequest the ids to set read.
     */
    public Set<Long> updateRead(final NotificationUpdateReadRequest notificationUpdateReadRequest) {
        final EntityUser currentUser = userService.getCurrentUser();

        Set<Long> updatedIds = new HashSet<>();

        entityNotificationRepository.findAllByIsReadIsFalseAndNotifyToAndIdIn(currentUser, notificationUpdateReadRequest.getNotificationIds())
            .forEach(entityNotification -> {
                updatedIds.add(entityNotification.getId());
                entityNotification.setRead(true);
            });

        log.debug("Notifications Ids set as Read: {}", notificationUpdateReadRequest.getNotificationIds());

        return updatedIds;
    }
}
