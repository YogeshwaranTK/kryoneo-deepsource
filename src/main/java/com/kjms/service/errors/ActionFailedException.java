package com.kjms.service.errors;

import com.kjms.security.BadRequestAlertException;

import java.net.URI;

public class ActionFailedException extends BadRequestAlertException {
    public ActionFailedException(URI type, String defaultMessage, String entityName, String errorKey) {
        super(type, defaultMessage, entityName, errorKey);
    }
    public ActionFailedException() {
        super(null, null, null, null);

    }
}
