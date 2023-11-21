package com.kjms.web.rest.errors;

import java.net.URI;

public final class ErrorConstants {

    public static final String PASSWORD_REGEX_ERROR_MESSAGE = "Password should be 8 to 12 Characters with One Uppercase,One Lowercase ,One Special Character & One Numeric.";

    public static final String ERR_CONCURRENCY_FAILURE = "error.concurrencyFailure";
    public static final String ERR_VALIDATION = "error.validation";
    public static final String PROBLEM_BASE_URL = "https://www.uat.kryoneo.com/problem";
    public static final URI DEFAULT_TYPE = URI.create(PROBLEM_BASE_URL + "/problem-with-message");
    public static final URI EMAIL_lOTP_LIMIT_EXCEED_TYPE = URI.create(PROBLEM_BASE_URL + "/invalid-email-send-otp");
    public static final URI FINAL_ARTICLE_SUBMISSION_TYPE = URI.create(PROBLEM_BASE_URL + "/final-article-submission");
    public static final URI CONSTRAINT_VIOLATION_TYPE = URI.create(PROBLEM_BASE_URL + "/constraint-violation");
    public static final URI INVALID_PASSWORD_TYPE = URI.create(PROBLEM_BASE_URL + "/invalid-password");
    public static final URI EMAIL_ALREADY_USED_TYPE = URI.create(PROBLEM_BASE_URL + "/email-already-used");
    public static final URI LOGIN_ALREADY_USED_TYPE = URI.create(PROBLEM_BASE_URL + "/login-already-used");
    public static final URI USER_EMAIL_NOT_VERIFIED_TYPE = URI.create(PROBLEM_BASE_URL + "/email-not-verfied");
    ;
    public static final URI USER_PASSWORD_EXPIRED_TYPE = URI.create(PROBLEM_BASE_URL + "/password-expired");
    ;

    private ErrorConstants() {
    }
}
