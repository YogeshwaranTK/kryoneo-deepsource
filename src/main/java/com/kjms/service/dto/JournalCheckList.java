package com.kjms.service.dto;

import com.kjms.domain.EntityJournalCheckList;

import java.io.Serializable;

/**
 * A DTO representing a {@link EntityJournalCheckList} country.
 */
public class JournalCheckList implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String journalCheckListItem;
    private Integer displayPosition;

    public Integer getDisplayPosition() {
        return displayPosition;
    }

    public void setDisplayPosition(Integer displayPosition) {
        this.displayPosition = displayPosition;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJournalCheckListItem() {
        return journalCheckListItem;
    }

    public void setJournalCheckListItem(String journalCheckListItem) {
        this.journalCheckListItem = journalCheckListItem;
    }
}
