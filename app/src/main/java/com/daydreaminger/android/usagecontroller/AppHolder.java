package com.daydreaminger.android.usagecontroller;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

/**
 * 保存APP运行期间的全局变量<br/>
 * 1. Application实例<br/>
 *
 * @author : daydreaminger
 * @date : 2020/10/1 19:23
 */
public class AppHolder {
    private static final String TAG = "AppHolder";

    private static Context APP_CONTEXT = null;
    private static Handler MAIN_HANDLER = null;

    public static void init(Context context) {
        if (context == null) {
            throw new NullPointerException("Application context can not be null!");
        }

        if (context instanceof Application) {
            APP_CONTEXT = context;
        } else {
            APP_CONTEXT = context.getApplicationContext();
        }

        MAIN_HANDLER = new Handler(APP_CONTEXT.getMainLooper());
    }

    public static Context getAppContext() {
        return APP_CONTEXT;
    }

    public static Handler getMainHandler() {
        return MAIN_HANDLER;
    }

    public static void runOnMainThread(Runnable task) {
        MAIN_HANDLER.post(task);
    }
}
