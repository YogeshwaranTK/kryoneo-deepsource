package com.kjms.domain;

import com.kjms.service.mail.CommonMailActionId;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "jm_common_mail_template")
public class EntityCommonMailAction {
    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "id")
    private CommonMailActionId id;
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
    @Column(name = "subject")
    private String subject;
    @Column(name = "html")
    private String html;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "mail_to", foreignKey = @ForeignKey(name = "fk_jm_mail_template_mail_to"))
    private List<EntityMailTo> mailTo;

    public CommonMailActionId getId() {
        return id;
    }

    public void setId(CommonMailActionId id) {
        this.id = id;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public List<EntityMailTo> getMailTo() {
        return mailTo;
    }

    public void setMailTo(List<EntityMailTo> mailTo) {
        this.mailTo = mailTo;
    }
}
