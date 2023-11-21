package com.kjms.service.mapper;

import com.kjms.domain.EntityCommonMailAction;
import com.kjms.domain.EntityMailTo;
import com.kjms.service.mail.CommonMailAction;
import com.kjms.service.mail.MailConstants;
import com.kjms.service.mail.MailToVariable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.kjms.service.mail.MailConstants.getCommonMailActions;

@Service
public class MailMapper {
    public CommonMailAction mapEntityCommonMailActionToCommonMailAction(EntityCommonMailAction entityCommonMailAction) {

        CommonMailAction mailAction = new CommonMailAction();

        mailAction.setId(entityCommonMailAction.getId());

        mailAction.setHtml(entityCommonMailAction.getHtml());

        mailAction.setSubject(entityCommonMailAction.getSubject());

        mailAction.setName(getCommonMailActions().stream().filter(action -> action.getId() == entityCommonMailAction.getId()).collect(Collectors.toList()).get(0).getName());

        mailAction.setMailToVariables(MailConstants.getCommonMailActions()
            .stream()
            .filter(action -> action.getId() == entityCommonMailAction.getId())
            .flatMap(action -> action.getMailToVariables().stream())
            .collect(Collectors.toList()));

        mailAction.setSelectedMailToVariables(mapEntityMailToVariableToMailToVariable(entityCommonMailAction.getMailTo() == null ? new ArrayList<>() : entityCommonMailAction.getMailTo()));

        mailAction.setCommonMailActionVariables(MailConstants.getCommonMailActions()
            .stream()
            .filter(action -> action.getId() == entityCommonMailAction.getId())
            .flatMap(action -> action.getCommonMailActionVariables().stream())
            .collect(Collectors.toList()));

        return mailAction;
    }


    private List<MailToVariable> mapEntityMailToVariableToMailToVariable(List<EntityMailTo> mailTos) {

        List<MailToVariable> mailToVariables = new ArrayList<>();

        mailTos.forEach(entityMailTo -> mailToVariables.add(entityMailTo.getMailToVariable()));

        return mailToVariables;
    }
}
