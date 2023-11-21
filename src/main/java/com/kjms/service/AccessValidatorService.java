package com.kjms.service;

import com.kjms.config.Constants;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


@Service
@Transactional(readOnly = true)
public class AccessValidatorService {

    private final Logger log = LoggerFactory.getLogger(AccessValidatorService.class);


    public boolean validate(String endPointId) {

        log.debug("REST request Id: {}", endPointId);

        return true;
    }

    /**
     * Check Permission exist in a particular journal & it returns true or false
     */
    private boolean checkJournalPermission(String permission) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        if (request.getHeader(Constants.JOURNAL_ID) == null) {
            return false;
        }

        long journalId;

        try {
            journalId = Long.parseLong(request.getHeader(Constants.JOURNAL_ID));
        } catch (NumberFormatException e) {
            return false;
        }

        return true;

//        return SecurityUtils.getJournalPermissions().get(journalId) != null && SecurityUtils.getJournalPermissions().get(journalId).contains(permission);
    }
}
