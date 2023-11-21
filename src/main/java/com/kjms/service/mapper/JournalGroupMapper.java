package com.kjms.service.mapper;

import com.kjms.config.Constants;
import com.kjms.domain.EntityJournalGroup;
import com.kjms.service.dto.JournalGroup;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper class responsible for converting {@link EntityJournalGroup} to {@link JournalGroup}
 */
@Service
public class JournalGroupMapper {

    public JournalGroup entityJournalGroupToJournalGroup(EntityJournalGroup entityJournalGroup) {
        JournalGroup journalGroup = new JournalGroup();

        journalGroup.setId(entityJournalGroup.getId());
        journalGroup.setName(entityJournalGroup.getName());
        journalGroup.setCreatedDate(entityJournalGroup.getCreatedAt());
        journalGroup.setLastModifiedDate(entityJournalGroup.getLastModifiedAt());
        journalGroup.setCreatedByName(getCreatedByName(entityJournalGroup));
        journalGroup.setLastModifiedByName(
            entityJournalGroup.getLastModifiedBy() != null ? entityJournalGroup.getLastModifiedBy().getFullName() : null
        );
        journalGroup.setDefault(
            (entityJournalGroup.getDefault() != null) ? entityJournalGroup.getDefault() : false
        );

        return journalGroup;
    }

    public Page<JournalGroup> entityJournalGroupsToJournalGroupList(Page<EntityJournalGroup> entityJournalGroups) {
        return entityJournalGroups.map(this::entityJournalGroupToJournalGroup);
    }

    public List<JournalGroup> entityJournalGroupsToJournalGroupList(List<EntityJournalGroup> entityJournalGroups) {
        return entityJournalGroups.stream().map(this::entityJournalGroupToJournalGroup)
            .collect(Collectors.toList());
    }

    private String getCreatedByName(EntityJournalGroup entityJournalGroup) {

        if (entityJournalGroup.getDefault() != null && entityJournalGroup.getDefault()) {
            return Constants.SYSTEM;
        } else {
            return entityJournalGroup.getCreatedBy() != null ? entityJournalGroup.getCreatedBy().getFullName() : null;
        }
    }
}
