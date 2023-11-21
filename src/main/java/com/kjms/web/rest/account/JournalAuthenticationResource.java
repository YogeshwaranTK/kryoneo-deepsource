package com.kjms.web.rest.account;

import com.kjms.config.Constants;
import com.kjms.service.JournalAccessService;
import com.kjms.service.dto.JournalAccess;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Role - Journal Authentication")
@RestController
@RequestMapping("/api/v1/journal/access")
public class JournalAuthenticationResource {

    private final JournalAccessService journalAccessService;


    public JournalAuthenticationResource(JournalAccessService journalAccessService) {
        this.journalAccessService = journalAccessService;
    }


    @GetMapping("")
    public ResponseEntity<JournalAccess> getAccess(
        @RequestHeader(name = Constants.JOURNAL_ID) Long journalId) {

        return new ResponseEntity<>(journalAccessService.getAccess(journalId), HttpStatus.OK);
    }

}
