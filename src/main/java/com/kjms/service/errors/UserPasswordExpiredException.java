package com.kjms.service.errors;

import org.springframework.security.authentication.InternalAuthenticationServiceException;

public class UserPasswordExpiredException extends InternalAuthenticationServiceException {
    public UserPasswordExpiredException(String email){
        super(email);
    }
}
