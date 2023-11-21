package com.kjms.service.dto.requests;

import javax.validation.constraints.NotNull;
import java.util.List;

public class UsersRequest {

    @NotNull
    private List<String> userIds;

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }
}
