package com.kjms.web.rest;

import com.kjms.security.SecurityUtils;
import com.kjms.service.NotificationService;
import com.kjms.service.dto.Notification;
import com.kjms.service.dto.requests.NotificationUpdateReadRequest;
import com.kjms.web.rest.errors.WrongSortKeyException;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * REST controller for managing the Notification.
 */
@Tag(name = "Notification")
@RestController
@RequestMapping("/api/v1/notification")
public class NotificationResource {

    private final Logger log = LoggerFactory.getLogger(NotificationResource.class);

    private final NotificationService notificationService;

    public NotificationResource(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    private static final List<String> ALLOWED_ORDERED_PROPERTIES = List.of("id");

    private boolean onlyContainsAllowedProperties(Pageable pageable) {
        return !pageable.getSort().stream().map(Sort.Order::getProperty).allMatch(ALLOWED_ORDERED_PROPERTIES::contains);
    }

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    /**
     * {@code GET api/v1/notification/update-read} : Get All Unread Notifications for current user.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)}
     */
    @GetMapping(path = "/list")
    public ResponseEntity<List<Notification>> getListJournal(@ParameterObject Pageable pageable) {
        log.debug("REST request to get all Notifications for User Id: {}", SecurityUtils.getCurrentUserId());

        if (onlyContainsAllowedProperties(pageable)) {
            throw new WrongSortKeyException();
        }

        final Page<Notification> notifications = notificationService.getNotifications(pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), notifications);

        return new ResponseEntity<>(notifications.getContent(), headers, HttpStatus.OK);
    }

    /**
     * {@code PUT api/v1/notification/update-read} : Update the notification as read.
     *
     * @param notificationUpdateReadRequest the notification to set read.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)}
     */
    @PutMapping(path = "/update-read")
    public ResponseEntity<Set<Long>> updatePublishingPortal(@Valid @RequestBody NotificationUpdateReadRequest notificationUpdateReadRequest) {
        log.debug("REST request to update Notifications as Read: {}", notificationUpdateReadRequest.getNotificationIds());

        Set<Long> updatedIds = notificationService.updateRead(notificationUpdateReadRequest);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, "notification.readUpdated", ""))
            .body(updatedIds);
    }
}
