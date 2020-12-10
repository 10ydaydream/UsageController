package com.daydreaminger.android.usagecontroller.model;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.AppUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 时间使用统计结果
 *
 * @author : daydreaminger
 * @date : 2020/11/8 22:22
 */
public class UsageData {
    public String packageName = "";
    public AppUtils.AppInfo appInfo = null;
    public List<UsagePeriod> usePeriods = new ArrayList<>();

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof UsageData) {
            return ((UsageData) obj).packageName.equals(this.packageName);
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "UsageData{" +
                "packageName='" + packageName + '\'' +
                ", appInfo=" + appInfo +
                ", usePeriods=" + usePeriods +
                '}';
    }

    public static class UsagePeriod {
        public long startTime;
        public long endTime;
        public long usagePeriod;

        @Override
        public String toString() {
            return "UsagePeriod{" +
                    "startTime=" + startTime +
                    ", endTime=" + endTime +
                    ", usagePeriod=" + usagePeriod +
                    '}';
        }
    }
}
