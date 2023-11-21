package com.kjms.service.dto.requests;

import java.util.Set;

public class CancelPeerReviewRequest extends DeclineRequest {

    private Set<Long> Ids;


    public Set<Long> getIds() {
        return Ids;
    }

    public void setIds(Set<Long> ids) {
        Ids = ids;
    }
}
