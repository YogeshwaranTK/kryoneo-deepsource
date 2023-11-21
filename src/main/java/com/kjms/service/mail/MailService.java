package com.kjms.service.mail;

import com.kjms.domain.EntityCommonMailAction;
import com.kjms.domain.EntityMailTo;
import com.kjms.repository.EntityMailActionTemplateRepository;
import com.kjms.service.dto.requests.MailTemplateUpdateRequest;
import com.kjms.service.mapper.MailMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.kjms.config.ApplicationConstants.*;

/**
 * Service for sending emails.
 * <p>
 * We use the {@link Async} annotation to send emails asynchronously.
 */
@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    private final Environment env;

    private final EntityMailActionTemplateRepository entityMailActionTemplateRepository;

    private final MailMapper mailMapper;

    public MailService(Environment env,
                       EntityMailActionTemplateRepository entityMailActionTemplateRepository, MailMapper mailMapper) {
        this.env = env;
        this.entityMailActionTemplateRepository = entityMailActionTemplateRepository;
        this.mailMapper = mailMapper;
    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug(
            "Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart,
            isHtml,
            to,
            subject,
            content
        );

        Properties properties = new Properties();
        properties.setProperty("mail.smtp.host", env.getProperty(MAIL_HOST));
        properties.setProperty("mail.smtp.port", env.getProperty(MAIL_PORT));
        properties.setProperty("mail.smtp.auth", env.getProperty(MAIL_AUTH));
        properties.put("mail.smtp.starttls.enable", env.getProperty(MAIL_STARTTLS_ENABLE));
        Session session = Session.getDefaultInstance(properties,
            new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(
                        env.getProperty(MAIL_USERNAME),
                        env.getProperty(MAIL_PASSWORD)
                    );// Specify the Username and the PassWord
                }
            });
        // Prepare a message using a Spring helper
        MimeMessage mimeMessage = new MimeMessage(session);
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(Objects.requireNonNull(env.getProperty(MAIL_USERNAME)));
            message.setSubject(subject);
            message.setText(content, isHtml);
            Transport.send(mimeMessage);
            log.debug("Sent email to User '{}'", to);
        } catch (MailException | MessagingException e) {
            log.warn("Email could not be sent to user '{}'", to, e);
        }
    }


    public List<CommonMailAction> getCommonMailActions() {

        return MailConstants.getCommonMailActions();
    }

    public CommonMailAction getCommonMailActionTemplate(CommonMailActionId actionId) {

        Optional<EntityCommonMailAction> mailActionTemplate = entityMailActionTemplateRepository.getCommonTemplateHTML(actionId);

        EntityCommonMailAction entityCommonMailAction;

        if (mailActionTemplate.isEmpty()) {

            entityCommonMailAction = createBasicTemplate(actionId);

        } else {

            entityCommonMailAction = mailActionTemplate.get();
        }

        return mailMapper.mapEntityCommonMailActionToCommonMailAction(entityCommonMailAction);
    }

    private EntityCommonMailAction createBasicTemplate(CommonMailActionId actionId) {

        EntityCommonMailAction entityCommonMailAction = new EntityCommonMailAction();

        entityCommonMailAction.setId(actionId);

        entityCommonMailAction.setSubject("Subject for actionId->" + actionId.name());

        entityCommonMailAction.setActive(true);

        entityCommonMailAction.setHtml("Body content for actionId->" + actionId.name());

        entityCommonMailAction.setMailTo(new ArrayList<>());

        return entityMailActionTemplateRepository.save(entityCommonMailAction);

    }

    public CommonMailAction updateMailTemplate(MailTemplateUpdateRequest mailTemplateUpdateRequest) {

        Optional<EntityCommonMailAction> entityMailActionTemplate = entityMailActionTemplateRepository.findById(mailTemplateUpdateRequest.getActionId());

        EntityCommonMailAction mailActionTemplate;

        mailActionTemplate = entityMailActionTemplate.orElseGet(EntityCommonMailAction::new);

        if (mailActionTemplate.getId() == null) {

            mailActionTemplate.setId(mailTemplateUpdateRequest.getActionId());
        }

        mailActionTemplate.setHtml(mailTemplateUpdateRequest.getHtml());

        mailActionTemplate.setSubject(mailTemplateUpdateRequest.getSubject());

        mailActionTemplate.setActive(mailTemplateUpdateRequest.getActive());

        List<EntityMailTo> entityMailTos = new ArrayList<>();

        mailTemplateUpdateRequest.getMailToVariables().forEach(mailToVariable -> {

            EntityMailTo mailTo = new EntityMailTo();

            mailTo.setMailToVariable(mailToVariable);

            entityMailTos.add(mailTo);
        });

        mailActionTemplate.setMailTo(entityMailTos);

        entityMailActionTemplateRepository.save(mailActionTemplate);

        CommonMailAction commonMailAction = getCommonMailActionTemplate(mailTemplateUpdateRequest.getActionId());

        commonMailAction.setHtml(mailActionTemplate.getHtml());

        commonMailAction.setSubject(mailActionTemplate.getSubject());

        return commonMailAction;
    }


    public void triggerMail(Long journalId, CommonMailActionId actionId, Map<CommonMailActionVariable, String> variables,
                            Map<MailToVariable, String> toEmails) {
        try {

            Optional<EntityCommonMailAction> mailActionTemplate = entityMailActionTemplateRepository.getCommonTemplateHTML(actionId);

            EntityCommonMailAction entityCommonMailAction = null;

            if (mailActionTemplate.isEmpty()) {

                entityCommonMailAction = createBasicTemplate(actionId);

            } else {

                entityCommonMailAction = mailActionTemplate.get();
            }

            String subject = entityCommonMailAction.getSubject() == null ? "" : entityCommonMailAction.getSubject();

            String content = entityCommonMailAction.getHtml() == null ? "" : entityCommonMailAction.getHtml();


            for (CommonMailActionVariable mailToVariable : variables.keySet()) {
                subject = subject.replace("[[$" + mailToVariable.name() + "$]]", variables.getOrDefault(mailToVariable, ""));
            }

            for (CommonMailActionVariable mailToVariable : variables.keySet()) {
                content = content.replace("[[$" + mailToVariable.name() + "$]]", variables.getOrDefault(mailToVariable, ""));
            }

            for (MailToVariable mailToVariable : toEmails.keySet()) {

                sendEmail(toEmails.get(mailToVariable), subject, content, false, true);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
