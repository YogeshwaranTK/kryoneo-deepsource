package com.kjms.service;

import com.kjms.domain.EntityJournalCheckList;
import com.kjms.repository.EntityJournalCheckListRepository;
import com.kjms.repository.EntityJournalRepository;
import com.kjms.security.BadRequestAlertException;
import com.kjms.service.dto.JournalCheckList;
import com.kjms.service.mapper.JournalCheckListMapper;
import com.kjms.service.dto.requests.JournalCheckListCreateRequest;
import com.kjms.service.dto.requests.JournalCheckListUpdateRequest;
import com.kjms.web.rest.journal.JournalCheckListResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class JournalCheckListService {
    private final Logger log = LoggerFactory.getLogger(JournalCheckListResource.class);

    private final EntityJournalCheckListRepository journalCheckListRepository;
    private final EntityJournalRepository journalRepository;
    private final JournalCheckListMapper journalCheckListMapper;

    public JournalCheckListService(EntityJournalCheckListRepository journalCheckListRepository, EntityJournalRepository journalRepository, JournalCheckListMapper journalCheckListMapper) {
        this.journalCheckListRepository = journalCheckListRepository;
        this.journalRepository = journalRepository;
        this.journalCheckListMapper = journalCheckListMapper;
    }

    public JournalCheckList createJournalCheckList(JournalCheckListCreateRequest journalCheckListCreateRequest, Long journalId) {

        EntityJournalCheckList entityJournalCheckList = new EntityJournalCheckList();

        entityJournalCheckList.setCheckListItem(journalCheckListCreateRequest.getJournalCheckListItem());
        entityJournalCheckList.setDeleted(false);
        entityJournalCheckList.setDisplayPosition(journalCheckListCreateRequest.getDisplayPosition()==null?0:journalCheckListCreateRequest.getDisplayPosition());

        journalRepository.findOneByIdAndIsDeletedIsFalse(journalId)
            .ifPresent(entityJournalCheckList::setJournal);

        EntityJournalCheckList createdEntityJournalCheckList = journalCheckListRepository.save(entityJournalCheckList);

        log.debug("Created Information for Journal Check List: {}", createdEntityJournalCheckList);

        return journalCheckListMapper.entityJournalCheckListToJournalCheckListDto(createdEntityJournalCheckList);
    }

    public JournalCheckList updateJournalCheckList(JournalCheckListUpdateRequest journalCheckListUpdateRequest) {

        EntityJournalCheckList entityJournalCheckList = journalCheckListRepository
            .findByIdAndDeletedFalse(journalCheckListUpdateRequest.getId())
            .orElseThrow(() -> new BadRequestAlertException("Journal Check List Not Found", "journalCheckList", "notFound"));

        entityJournalCheckList.setCheckListItem(journalCheckListUpdateRequest.getJournalCheckListItem());
        entityJournalCheckList.setDisplayPosition(journalCheckListUpdateRequest.getDisplayPosition() == null ? 0 : journalCheckListUpdateRequest.getDisplayPosition());

        EntityJournalCheckList updatedEntityJournalCheckList = journalCheckListRepository.save(entityJournalCheckList);

        log.debug("Updated Information for Journal Check List: {}", updatedEntityJournalCheckList);

        return journalCheckListMapper.entityJournalCheckListToJournalCheckListDto(updatedEntityJournalCheckList);
    }

    public void deleteJournalCheckList(Long id) {
        journalCheckListRepository.findByIdAndDeletedFalse(id)
            .ifPresent(entityJournalCheckList -> {
                entityJournalCheckList.setDeleted(true);
                log.debug("Deleted Information for Journal Check List: {}", id);
            });
    }

    @Transactional(readOnly = true)
    public List<JournalCheckList> getJournalCheckList(Long journalId) {

        return journalCheckListMapper.entityJournalCheckListToJournalCheckListDto(journalCheckListRepository.findAllByJournal(journalId));
    }

}
