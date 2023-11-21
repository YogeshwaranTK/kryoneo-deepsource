package com.kjms.service.mapper;

import com.kjms.service.utils.WordUtils;
import org.ocpsoft.prettytime.PrettyTime;
import com.kjms.domain.EntityNotification;
import com.kjms.service.dto.Notification;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Mapper class responsible for converting {@link EntityNotification} to {@link Notification}
 */
@Service
public class NotificationMapper {

    public Notification entityNotificationToNotification(EntityNotification entityNotification, Locale locale) {
        Notification notification = new Notification();

        String title = WordUtils.formatMessage(entityNotification.getTitleKey(), entityNotification.getTitleValues(), locale);
        String desc = WordUtils.formatMessage(entityNotification.getDescKey(), entityNotification.getDescValues(), locale);

        PrettyTime prettyTime = new PrettyTime(locale);

        String formattedCreatedDate = prettyTime.format(Date.from(entityNotification.getCreatedAt().toInstant()));

        notification.setTitle(title);
        notification.setDesc(desc);
        notification.setId(entityNotification.getId());
        notification.setCreatedDate(entityNotification.getCreatedAt());
        notification.setFormattedCreatedDate(formattedCreatedDate);

        return notification;
    }

    public Page<Notification> entityNotificationsToNotifications(Page<EntityNotification> entityNotifications, Locale locale) {
        return entityNotifications
            .map(entityNotification -> entityNotificationToNotification(entityNotification, locale));
    }
}
