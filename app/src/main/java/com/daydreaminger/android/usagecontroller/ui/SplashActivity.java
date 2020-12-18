package com.daydreaminger.android.usagecontroller.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.daydreaminger.android.usagecontroller.R;
import com.daydreaminger.android.usagecontroller.databinding.HomeActivitySplashBinding;
import com.daydreaminger.android.usagecontroller.ui.basic.AppBaseActivity;
import com.daydreaminger.android.usagecontroller.utils.AppSettingUtils;

/**
 * show permission grant dialog in splash page.<br></br>
 * if not granted necessary permission, can not use app(exit application).<br/>
 * <p>
 * init step:<br/>
 * 1. show tip to notification user grant necessary permission.<br/>
 * 2. guide to open over layer window permission.<br/>
 * 3. start {@link HomeActivity} and finish itself.<br/>
 *
 * @author : daydreaminger
 * @date : 2020/9/30 10:17
 */
public class SplashActivity extends AppBaseActivity<HomeActivitySplashBinding> {
    private static final String TAG = "SplashActivity";

    private Handler handler;
    /**
     * request get usage stat permission.
     */
    private final int REQ_USAGE_STAT = 2;
    private final int REQ_END = 10;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_splash);
        handler = new InitHandler();
        handler.sendEmptyMessage(REQ_USAGE_STAT);
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume: ");
        super.onResume();
        //need delay time to wait system update.
        handler.sendEmptyMessageDelayed(REQ_USAGE_STAT, 160);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }

    private void showPermissionTipDialog() {
        AlertDialog dialog = new AlertDialog.Builder(SplashActivity.this)
                .setTitle("Tips")
                .setMessage("需要手动授予使用情况访问权限")
                .setNegativeButton("不同意", (dialog1, which) -> {
                    dialog1.dismiss();
                    finish();
                })
                .setPositiveButton("同意", (dialog12, which) -> {
                    dialog12.dismiss();
                    AppSettingUtils.toUsage(getApplicationContext());
                })
                .create();
        dialog.show();
    }

    @SuppressLint("HandlerLeak")
    private class InitHandler extends Handler {

        @Override
        public void dispatchMessage(@NonNull Message msg) {
            switch (msg.what) {
                case REQ_USAGE_STAT:
                    if (!AppSettingUtils.checkSetting(getApplicationContext(), AppSettingUtils.SETTING_APP_USAGE)) {
                        //show dialog
                        showPermissionTipDialog();
                    } else {
                        sendEmptyMessage(REQ_END);
                    }
                    break;
                case REQ_END:
                    //request get usage stat permission
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    finish();
                    break;
            }
        }
    }
}
