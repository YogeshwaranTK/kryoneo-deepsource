package com.kjms.service.errors;

import com.kjms.security.BadRequestAlertException;
import com.kjms.security.BadRequestEntityConstants;
import com.kjms.service.message.SubmissionResponseMessage;

@SuppressWarnings("java:S110") // The Inheritance tree of classes should not be too deep
public class SubmissionNotFoundException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public SubmissionNotFoundException() {
        super(SubmissionResponseMessage.SUBMISSION_NOT_FOUND.getType(), SubmissionResponseMessage.SUBMISSION_NOT_FOUND.getDefaultMessage(), BadRequestEntityConstants.SUBMISSION_ARTICLE, SubmissionResponseMessage.SUBMISSION_NOT_FOUND.getKey());
    }
}
