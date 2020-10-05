package com.daydreaminger.android.usagecontroller.model;

/**
 * 记录必要的使用详情相关信息
 *
 * @author : daydreaminger
 * @date : 2020/10/5 13:39
 */
public class UsageInfo {
    //===============UsageStats

    //SDK API提供可用的字段
    /**
     * app包名
     */
    public String mPackageName;
    /**
     * 使用时间范围的开始时间
     */
    public long mBeginTimeStamp;
    /**
     * 使用时间范围的结束时间
     */
    public long mEndTimeStamp;
    /**
     * 最后一次使用时长
     */
    public long mLastTimeUsed;
    /**
     * app前台（可见）总使用时间
     */
    public long mTotalTimeInForeground;

    //Android P/Q...新增字段
    /**
     * app的前台服务最后一次可见的时间（通知栏）
     */
    public long mLastTimeForegroundServiceUsed;
    /**
     * app的前台服务的总使用时间
     */
    public long mTotalTimeForegroundServiceUsed;
    /**
     * app最后一次可见的时间
     */
    public long mLastTimeVisible;
    /**
     * app可见的总时间（屏幕上能看到app相关的界面、通知等）
     */
    public long mTotalTimeVisible;

    //需要反射获取的字段
    /**
     * app启动运行次数
     */
    public int mLaunchCount;
    /**
     * 最后一次的事件
     */
    public int mLastEvent;

//    public SparseIntArray mActivities = new SparseIntArray();
//    public ArrayMap<String, Integer> mForegroundServices = new ArrayMap<>();


    @Override
    public String toString() {
        return "\nUsageInfo{" +
                "mPackageName='" + mPackageName + '\'' +
                ", mBeginTimeStamp=" + mBeginTimeStamp +
                ", mEndTimeStamp=" + mEndTimeStamp +
                ", mLastTimeUsed=" + mLastTimeUsed +
                ", mTotalTimeInForeground=" + mTotalTimeInForeground +
                ", mLastTimeForegroundServiceUsed=" + mLastTimeForegroundServiceUsed +
                ", mTotalTimeForegroundServiceUsed=" + mTotalTimeForegroundServiceUsed +
                ", mLastTimeVisible=" + mLastTimeVisible +
                ", mTotalTimeVisible=" + mTotalTimeVisible +
                ", mLaunchCount=" + mLaunchCount +
                ", mLastEvent=" + mLastEvent +
                '}' + "\n";
    }
}
