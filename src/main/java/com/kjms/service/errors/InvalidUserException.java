package com.kjms.service.errors;

import com.kjms.security.BadRequestAlertException;
import com.kjms.security.BadRequestEntityConstants;

@SuppressWarnings("java:S110") // Inheritance tree of classes should not be too deep
public class InvalidUserException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public InvalidUserException() {
        super("Invalid User", BadRequestEntityConstants.COMMON, "invalidUser");
    }
}
