package com.kjms.service.message;

import com.kjms.config.EntityNameConstant;
import com.kjms.service.utils.WordUtils;

import java.net.URI;

public enum RevisionResponseMessage implements ResponseMessage {
    REVISION_NOT_FOUND("Revision Not Found", "revisionNotFound", "not-found"),
    TECHNICAL_CHECK_ALREADY_SENT("Revision Not Found", "revisionNotFound", "not-found"),
    PEER_REVISION_ALREADY_SENT("Revision Not Found", "revisionNotFound", "not-found"),
    ACTION_FAILED("Revision Action Failed", "revisionActionFailed", "action-failed");
    private final String key;
    private final String defaultMessage;
    private final String type;

    RevisionResponseMessage(String key, String defaultMessage, String type) {
        this.key = key;
        this.defaultMessage = defaultMessage;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public String getEntityName() {
        return EntityNameConstant.TECHNICAL_CHECK;
    }

    public URI getType() {
        return WordUtils.createResponseQueryParam(EntityNameConstant.TECHNICAL_CHECK, type);
    }
}
