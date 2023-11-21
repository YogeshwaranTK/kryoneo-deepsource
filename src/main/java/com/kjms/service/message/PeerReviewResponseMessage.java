package com.kjms.service.message;

import com.kjms.config.EntityNameConstant;
import com.kjms.service.utils.WordUtils;

import java.net.URI;

/**
 * Enum for Peer Review Response
 */
public enum PeerReviewResponseMessage implements ResponseMessage {
    PEER_REVIEW_NOT_FOUND("peerReviewNotFound", "Peer Review Not Found", "not-found"),
    ALREADY_ACCEPTED("peerReviewAlreadyAccepted", "Peer Review Already Accepted", "already-accepted"),
    ALREADY_REJECTED("peerReviewAlreadyRejected", "Peer Review Already Rejected", "already-rejected"),
    ALREADY_CANCELLED("peerReviewAlreadyCancelled", "Peer Review Already Cancelled", "already-cancelled"),
    ALREADY_SUBMITTED("peerReviewAlreadySubmitted", "Peer Review Already Submitted", "already-submitted"),
    ACTION_FAILED("peerReviewActionFailed", "Peer Review Action Failed", "action-failed");

    private final String key;
    private final String defaultMessage;
    private final String type;


    PeerReviewResponseMessage(String key, String defaultMessage, String type) {
        this.key = key;
        this.defaultMessage = defaultMessage;
        this.type = type;
    }

    public String getEntityName() {
        return EntityNameConstant.PEER_REVIEW;
    }

    public URI getType() {
        return WordUtils.createResponseQueryParam(EntityNameConstant.PEER_REVIEW, type);
    }

    public String getKey() {
        return key;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}
