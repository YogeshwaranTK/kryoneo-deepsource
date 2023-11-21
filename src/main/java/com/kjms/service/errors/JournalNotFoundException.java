package com.kjms.service.errors;

import com.kjms.security.BadRequestAlertException;
import com.kjms.security.BadRequestEntityConstants;

@SuppressWarnings("java:S110") // The Inheritance tree of classes should not be too deep
public class JournalNotFoundException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public JournalNotFoundException() {
        super("Journal Not Found", BadRequestEntityConstants.JOURNAL, "notFound");
    }
}
