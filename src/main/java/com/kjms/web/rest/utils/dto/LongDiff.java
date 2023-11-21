package com.kjms.web.rest.utils.dto;

import java.util.List;

public class LongDiff {

    private List<Long> added;
    private List<Long> removed;

    public LongDiff(List<Long> added, List<Long> removed) {
        this.added = added;
        this.removed = removed;
    }

    public List<Long> getAdded() {
        return added;
    }

    public void setAdded(List<Long> added) {
        this.added = added;
    }

    public List<Long> getRemoved() {
        return removed;
    }

    public void setRemoved(List<Long> removed) {
        this.removed = removed;
    }
}
