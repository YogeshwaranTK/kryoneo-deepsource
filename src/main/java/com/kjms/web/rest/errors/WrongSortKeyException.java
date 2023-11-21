package com.kjms.web.rest.errors;

import com.kjms.security.BadRequestAlertException;
import com.kjms.security.BadRequestEntityConstants;

@SuppressWarnings("java:S110") // Inheritance tree of classes should not be too deep
public class WrongSortKeyException extends BadRequestAlertException {

    private static final long serialVersionUID = 1L;

    public WrongSortKeyException() {
        super("Wrong Sort Key", BadRequestEntityConstants.COMMON, "sortKeyNotAllowed");
    }


}
