package com.kjms.service.errors;

import com.kjms.security.BadRequestAlertException;
import com.kjms.security.BadRequestEntityConstants;
import com.kjms.service.message.PeerReviewResponseMessage;

public class PeerReviewNotFoundException extends BadRequestAlertException {
    private static final long serialVersionUID = 1L;

    public PeerReviewNotFoundException() {
        super(PeerReviewResponseMessage.PEER_REVIEW_NOT_FOUND.getDefaultMessage(), BadRequestEntityConstants.PEER_REVIEW, PeerReviewResponseMessage.PEER_REVIEW_NOT_FOUND.getKey());
    }
}
