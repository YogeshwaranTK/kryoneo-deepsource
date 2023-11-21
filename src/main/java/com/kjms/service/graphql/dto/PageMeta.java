package com.kjms.service.graphql.dto;

public class PageMeta {

    private Long totalRecord;

    public PageMeta(Long totalRecord) {
        this.totalRecord = totalRecord;
    }

    public Long getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(Long totalRecord) {
        this.totalRecord = totalRecord;
    }
}
