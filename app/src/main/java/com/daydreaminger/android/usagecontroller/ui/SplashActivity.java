package com.daydreaminger.android.usagecontroller.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.daydreaminger.android.usagecontroller.R;
import com.daydreaminger.android.usagecontroller.ui.basic.AppBaseActivity;
import com.daydreaminger.android.usagecontroller.ui.home.HomeActivity;
import com.daydreaminger.android.usagecontroller.utils.AppSettingUtils;

/**
 * @author : daydreaminger
 * @date : 2020/9/30 10:17
 */
public class SplashActivity extends AppBaseActivity {
    private static final String TAG = "SplashActivity";

    final int CODE_PERM_OVER_LAYER = 1024;
    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_splash);
        handler = new Handler();
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed(() -> {
            //check UsageStatus and overLayer permission.
            reqPermission();
        }, 300);
    }

    /**
     * 请求权限，提示用户申请读取应用运行信息以及悬浮窗信息
     */
    private void reqPermission() {
        boolean hasOverWindow = false;
        if (!Settings.canDrawOverlays(getApplicationContext())) {
            //设置悬浮窗权限
            showOverWindowDialog();
        } else {
            hasOverWindow = true;
        }

        boolean hasUsage = false;
        if (!AppSettingUtils.checkSetting(getApplicationContext(), AppSettingUtils.SETTING_APP_USAGE)) {
            showUsagePermissionDialog();
        } else {
            hasUsage = true;
        }

        if (hasOverWindow && hasUsage) {
            //goto home
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
    }

    private void showUsagePermissionDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("手动打开获取应用信息的开关")
                .setNegativeButton("取消", (dialog1, which) -> {
                    dialog1.dismiss();
                    finish();
                })
                .setPositiveButton("设置", (dialog12, which) -> {
                    AppSettingUtils.toUsage(getApplicationContext());
                    dialog12.dismiss();
                    reqPermission();
                })
                .create();
        dialog.show();
    }

    private void showOverWindowDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("需要打开悬浮窗权限")
                .setNegativeButton("取消", (dialog1, which) -> {
                    dialog1.dismiss();
                    finish();
                })
                .setPositiveButton("设置", (dialog12, which) -> {
                    dialog12.dismiss();
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, 1024);
                })
                .create();
        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1024) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                reqPermission();
            }
        }
    }
}
