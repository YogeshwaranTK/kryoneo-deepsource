package com.kjms.security;

/**
 * Constants for a entityName in  {@link BadRequestAlertException}.
 */
public final class BadRequestEntityConstants {

    private BadRequestEntityConstants(){}

    public static final String COMMON = "common";
    public static final String JOURNAL = "journal";
    public static final String ORGANIZATION = "organization";
    public static final String JOURNAL_GROUP = "journalGroup";
    public static final String USER = "user";
    public static final String JOURNAL_USER = "journalUser";
    public static final String JOURNAL_USER_GROUP = "journalUserGroup";
    public static final String SUBMISSION_ARTICLE = "submissionArticle";
    public static final String PEER_REVIEW = "peerReview";
    public static final String ROLE = "role";
}
