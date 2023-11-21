package com.kjms.service.mapper;

import com.kjms.domain.EntityJournalCheckList;
import com.kjms.service.dto.JournalCheckList;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper class responsible for converting {@link EntityJournalCheckList} to {@link JournalCheckList}
 */
@Service
public class JournalCheckListMapper {
    public JournalCheckList entityJournalCheckListToJournalCheckListDto(EntityJournalCheckList entityJournalCheckList) {

        JournalCheckList journalCheckList = new JournalCheckList();

        journalCheckList.setId(entityJournalCheckList.getId());
        journalCheckList.setJournalCheckListItem(entityJournalCheckList.getCheckListItem());
        journalCheckList.setDisplayPosition(entityJournalCheckList.getDisplayPosition());

        return journalCheckList;
    }

    public List<JournalCheckList> entityJournalCheckListToJournalCheckListDto(Collection<EntityJournalCheckList> entityJournalCheckLists) {
        return entityJournalCheckLists.stream().map(this::entityJournalCheckListToJournalCheckListDto)
            .collect(Collectors.toList());
    }
}
