package com.kjms.web.rest.journal;

import com.kjms.config.Constants;
import com.kjms.service.JournalCheckListService;
import com.kjms.service.dto.JournalCheckList;
import com.kjms.service.dto.requests.JournalCheckListCreateRequest;
import com.kjms.service.dto.requests.JournalCheckListUpdateRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import tech.jhipster.web.util.HeaderUtil;


import javax.validation.Valid;
import java.util.List;

/**
 * REST controller for managing the Journal Check List.
 */
@Tag(name = "Journal Check List")
@RestController
@RequestMapping("/api/v1/journal/journal-checklist")
public class JournalCheckListResource {
    private final Logger log = LoggerFactory.getLogger(JournalCheckListResource.class);

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JournalCheckListService journalCheckListService;

    public JournalCheckListResource(JournalCheckListService journalCheckListService) {
        this.journalCheckListService = journalCheckListService;
    }


    @PostMapping
    public ResponseEntity<JournalCheckList> createJournalCheckList(@Valid @RequestBody JournalCheckListCreateRequest journalCheckListCreateRequest,
                                                                   @RequestHeader(name = Constants.JOURNAL_ID) Long journalId) {
        log.debug("REST request to save Journal Check List: {}", journalCheckListCreateRequest);

        JournalCheckList journalCheckList = journalCheckListService.createJournalCheckList(journalCheckListCreateRequest, journalId);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, "journalCheckList.created", ""))
            .body(journalCheckList);
    }

    @PutMapping
    public ResponseEntity<JournalCheckList> updateJournalCheckList(@Valid @RequestBody JournalCheckListUpdateRequest journalCheckListUpdateRequest) {
        log.debug("REST request to update Journal Check List: {}", journalCheckListUpdateRequest);

        JournalCheckList journalCheckLists = journalCheckListService.updateJournalCheckList(journalCheckListUpdateRequest);

        return ResponseEntity.ok()
            .headers(HeaderUtil.createAlert(applicationName, "journalCheckList.updated", ""))
            .body(journalCheckLists);
    }

    @GetMapping
    public ResponseEntity<List<JournalCheckList>> getJournalCheckList(@RequestHeader(name = Constants.JOURNAL_ID) Long journalId) {
        log.debug("REST request to get Journal Check List");

        return ResponseEntity.ok(journalCheckListService.getJournalCheckList(journalId));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteJournalCheckList(@RequestParam(name = Constants.ID) Long id) {

        log.debug("REST request to Delete Journal Check List: {}", id);

        journalCheckListService.deleteJournalCheckList(id);

        return ResponseEntity.ok().build();
    }

}
