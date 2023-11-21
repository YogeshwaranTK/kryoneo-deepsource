package com.kjms.service.errors;

import com.kjms.security.BadRequestAlertException;

@SuppressWarnings("java:S110") // Inheritance tree of classes should not be too deep
public class GroupNotFoundException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public GroupNotFoundException() {
        super("Group Not Found", "group", "notFound");
    }
}
