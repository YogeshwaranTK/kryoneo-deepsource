package com.kjms.service.utils;

import com.kjms.config.Constants;
import com.kjms.web.rest.errors.ErrorConstants;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.util.Locale;

@Service
public class WordUtils {
    private static MessageSource messageSource = null;

    public WordUtils(MessageSource messageSource) {
        WordUtils.messageSource = messageSource;
    }

    public static String makeFirstLetterUpper(String word) {

        if (StringUtils.hasText(word)) {
            try {
                String trimmedWord = StringUtils.trimWhitespace(word);

                return Character.toUpperCase(trimmedWord.charAt(0)) + trimmedWord.substring(1);
            } catch (StringIndexOutOfBoundsException e) {
                return null;
            }
        } else {
            return null;
        }
    }

    public static String formatMessage(final String titleKey, final String commaSeparatedValues, Locale locale) {
        try {
            final String title = messageSource.getMessage(titleKey, null, locale);

            final String[] values = commaSeparatedValues.split(Constants.MESSAGE_SOURCE_VALUE_SEPARATE_OPERATOR);

            return format(title, values);
        } catch (NoSuchMessageException e) {
            return "";
        }
    }

    // Custom string format method
    public static String format(final String pattern, final String[] values) {
        StringBuilder formatted = new StringBuilder();

        for (int i = 0; i < pattern.length(); i++) {
            if (pattern.charAt(i) == '{' && i + 1 < pattern.length() && pattern.charAt(i + 1) >= '0' && pattern.charAt(i + 1) <= '9') {
                int placeholderIndex = pattern.charAt(i + 1) - '0';
                if (placeholderIndex < values.length) {
                    formatted.append(values[placeholderIndex]);
                    i += 2;
                    continue;
                }
            }

            formatted.append(pattern.charAt(i));
        }

        return formatted.toString();
    }

    /**
     * Use regular expression to insert hyphens before uppercase letters
     * Example : peerReview -> peer-review
     *
     * @param input the string to convert
     * @return lower case with hypen string
     */
    public static String convertToLowerHyphen(String input) {
        if (input == null) {
            return "";
        }

        String hyphenated = input.replaceAll("([a-z])([A-Z])", "$1-$2");
        return hyphenated.toLowerCase();
    }

    public static URI createResponseQueryParam(String entity, String type) {
        return URI.create(format(ErrorConstants.PROBLEM_BASE_URL + "?entity={0}&type={1}", new String[]{convertToLowerHyphen(entity), type}));
    }
}
