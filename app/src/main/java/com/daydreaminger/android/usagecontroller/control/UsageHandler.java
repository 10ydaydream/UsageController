package com.daydreaminger.android.usagecontroller.control;

import android.annotation.SuppressLint;
import android.app.usage.ConfigurationStats;
import android.app.usage.EventStats;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.daydreaminger.android.usagecontroller.AppHolder;
import com.daydreaminger.android.usagecontroller.model.UsageInfo;

import java.util.List;
import java.util.Map;

/**
 * 封装UsageStatsManager相关操作
 * <p></p>
 * UsageStatsManager相关API说明：
 * 1. {@link UsageStatsManager#queryUsageStats(int, long, long)}返回一个UsageStats的List列表，列表中同一个包名（APP）可能会有多个UsageStats实例。
 * 2. {@link UsageStatsManager#queryAndAggregateUsageStats(long, long)}返回一个Map类型数据，通过将时间段内同一个包名（APP）的合并起来，最终返回应用的使用总情况。
 *
 * @author : daydreaminger
 * @date : 2020/10/1 19:21
 */
public class UsageHandler {
    private static final String TAG = "UsageHandler";

    private static UsageHandler instance;

    public static UsageHandler getInstance() {
        if (instance == null) {
            instance = new UsageHandler();
        }
        return instance;
    }

    /**
     * 获取UsageStatsManager实例
     */
    private static UsageStatsManager getUsageStatsManager() {
        return (UsageStatsManager) AppHolder.getAppContext().getSystemService(Context.USAGE_STATS_SERVICE);
    }

    private UsageStatsManager mUsageStatsManager;

    private UsageHandler() {
        mUsageStatsManager = getUsageStatsManager();
    }

    //wrapper UsageStatsManager public method.======================================================

    /**
     * 查询时间范围内的使用情况数据，返回结果的列表中，同一个APP可能会有多个UsageStats对象记录app的使用情况。
     *
     * @param intervalType 时间间隔类型{@link UsageStatsManager#INTERVAL_BEST}，之中的一个
     * @param period       时间范围，查询的时间区间为当前时间到当前时间往前period之间
     * @return 返回使用情况列表
     */
    public List<UsageStats> queryUsageStats(int intervalType, long period) {
        long now = System.currentTimeMillis();
        return queryUsageStats(
                intervalType,
                now - period,
                now);
    }

    /**
     * 查询时间范围内的使用情况数据，返回结果的列表中，同一个APP可能会有多个UsageStats对象记录app的使用情况。
     *
     * @param intervalType 时间间隔类型{@link UsageStatsManager#INTERVAL_BEST}，之中的一个。
     * @param start        开始时间
     * @param end          结束时间
     * @return 返回使用情况列表
     */
    public List<UsageStats> queryUsageStats(int intervalType, long start, long end) {
        return mUsageStatsManager.queryUsageStats(
                intervalType,
                start,
                end);
    }

    /**
     * 查询时间范围内的使用情况合并后的数据。
     *
     * @param period 查询的时间范围
     * @return 返回Map类型数据，将每个APP的UsageStats使用数据合并成同一个
     */
    public Map<String, UsageStats> queryAndAggregateUsageStats(long period) {
        long now = System.currentTimeMillis();
        return mUsageStatsManager.queryAndAggregateUsageStats(now - period, now);
    }

    public List<ConfigurationStats> queryConfigurations(int intervalType, long period) {
        long now = System.currentTimeMillis();
        return queryConfigurations(
                intervalType,
                now - period,
                now
        );
    }

    public List<ConfigurationStats> queryConfigurations(int intervalType, long start, long end) {
        return mUsageStatsManager.queryConfigurations(intervalType, start, end);
    }

    public UsageEvents queryEvent(long period) {
        long now = System.currentTimeMillis();
        return queryEvent(
                now - period,
                now);
    }

    public UsageEvents queryEvent(long start, long end) {
        return mUsageStatsManager.queryEvents(start, end);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public UsageEvents queryEventsForSelf(long period) {
        long now = System.currentTimeMillis();
        return queryEventsForSelf(
                now - period,
                now);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public UsageEvents queryEventsForSelf(long start, long end) {
        return mUsageStatsManager.queryEventsForSelf(start, end);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public List<EventStats> queryEventStats(int intervalType, long period) {
        long now = System.currentTimeMillis();
        return queryEventStats(
                intervalType,
                now - period,
                now);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public List<EventStats> queryEventStats(int intervalType, long start, long end) {
        return mUsageStatsManager.queryEventStats(intervalType, start, end);
    }

    /**
     * 获取当前APP的stand bucket状态
     *
     * @return APP的stand bucket状态，无法获取或其他问题则返回-1
     */
    @RequiresApi(api = Build.VERSION_CODES.P)
    @SuppressLint("WrongConstant")
    public int getAppStandByBucket() {
        int standByBucket = -1;
        standByBucket = mUsageStatsManager.getAppStandbyBucket();
        return standByBucket;
    }

    /**
     * 查询特定包名的App是否处于活动状态（所谓的活动状态就是指在系统设置的时间间隔内，App有直接——和App的Activity交互，或间接使用——通知栏交互）
     */
    public static boolean appActive(String appPkg) {
        return getUsageStatsManager().isAppInactive(appPkg);
    }

    //wrapper UsageStatsManager public method.======================================================

    //handle system model field.======================================================

    /**
     * 提取UsageStats的通用字段
     */
    public static void coverPublicField(UsageInfo info, UsageStats usageStats) {
        info.mPackageName = usageStats.getPackageName();
        info.mBeginTimeStamp = usageStats.getFirstTimeStamp();
        info.mEndTimeStamp = usageStats.getLastTimeStamp();
        info.mLastTimeUsed = usageStats.getLastTimeUsed();
        info.mTotalTimeInForeground = usageStats.getTotalTimeInForeground();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public static void coverAndroidQField(UsageInfo info, UsageStats usageStats) {
        info.mLastTimeForegroundServiceUsed = usageStats.getLastTimeForegroundServiceUsed();
        info.mTotalTimeForegroundServiceUsed = usageStats.getTotalTimeForegroundServiceUsed();
        info.mLastTimeVisible = usageStats.getLastTimeVisible();
        info.mTotalTimeVisible = usageStats.getTotalTimeVisible();
    }

    //handle system model field.======================================================


}
