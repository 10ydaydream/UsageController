package com.daydreaminger.android.usagecontroller.vm;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.os.Build;
import android.util.ArrayMap;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.blankj.utilcode.util.AppUtils;
import com.daydreaminger.android.usagecontroller.control.UsageHandler;
import com.daydreaminger.android.usagecontroller.model.UsageInfo;
import com.daydreaminger.android.usagecontroller.model.UsageInfoComparator;
import com.daydreaminger.android.usagecontroller.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 获取整合后的使用情况数据
 *
 * @author : daydreaminger
 * @date : 2020/10/1 23:05
 */
public class UsageViewModel extends ViewModel {
    private static final String TAG = "UsageViewModel";

    public static class UsageStatsData {
        public List<UsageInfo> usageInfoList;
        public ArrayMap<String, AppUtils.AppInfo> appInfoMap;

        public UsageStatsData() {
            usageInfoList = new ArrayList<>();
            appInfoMap = new ArrayMap<>();
        }

        public UsageStatsData(List<UsageInfo> stats, ArrayMap<String, AppUtils.AppInfo> appInfo) {
            this.usageInfoList = stats;
            this.appInfoMap = appInfo;
        }
    }

    MutableLiveData<UsageStatsData> usageStates;

    /**
     * 也可以拆分成两个方法，一个获取MutableLiveData实例，另一个发起异步任务的调用
     */
    public MutableLiveData<UsageStatsData> getUsageStates() {
        if (usageStates == null) {
            usageStates = new MutableLiveData<>();
        }
        return usageStates;
    }

    /**
     * 异步读取设备的使用详情
     */
    public void asyncGetUsageState() {
        Disposable task = Observable.create((ObservableOnSubscribe<UsageStatsData>) emitter -> {

            UsageHandler usageHandler = UsageHandler.getInstance();
            ArrayMap<String, AppUtils.AppInfo> appInfoMap = new ArrayMap<>();
            List<UsageStats> data = usageHandler.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, TimeUtils.TIME_DAY);
            data = UsageHandler.mergeUsageStatsData(data);
            List<UsageInfo> result = new ArrayList<>();
            for (UsageStats usageStats : data) {
                UsageInfo info = new UsageInfo();
                UsageHandler.coverPublicField(info, usageStats);
                UsageHandler.coverPrivateField(info, usageStats);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    UsageHandler.coverAndroidQField(info, usageStats);
                }
                result.add(info);
                appInfoMap.put(usageStats.getPackageName(), AppUtils.getAppInfo(usageStats.getPackageName()));
            }
            UsageInfoComparator.sortByUseTime(result);
            //获取了结果后，需要设置MutableLiveData的值
//            usageStates.postValue(data);
            emitter.onNext(new UsageStatsData(result, appInfoMap));
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(usageStatsData -> {
                    usageStates.setValue(usageStatsData);
                    Log.i(TAG, "accept: ");
                }, throwable -> {
                    Log.i(TAG, "accept: error");
                }, () -> {
                    Log.i(TAG, "run: complete.");
                });
    }

}
