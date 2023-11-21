package com.kjms.service.errors;

import com.kjms.security.BadRequestAlertException;
import com.kjms.security.BadRequestEntityConstants;

@SuppressWarnings("java:S110") // The Inheritance tree of classes should not be too deep
public class JournalUserNotFoundException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public JournalUserNotFoundException() {
        super("Journal User Not Found", BadRequestEntityConstants.JOURNAL_USER, "notFound");
    }
}
