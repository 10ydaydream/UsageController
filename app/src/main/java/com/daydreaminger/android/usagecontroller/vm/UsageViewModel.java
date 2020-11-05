package com.daydreaminger.android.usagecontroller.vm;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.daydreaminger.android.usagecontroller.control.UsageHandler;
import com.daydreaminger.android.usagecontroller.utils.TimeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    MutableLiveData<List<UsageStats>> dayUsage = new MutableLiveData<>();

    public MutableLiveData<List<UsageStats>> getDayUsage() {
        return dayUsage;
    }

    /**
     * 统计当天使用总时间
     */
    public void calDayTotalUsage() {
        Disposable calTask = Observable
                .create((ObservableOnSubscribe<List<UsageStats>>) emitter -> {
                    long zeroTime = TimeUtils.currentTimeZero();
                    List<UsageStats> usageStatsList = UsageHandler.getInstance()
                            .queryUsageStats(UsageStatsManager.INTERVAL_DAILY, zeroTime, zeroTime + TimeUtils.TIME_DAY);

                    Map<String, UsageStats> mergeMap = new HashMap<>();
                    for (UsageStats usageStats : usageStatsList) {
                        if (mergeMap.containsKey(usageStats.getPackageName())) {
                            mergeMap.get(usageStats.getPackageName()).add(usageStats);
                        } else {
                            mergeMap.put(usageStats.getPackageName(), usageStats);
                        }
                    }

                    List<UsageStats> mergeList = new ArrayList<>();
                    for (Map.Entry<String, UsageStats> entry : mergeMap.entrySet()) {
                        mergeList.add(entry.getValue());
                    }
                    emitter.onNext(mergeList);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> dayUsage.setValue(list));
    }

    public void loadUsageInfo() {

    }
}
