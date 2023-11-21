package com.kjms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduler Class for do all scheduler works.
 */
@Component
public class SchedulerService {
    private final Logger log = LoggerFactory.getLogger(SchedulerService.class);

    /**
     * Scheduler Method for check Response Or Review date missed for Submission Revision.
     * The method runs every hour.
     * todo : Change the status for Missed Submission Revision Review Due Date And Response Due Date.
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void detectMissedRevisionResponseOrReviewDueDate() {
        log.debug("Scheduler Enabling to Check Missed Submission Revision Review Due Date And Response Due Date");
    }

    /**
     * Scheduler Method for delete temporary files
     * The method runs once in month.
     * todo : Delete the temporary files.
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    public void deleteTempFiles() {
        log.debug("Scheduler Enabling to clear temporary files");
    }
}
