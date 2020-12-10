package com.daydreaminger.android.usagecontroller.viewmodel;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.daydreaminger.android.usagecontroller.control.UsageHandler;
import com.daydreaminger.android.usagecontroller.model.UsageData;
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

    MutableLiveData<List<UsageData>> usageDataWrapper = new MutableLiveData<>();

    public LiveData<List<UsageData>> getUsageDataWrapper() {
        return usageDataWrapper;
    }

    /**
     * 统计当天使用总时间
     */
    public void calDayUsage() {
        Disposable calTask = Observable
                .create((ObservableOnSubscribe<List<UsageData>>) emitter -> {

                    long zeroTime = TimeUtils.currentTimeZero();
                    List<UsageStats> usageStatsList = UsageHandler.getInstance()
                            .queryUsageStats(UsageStatsManager.INTERVAL_DAILY, zeroTime, zeroTime + TimeUtils.TIME_DAY);

                    //analyza
                    List<UsageData> usageData = new ArrayList<>();
                    for (UsageStats usageStats : usageStatsList) {

                    }

                    emitter.onNext(usageData);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(usageStats -> {
                    //
                    Log.i(TAG, "accept: ");
                }, throwable -> {
                    //
                    Log.i(TAG, "accept: ");
                }, () -> {
                    //
                    Log.i(TAG, "run: ");
                });
    }

    public void loadUsageInfo() {

    }
}
