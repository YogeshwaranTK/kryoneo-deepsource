package com.kjms.service.graphql.dto;


import com.kjms.service.dto.Journal;

import java.util.List;

public class JournalPageableResponse {

    private PageMeta meta;

    private List<Journal> data;


    public PageMeta getMeta() {
        return meta;
    }

    public void setMeta(PageMeta meta) {
        this.meta = meta;
    }

    public List<Journal> getData() {
        return data;
    }

    public void setData(List<Journal> data) {
        this.data = data;
    }
}
