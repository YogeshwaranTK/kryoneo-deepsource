package com.kjms.service.errors;

import com.kjms.security.BadRequestAlertException;
import com.kjms.security.BadRequestEntityConstants;

@SuppressWarnings("java:S110") // Inheritance tree of classes should not be too deep
public class UserNotFoundException extends BadRequestAlertException {
    private static final long serialVersionUID = 1L;

    public UserNotFoundException() {
        super("User Not Found", BadRequestEntityConstants.USER, "notFound");
    }

    public UserNotFoundException(String email) {
        super(String.format("User %s Not Found", email), BadRequestEntityConstants.USER, "emailUserNotFound");
    }
}
