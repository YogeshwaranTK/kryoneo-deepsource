package com.kjms.config;

import com.kjms.service.AccessValidatorService;


import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

public class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private AccessValidatorService accessValidatorService;

    private Object filterObject;
    private Object returnObject;
    private Object target;

    public CustomMethodSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }

    public boolean hasEndPointAccess(String endPointId) {
        return getAccessValidatorService().validate(endPointId);
    }

    public AccessValidatorService getAccessValidatorService() {
        return accessValidatorService;
    }

    public void setAccessValidatorService(AccessValidatorService accessValidatorService) {
        this.accessValidatorService = accessValidatorService;
    }

    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getFilterObject() {
        return filterObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getReturnObject() {
        return returnObject;
    }

    @Override
    public Object getThis() {
        return target;
    }
}
