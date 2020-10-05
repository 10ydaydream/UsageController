package com.daydreaminger.android.usagecontroller.ui.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.daydreaminger.android.usagecontroller.R;
import com.daydreaminger.android.usagecontroller.vm.UsageViewModel;

/**
 * @author : daydreaminger
 * @date : 2020/9/30 10:17
 */
public class HomeActivity extends AppBaseActivity {
    private static final String TAG = "HomeActivity";

    UsageViewModel mUsageViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity_home);
        initToolbar();

        mUsageViewModel = new ViewModelProvider(this).get(UsageViewModel.class);
        mUsageViewModel.getUsageStates()
                .observe(this, usageWrappers -> {
                    //after get data.
                    Log.i(TAG, "onCreate: " + usageWrappers.size());
                    Log.i(TAG, "onChanged: " + usageWrappers.toString());
                });
    }

    @Override
    protected void initToolbar() {
        super.initToolbar();
        setToolbarTitle("屏幕时间管理");
    }
}
