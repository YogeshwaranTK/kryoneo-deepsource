package com.kjms.service.dto.requests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class JournalCheckListCreateRequest {
    @NotNull
    @NotBlank
    private String journalCheckListItem;

    private Integer displayPosition;

    public Integer getDisplayPosition() {
        return displayPosition;
    }

    public void setDisplayPosition(Integer displayPosition) {
        this.displayPosition = displayPosition;
    }

    public String getJournalCheckListItem() {
        return journalCheckListItem;
    }

    public void setJournalCheckListItem(String journalCheckListItem) {
        this.journalCheckListItem = journalCheckListItem;
    }
}
