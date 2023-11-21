package com.kjms.config;

import java.util.List;

/**
 * Application constants.
 */
public final class Constants {

    public static final String DEFAULT_LANGUAGE = "en";
    public static final String SUPER_ADMIN_USER_ID = "0351b16a-5a92-4fc3-87c3-7d373c3de041";
    public static final String SYSTEM = "System";
    public static final String ANONYMOUS = "Anonymous";
    public static final String PASSWORD_REGEX = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,12}$";
    public static final String OTP_REGEX = "\\b\\d{6}\\b";
    public static final String JOURNAL_KEY_REGEX = "^[a-zA-Z]{1,5}$";
    public static final String JOURNAL_ID = "journalId";
    public static final String SUBMISSION_ARTICLE_ID = "submissionArticleId";
    public static final String SEARCH_TEXT = "searchText";
    public static final String DELETED_REMARKS = "deletedRemarks";
    public static final int EMAIL_OTP_EXPIRY_MINUTES = 5;
    public static final int EMAIL_OTP_RESEND_MINUTES = 3;
    public static final int MAX_EMAIL_OTP_SEND_LIMIT = 10;
    public static final String MESSAGE_SOURCE_VALUE_SEPARATE_OPERATOR = ";";
    public final static String FORBIDDEN = "Forbidden";
    public final static String PEER_REVIEWED_FILE_PREFIX = "Peer_Reviewed_";
    public final static List<String> EXCEL_FILE_EXTENSIONS = List.of("xlsx", "xls", "xlsm", "xlt", "xltx", "csv", "tsv", "dif", "sylk");
    public static final String ID = "id";

    private Constants() {
    }
}
