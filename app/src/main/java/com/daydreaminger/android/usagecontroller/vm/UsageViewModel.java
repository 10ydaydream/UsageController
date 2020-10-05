package com.daydreaminger.android.usagecontroller.vm;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.os.Build;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.daydreaminger.android.usagecontroller.control.UsageHandler;
import com.daydreaminger.android.usagecontroller.model.UsageInfo;
import com.daydreaminger.android.usagecontroller.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
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
                result.add(info);
            }
            //获取了结果后，需要设置MutableLiveData的值
//            usageStates.postValue(data);

            emitter.onNext(result);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<UsageInfo>>() {
                    @Override
                    public void accept(List<UsageInfo> usageWrappers) throws Exception {
                        usageStates.setValue(usageWrappers);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.i(TAG, "accept: error");
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.i(TAG, "run: complete.");
                    }
                });
    }
}
