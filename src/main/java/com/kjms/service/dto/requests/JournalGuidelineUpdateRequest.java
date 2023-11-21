package com.kjms.service.dto.requests;

public class JournalGuidelineUpdateRequest {
    private String guidelines;
    private String copyrightNotice;

    public String getGuidelines() {
        return guidelines;
    }

    public void setGuidelines(String guidelines) {
        this.guidelines = guidelines;
    }

    public String getCopyrightNotice() {
        return copyrightNotice;
    }

    public void setCopyrightNotice(String copyrightNotice) {
        this.copyrightNotice = copyrightNotice;
    }
}
