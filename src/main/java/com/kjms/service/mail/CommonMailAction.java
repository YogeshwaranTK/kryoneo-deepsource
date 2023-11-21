package com.kjms.service.mail;

import java.io.Serializable;
import java.util.List;

public class CommonMailAction implements Serializable {

    private static final long serialVersionUID = 1L;
    private CommonMailActionId id;
    private String name;
    private String html;
    private String subject;

    private List<CommonMailActionVariable> commonMailActionVariables;

    private List<MailToVariable> mailToVariables;

    private List<MailToVariable> selectedMailToVariables;

    public CommonMailActionId getId() {
        return id;
    }

    public void setId(CommonMailActionId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<CommonMailActionVariable> getCommonMailActionVariables() {
        return commonMailActionVariables;
    }

    public void setCommonMailActionVariables(List<CommonMailActionVariable> commonMailActionVariables) {
        this.commonMailActionVariables = commonMailActionVariables;
    }

    public List<MailToVariable> getMailToVariables() {
        return mailToVariables;
    }

    public void setMailToVariables(List<MailToVariable> mailToVariables) {
        this.mailToVariables = mailToVariables;
    }

    public List<MailToVariable> getSelectedMailToVariables() {
        return selectedMailToVariables;
    }

    public void setSelectedMailToVariables(List<MailToVariable> selectedMailToVariables) {
        this.selectedMailToVariables = selectedMailToVariables;
    }
}
