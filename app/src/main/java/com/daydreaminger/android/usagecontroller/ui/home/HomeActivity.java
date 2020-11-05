package com.daydreaminger.android.usagecontroller.ui.home;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.daydreaminger.android.usagecontroller.R;
import com.daydreaminger.android.usagecontroller.ui.basic.AppBaseActivity;

/**
 * Activity + 多Fragment的方式，就要考虑如何完成Activity和Fragment、以及Fragment和Fragment之间的交互；
 *
 * @author : daydreaminger
 * @date : 2020/9/30 10:17
 */
public class HomeActivity extends AppBaseActivity {
    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_home);
        initToolbar();
        initViews();
        initListener();
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        setToolbarTitle("屏幕时间管理");
    }

    private void initViews() {
        runOnUiThread(() -> {
            Log.i(TAG, "load home fragment.");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, HomeFragment.newInstance(), HomeFragment.TAG)
                    .commitAllowingStateLoss();
        });
    }

    private void initListener() {

    }
}
