package com.kjms.domain;

import com.kjms.service.mail.MailToVariable;

import javax.persistence.*;

@Entity
@Table(name = "jm_mail_to")
public class EntityMailTo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)// TODO: 13-Nov-23 @varghesh (unique missing)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "mail_to_variable")// TODO: 13-Nov-23 @varghesh (length missing)
    private MailToVariable mailToVariable;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MailToVariable getMailToVariable() {
        return mailToVariable;
    }

    public void setMailToVariable(MailToVariable mailToVariable) {
        this.mailToVariable = mailToVariable;
    }
}
