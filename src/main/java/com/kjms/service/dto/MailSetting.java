package com.kjms.service.dto;

public class MailSetting {

    private String host;
    private String port;
    private String senderEmail;
    private String senderPassword;
    private String authEnabled;
    private String startTls;

    public MailSetting() {
    }

    public MailSetting(String host, String port, String senderEmail, String senderPassword, String authEnabled, String startTls) {
        this.host = host;
        this.port = port;
        this.senderEmail = senderEmail;
        this.senderPassword = senderPassword;
        this.authEnabled = authEnabled;
        this.startTls = startTls;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getSenderPassword() {
        return senderPassword;
    }

    public void setSenderPassword(String senderPassword) {
        this.senderPassword = senderPassword;
    }

    public String getAuthEnabled() {
        return authEnabled;
    }

    public void setAuthEnabled(String authEnabled) {
        this.authEnabled = authEnabled;
    }

    public String getStartTls() {
        return startTls;
    }

    public void setStartTls(String startTls) {
        this.startTls = startTls;
    }

    @Override
    public String toString() {
        return "MailSetting{" +
            "host='" + host + '\'' +
            ", port='" + port + '\'' +
            ", senderEmail='" + senderEmail + '\'' +
            ", senderPassword='" + senderPassword + '\'' +
            ", authEnabled='" + authEnabled + '\'' +
            ", startTls='" + startTls + '\'' +
            '}';
    }
}
