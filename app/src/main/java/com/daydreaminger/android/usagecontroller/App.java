package com.daydreaminger.android.usagecontroller;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * @author : daydreaminger
 * @date : 2020/9/30 10:08
 */
public class App extends Application {
    private static final String TAG = "App";

    @Override
    public void onCreate() {
        super.onCreate();

        AppHolder.init(this);

        //bugly initialize.
        CrashReport.initCrashReport(this, Constants.BuglyAppId, BuildConfig.DEBUG);
    }
}
