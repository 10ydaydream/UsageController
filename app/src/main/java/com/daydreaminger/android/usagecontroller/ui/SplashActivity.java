package com.daydreaminger.android.usagecontroller.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.daydreaminger.android.usagecontroller.R;
import com.daydreaminger.android.usagecontroller.databinding.HomeActivitySplashBinding;
import com.daydreaminger.android.usagecontroller.ui.basic.AppBaseActivity;
import com.daydreaminger.android.usagecontroller.ui.home.HomeActivity;
import com.daydreaminger.android.usagecontroller.utils.AppSettingUtils;

/**
 * show permission grant dialog in splash page.<br></br>
 * if not granted necessary permission, can not use app(exit application).<br/>
 * <p>
 * init step:<br/>
 * 1. show tip to notification user grant necessary permission.<br/>
 * 2. guide to open over layer window permission.<br/>
 * 3. guide to open get device usage stat permission.<br/>
 * 4. start {@link HomeActivity} and finish itself.<br/>
 *
 * @author : daydreaminger
 * @date : 2020/9/30 10:17
 */
public class SplashActivity extends AppBaseActivity<HomeActivitySplashBinding> {
    private static final String TAG = "SplashActivity";

    final int CODE_PERM_OVER_LAYER = 1024;

    private Handler handler;
    private boolean isShowTip = false;
    private final int SHOW_PERM_TIP = 0;
    /**
     * request over layer window permission.
     */
    private final int REQ_OVER_LAYER = 1;
    /**
     * request get usage stat permission.
     */
    private final int REQ_USAGE_STAT = 2;

    private final int GOTO_HOME = 3;
    private final int PERM_NOT_GRANTED = 10;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_splash);
        handler = new InitHandler();
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume: ");
        super.onResume();
        //need 300ms delay time to wait system update.
        handler.postDelayed(() -> {
            if (isShowTip) {
                handler.sendEmptyMessage(CODE_PERM_OVER_LAYER);
            } else {
                handler.sendEmptyMessage(SHOW_PERM_TIP);
            }
        }, 300);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
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
        if (requestCode == 1024) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                reqPermission();
            }
        }
    }

    @SuppressLint("HandlerLeak")
    private class InitHandler extends Handler {

        @Override
        public void dispatchMessage(@NonNull Message msg) {
            switch (msg.what) {
                case SHOW_PERM_TIP:
                    //show dialog,
                    break;
                case REQ_OVER_LAYER:
                    //request over layer permission
                    break;

                case REQ_USAGE_STAT:
                    //request get usage stat permission
                    break;
                default:
                    //if not match message, show a normal dialog to display message.
                    break;
            }
        }
    }
}
