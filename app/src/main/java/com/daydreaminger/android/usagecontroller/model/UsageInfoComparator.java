package com.daydreaminger.android.usagecontroller.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author : daydreaminger
 * @date : 2020/10/11 16:56
 */
public class UsageInfoComparator {
    private static final String TAG = "UsageInfoCompartor";

    /**
     * 根据总使用时长排序
     */
    public static void sortByUseTime(List<UsageInfo> list) {
        Collections.sort(list, new Comparator<UsageInfo>() {
            @Override
            public int compare(UsageInfo o1, UsageInfo o2) {
                return (int) (o2.mTotalTimeInForeground - o1.mTotalTimeInForeground);
            }
        });
    }
}
