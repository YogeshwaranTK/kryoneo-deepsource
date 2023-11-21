package com.kjms.web.rest.journal;


import com.kjms.config.Constants;
import com.kjms.service.JournalEditorialRole;
import com.kjms.service.JournalRoleService;
import com.kjms.service.dto.JournalRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/journal-role/")
public class JournalRoleResource {

    private final JournalRoleService journalRoleService;

    private final Logger log = LoggerFactory.getLogger(JournalRoleResource.class);

    public JournalRoleResource(JournalRoleService journalRoleService) {
        this.journalRoleService = journalRoleService;
    }

    @GetMapping("/")
    public ResponseEntity<JournalRole> getAccessLevel(@RequestHeader(name = Constants.JOURNAL_ID) Long journalId){

        log.debug("REST request get access level of the journal");

        return ResponseEntity.ok().body(journalRoleService.getJournalAccessLevel(journalId));
    }

    @GetMapping("/roles")
    public ResponseEntity<List<JournalEditorialRole>> getRoles(@RequestHeader(name = Constants.JOURNAL_ID) Long journalId){

        log.debug("REST request get roles of the journal");

        return ResponseEntity.ok().body(journalRoleService.getJournalRoles(journalId));
    }

}
