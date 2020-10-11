package com.daydreaminger.android.usagecontroller.vm;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.os.Build;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.blankj.utilcode.util.AppUtils;
import com.daydreaminger.android.usagecontroller.control.UsageHandler;
import com.daydreaminger.android.usagecontroller.model.UsageInfo;
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

    MutableLiveData<List<UsageInfo>> usageStates;

    /**
     * 也可以拆分成两个方法，一个获取MutableLiveData实例，另一个发起异步任务的调用
     */
    public MutableLiveData<List<UsageInfo>> getUsageStates() {
        if (usageStates == null) {
            usageStates = new MutableLiveData<>();
        }
        asyncGetUsageState();
        return usageStates;
    }

    /**
     * 异步读取设备的使用详情
     */
    public void asyncGetUsageState() {
        Disposable task = Observable.create((ObservableOnSubscribe<List<UsageInfo>>) emitter -> {

            UsageHandler usageHandler = UsageHandler.getInstance();
            List<UsageStats> data = usageHandler.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, TimeUtils.TIME_DAY);

            List<UsageInfo> result = new ArrayList<>();
            for (UsageStats usageStats : data) {
                UsageInfo info = new UsageInfo();
                UsageHandler.coverPublicField(info, usageStats);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    UsageHandler.coverAndroidQField(info, usageStats);
                }
                info.mAppInfo = AppUtils.getAppInfo(usageStats.getPackageName());
                Log.i(TAG, "asyncGetUsageState: app name:" + info.mAppInfo.getName());
                result.add(info);
            }
            //获取了结果后，需要设置MutableLiveData的值
//            usageStates.postValue(data);

            emitter.onNext(result);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(usageWrappers -> {
                    usageStates.setValue(usageWrappers);
                    Log.i(TAG, "accept: ");
                }, throwable -> {
                    Log.i(TAG, "accept: error");
                    Log.i(TAG, "accept: error");
                }, () -> {
                    Log.i(TAG, "run: complete.");
                    Log.i(TAG, "run: complete.");
                });
    }

}
