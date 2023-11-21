package com.kjms.service.dto;


import java.util.Set;

public class UserJournalGroup{
    private static final long serialVersionUID = 1L;
    private String journalTitle;

    private Set<String> journalGroupNames;

    public Set<String> getJournalGroupNames() {
        return journalGroupNames;
    }

    public void setJournalGroupNames(Set<String> journalGroupNames) {
        this.journalGroupNames = journalGroupNames;
    }

    public String getJournalTitle() {
        return journalTitle;
    }

    public void setJournalTitle(String journalTitle) {
        this.journalTitle = journalTitle;
    }
}
