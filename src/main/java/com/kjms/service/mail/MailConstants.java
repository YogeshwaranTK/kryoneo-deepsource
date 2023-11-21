package com.kjms.service.mail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MailConstants {

//    public static List<CommonMailAction> getJournalMailActions() {
//
//        List<CommonMailAction> commonMailActions = new ArrayList<>();
//
//        CommonMailAction commonMailAction = new CommonMailAction();
//
//        commonMailAction.setId(CommonMailActionId.SUBMISSION);
//
//        commonMailAction.setName("When a author successfully submitted a paper.");
//
//        List<CommonMailActionVariable> commonMailActionVariables = new ArrayList<>();
//
//        commonMailActionVariables.add(CommonMailActionVariable.AUTHOR_ID);
//
//        commonMailActionVariables.add(CommonMailActionVariable.JOURNAL_MANAGER_ID);
//
//        commonMailAction.setMailActionVariables(commonMailActionVariables);
//
//        List<MailToVariable> mailToVariables = new ArrayList<>();
//
//        mailToVariables.add(MailToVariable.AUTHOR);
//
//        mailToVariables.add(MailToVariable.JOURNAL_MANAGER);
//
//        commonMailAction.setMailToVariables(mailToVariables);
//
//        commonMailActions.add(commonMailAction);
//
//        return commonMailActions;
//    }

    public static List<CommonMailAction> getCommonMailActions() {

        List<CommonMailAction> commonMailActions = new ArrayList<>();

        commonMailActions.add(getForgotPasswordMailAction());

        return commonMailActions;
    }

    private static CommonMailAction getForgotPasswordMailAction() {

        CommonMailAction commonMailAction = new CommonMailAction();

        commonMailAction.setId(CommonMailActionId.USER_LOGIN_FORGOT_PASSWORD);

        commonMailAction.setName("When user send request to generate forgot password.");

        commonMailAction.setCommonMailActionVariables(List.of(CommonMailActionVariable.OTP,
            CommonMailActionVariable.USER_FULL_NAME));

        commonMailAction.setMailToVariables(Collections.singletonList(MailToVariable.USER_EMAIL));

        return commonMailAction;

    }
}
