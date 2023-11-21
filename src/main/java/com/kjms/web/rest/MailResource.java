package com.kjms.web.rest;

import com.kjms.service.mail.CommonMailAction;
import com.kjms.service.mail.CommonMailActionId;
import com.kjms.service.mail.MailService;
import com.kjms.service.dto.requests.MailTemplateUpdateRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing the Notification.
 */
@Tag(name = "Mail Resource")
@RestController
@RequestMapping("/api/v1/mail/common/")
public class MailResource {
    private final Logger log = LoggerFactory.getLogger(MailResource.class);
    private final MailService mailService;
    public MailResource(MailService mailService) {

        this.mailService = mailService;
    }

    @GetMapping("/action")
    public ResponseEntity<CommonMailAction> getAction(
        @RequestParam(name = "actionId") CommonMailActionId actionId) {

        log.debug("REST request to get all Variables: {}", actionId);

        return new ResponseEntity<>(mailService.getCommonMailActionTemplate(
            actionId
        ), HttpStatus.OK);
    }

    @GetMapping("/actions")
    public ResponseEntity<List<CommonMailAction>> getActions() {

        log.debug("REST request to get all Actions");

        return new ResponseEntity<>(mailService.getCommonMailActions(), HttpStatus.OK);
    }

    @PostMapping("/action")
    public ResponseEntity<CommonMailAction> updateMailAction(@RequestBody MailTemplateUpdateRequest mailTemplateUpdateRequest) {

        log.debug("REST request to get all Actions");

        return new ResponseEntity<>(mailService.updateMailTemplate(mailTemplateUpdateRequest), HttpStatus.OK);
    }
}
