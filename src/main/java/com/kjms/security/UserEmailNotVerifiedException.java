package com.kjms.security;

import org.springframework.security.authentication.InternalAuthenticationServiceException;

/**
 * This exception is thrown in case of a not email verified user trying to authenticate.
 */
public class UserEmailNotVerifiedException extends InternalAuthenticationServiceException {
    private static final long serialVersionUID = 1L;

    public UserEmailNotVerifiedException(String email) {
        super(email);
    }
}
