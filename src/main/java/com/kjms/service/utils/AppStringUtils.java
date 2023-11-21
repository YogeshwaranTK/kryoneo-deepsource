package com.kjms.service.utils;

import java.util.ArrayList;
import java.util.List;

public class AppStringUtils {
    public static List<Long> convertAsLongList(String commaSeparatedString) {

        if (commaSeparatedString == null) {
            return new ArrayList<>();
        }

        String[] stringArray = commaSeparatedString.split(",");

        // Step 2 and 3: Convert and store in a List<Long>
        List<Long> longList = new ArrayList<>();
        for (String element : stringArray) {
            longList.add(Long.parseLong(element));
        }

        return longList;
    }
}
