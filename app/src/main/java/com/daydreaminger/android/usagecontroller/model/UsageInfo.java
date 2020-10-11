package com.daydreaminger.android.usagecontroller.model;

import android.util.ArrayMap;
import android.util.SparseIntArray;

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
     * 从命名上看是表示app启动运行次数，但是实际结果不是很准确（开发过程中覆盖安装重新启动后，值会增加2）
     */
    public int mLaunchCount;
    /**
     * 最后一次的事件
     */
    public int mLastEvent;
    /**
     * 保存前台Service的class名和最新的状态值
     */
    public SparseIntArray mActivities = new SparseIntArray();
    /**
     * 记录所有的Activity状态值，判断是否处于前台（Resume）状态
     */
    public ArrayMap<String, Integer> mForegroundServices = new ArrayMap<>();
    /**
     * 不知道有什么用？
     */
    public ArrayMap<String, ArrayMap<String, Integer>> mChooserCounts;

    @Override
    public String toString() {
        return "UsageInfo{" +
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
                ", mActivities=" + mActivities +
                ", mForegroundServices=" + mForegroundServices +
                ", mChooserCounts=" + mChooserCounts +
                '}';
    }
}
