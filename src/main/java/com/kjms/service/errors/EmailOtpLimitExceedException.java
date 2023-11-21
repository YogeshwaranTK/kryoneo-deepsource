package com.kjms.service.errors;

public class EmailOtpLimitExceedException extends RuntimeException {
    private final String defaultMessage;
    private final String errorKey;
    private final long remainingTime;
    private final int totalLimits;

    public EmailOtpLimitExceedException(String defaultMessage, String errorKey, long remainingTime, int totalLimits) {
        super(defaultMessage);
        this.defaultMessage = defaultMessage;
        this.errorKey = errorKey;
        this.remainingTime = remainingTime;
        this.totalLimits = totalLimits;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public String getErrorKey() {
        return errorKey;
    }

    public long getRemainingTime() {
        return remainingTime;
    }

    public int getTotalLimits() {
        return totalLimits;
    }
}
