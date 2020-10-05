package com.daydreaminger.android.usagecontroller.utils;

import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

/**
 * 跳转设置界面的辅助类
 *
 * @author : daydreaminger
 * @date : 2020/8/2 22:06
 */
public class AppSettingUtils {
    private static final String TAG = "AppSettingUtils";

    public static final String SETTING_APP_USAGE = "android:get_usage_stats";

    /**
     * 通过AppOpsManager检查对应的权限或开关状态，从Android 29之后不保证可用，可用版本[19,29~)
     *
     * @param context app context
     * @param op      app setting/switch/permission name.
     * @return if allow to use return true, otherwise return false.
     */
    public static boolean checkSetting(Context context, String op) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return true;
        }

        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode;
        mode = appOps.checkOpNoThrow(op, android.os.Process.myUid(), context.getPackageName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (mode == AppOpsManager.MODE_FOREGROUND) {
                return true;
            }
        }
        return mode == AppOpsManager.MODE_ALLOWED;
    }

    /**
     * 跳转到访问应用运行信息的权限配置页面，设置之后返回申请应用的页面是没有回调的，需要在页面内自行判断
     */
    public static boolean toUsage(Context context) {
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        //系统兼容，在8.0或更高版本，通过非Activity的Context实例启动Activity，需要添加FLAG_ACTIVITY_NEW_TASK标志
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        if (componentName == null) {
            return false;
        }
        context.startActivity(intent);
        return true;
    }
}
