package com.kjms.service.dto.requests;


import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Represents a request object for set read for current notification.
 */
@Schema(title = "Notification Read", description = "Request Body for Set Read to Notification")
public class NotificationUpdateReadRequest {

    @Schema(required = true)
    @NotNull
    @NotEmpty
    private Set<Long> notificationIds;

    public Set<Long> getNotificationIds() {
        return notificationIds;
    }

    public void setNotificationIds(Set<Long> notificationIds) {
        this.notificationIds = notificationIds;
    }
}
