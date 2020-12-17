package com.daydreaminger.android.usagecontroller.viewmodel;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.daydreaminger.android.usagecontroller.control.UsageHandler;
import com.daydreaminger.android.usagecontroller.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author : daydreaminger
 * @date : 2020/12/5 21:06
 */
public class UsageAnalyzerViewModel extends ViewModel {
    private static final String TAG = "UsageAnalyzerViewModel";

    private MutableLiveData<List<UsageStats>> analyzeResult = new MutableLiveData<>();

    public LiveData<List<UsageStats>> getAnalyzeResult() {
        return analyzeResult;
    }

    public void getTodayAppUsage() {
        Disposable task = Observable
                .create((ObservableOnSubscribe<List<UsageStats>>) emitter -> {
                    long now = System.currentTimeMillis();
                    UsageHandler handler = UsageHandler.getInstance();
                    List<UsageStats> data = handler.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, TimeUtils.currentTimeZero(), now);

                    List<UsageStats> mergeData = new ArrayList<>();
                    List<String> uniPkg = new ArrayList<>();
                    for (UsageStats usage : data) {
                        int index = uniPkg.indexOf(usage.getPackageName());
                        if (index >= 0) {
                            mergeData.get(index).add(usage);
                        } else {
                            uniPkg.add(usage.getPackageName());
                            mergeData.add(usage);
                        }
                    }
                    emitter.onNext(mergeData);

                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(usageStats -> {
                    //
                    analyzeResult.setValue(usageStats);
                }, throwable -> {
                    if (throwable != null) {
                        throwable.printStackTrace();
                    }
                });
    }
}
