package com.kjms.service.message;

import com.kjms.config.EntityNameConstant;
import com.kjms.service.utils.WordUtils;

import java.net.URI;

public enum SubmissionResponseMessage implements ResponseMessage {
    SUBMISSION_NOT_FOUND("submissionNotFound", "Submission Not Found", "not-found");

    private final String key;
    private final String defaultMessage;
    private final String type;

    SubmissionResponseMessage(String key, String defaultMessage, String type) {
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
        return EntityNameConstant.SUBMISSION;
    }

    public URI getType() {
        return WordUtils.createResponseQueryParam(EntityNameConstant.SUBMISSION, type);
    }

}
