package com.kjms.service.errors;

import com.kjms.security.BadRequestAlertException;
import com.kjms.security.BadRequestEntityConstants;

@SuppressWarnings("java:S110") // Inheritance tree of classes should not be too deep
public class InvalidOrgException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public InvalidOrgException() {
        super("Invalid Org", BadRequestEntityConstants.COMMON, "invalidOrg");
    }
}
