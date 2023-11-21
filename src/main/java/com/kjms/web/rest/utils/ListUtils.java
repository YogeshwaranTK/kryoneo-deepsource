package com.kjms.web.rest.utils;

import com.kjms.web.rest.utils.dto.LongDiff;
import com.kjms.web.rest.utils.dto.StringDiff;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {

    public static LongDiff findLongDifference(List<Long> oldList, List<Long> newList) {


        List<Long> added = new ArrayList<>(newList);
        added.removeAll(oldList);

        List<Long> removed = new ArrayList<>(oldList);
        removed.removeAll(newList);

        return new LongDiff(added, removed);
    }

    public static StringDiff findStringDifference(List<String> oldList, List<String> newList) {

        List<String> added = new ArrayList<>(newList);
        added.removeAll(oldList);

        List<String> removed = new ArrayList<>(oldList);
        removed.removeAll(newList);

        return new StringDiff(added, removed);
    }
}
