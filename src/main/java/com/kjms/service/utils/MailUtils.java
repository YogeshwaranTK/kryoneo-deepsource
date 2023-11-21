package com.kjms.service.utils;

import com.kjms.config.Constants;
import com.kjms.domain.EntityUser;
import com.kjms.service.errors.EmailOtpLimitExceedException;


import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

public interface MailUtils {

    /**
     * OTP creating, according to ENV
     *
     * @return Six digit OTP
     */
    static String createOTP() {

        String otp;
        //for the production random will generate
        int randomNumber = new Random().nextInt(900000) + 100000;

        otp = String.valueOf(randomNumber);

        return otp;
    }

    // Check resend otp time exceed our time.
    static void checkResendEmailOtpTime(EntityUser entityUser) {
        if (entityUser.getOtpExpDt() != null) {
            OffsetDateTime offsetDateTime = entityUser.getOtpExpDt().minus(Constants.EMAIL_OTP_EXPIRY_MINUTES, ChronoUnit.MINUTES);

            long timeDifference = offsetDateTime.until(OffsetDateTime.now(), ChronoUnit.MINUTES);

            if (timeDifference >= 0 && timeDifference < Constants.EMAIL_OTP_RESEND_MINUTES) {

                long remainingMinutes = Constants.EMAIL_OTP_RESEND_MINUTES - timeDifference;

                throw new EmailOtpLimitExceedException(
                    "Please try again after " + remainingMinutes + " minute(s).",
                    "resentOtpTimeReached", remainingMinutes, Constants.MAX_EMAIL_OTP_SEND_LIMIT);
            } else {
                if (entityUser.getOtpCount() != null && entityUser.getOtpCount() > Constants.MAX_EMAIL_OTP_SEND_LIMIT) {
                    if (timeDifference < 1440) {

                        long hours = ((1440 - timeDifference) / 60) < 1 ? 1 : (1440 - timeDifference) / 60; // Calculate the whole number of hours

                        throw new EmailOtpLimitExceedException(
                            "You reached Maximum " + Constants.MAX_EMAIL_OTP_SEND_LIMIT + " limits, Please try after " + hours + " hour(s).",
                            "sentOtpLimitExceeds", hours, Constants.MAX_EMAIL_OTP_SEND_LIMIT
                        );
                    } else {
                        entityUser.setOtpCount(0);
                    }
                }
            }
        }
    }
}
