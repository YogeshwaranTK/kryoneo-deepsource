package com.kjms.service.dto.requests;


public enum PeerReviewRequestType {
    OPEN("OPEN"),
    SINGLE_BLIND("SINGLE_BLIND"),
    DOUBLE_BLIND("DOUBLE_BLIND");

    private final String ReviewTypeName;

    PeerReviewRequestType(String name) {
        this.ReviewTypeName = name;
    }

    public String getReviewTypeName() {
        return ReviewTypeName;
    }
}
