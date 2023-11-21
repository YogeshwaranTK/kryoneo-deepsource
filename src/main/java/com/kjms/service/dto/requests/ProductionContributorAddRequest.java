package com.kjms.service.dto.requests;

import java.util.List;

public class ProductionContributorAddRequest {

    private List<String> userIds;
    private String desc;

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
