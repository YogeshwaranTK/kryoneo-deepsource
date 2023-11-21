package com.kjms.service.dto.requests;

import com.kjms.service.mail.CommonMailActionId;
import com.kjms.service.mail.MailToVariable;

import java.util.List;

public class MailTemplateUpdateRequest {
    private CommonMailActionId actionId;
    private String html;
    private String subject;
    private Boolean isActive;
    private List<MailToVariable> mailToVariables;

    public CommonMailActionId getActionId() {
        return actionId;
    }

    public void setActionId(CommonMailActionId actionId) {
        this.actionId = actionId;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public List<MailToVariable> getMailToVariables() {
        return mailToVariables;
    }

    public void setMailToVariables(List<MailToVariable> mailToVariables) {
        this.mailToVariables = mailToVariables;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
